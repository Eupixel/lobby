package net.eupixel.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun Call.await(): Response = suspendCancellableCoroutine { cont ->
    enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            if (cont.isActive) cont.resumeWithException(e)
        }
        override fun onResponse(call: Call, response: Response) {
            cont.resume(response)
        }
    })
    cont.invokeOnCancellation { cancel() }
}

class PocketBaseFileClient {
    private val host: String = System.getenv("POCKET_HOST") ?: "localhost"
    private val port: Int = System.getenv("POCKET_PORT")?.toInt() ?: 8090
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getValueAwait(collection: String, key: String): String {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host(host)
            .port(port)
            .addPathSegments("api/collections/$collection/records")
            .addQueryParameter("filter", "key=\"$key\"")
            .build()
        val resp = client.newCall(Request.Builder().url(url).build()).await()
        if (!resp.isSuccessful) throw IOException("Record lookup failed: ${resp.code}")
        val items = json.parseToJsonElement(resp.body!!.string())
            .jsonObject["items"]!!.jsonArray
        if (items.isEmpty()) throw IOException("No record found for key: $key")
        return items[0].jsonObject["value"]!!.jsonPrimitive.content
    }

    private suspend fun getRecordIdAwait(collection: String, key: String): String {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host(host)
            .port(port)
            .addPathSegments("api/collections/$collection/records")
            .addQueryParameter("filter", "key=\"$key\"")
            .build()
        val resp = client.newCall(Request.Builder().url(url).build()).await()
        if (!resp.isSuccessful) throw IOException("Record lookup failed: ${resp.code}")
        val items = json.parseToJsonElement(resp.body!!.string())
            .jsonObject["items"]!!.jsonArray
        if (items.isEmpty()) throw IOException("No record found for key: $key")
        return items[0].jsonObject["id"]!!.jsonPrimitive.content
    }

    suspend fun downloadFileByKeyAwait(collection: String, key: String, destFile: Path) {
        val recordId = getRecordIdAwait(collection, key)
        downloadFileAwait(collection, recordId, destFile)
    }

    suspend fun downloadFileAwait(collection: String, recordId: String, destFile: Path) {
        val recordUrl = HttpUrl.Builder()
            .scheme("http")
            .host(host)
            .port(port)
            .addPathSegments("api/collections/$collection/records/$recordId")
            .build()
        val metaResp = client.newCall(Request.Builder().url(recordUrl).build()).await()
        if (!metaResp.isSuccessful) throw IOException("Record lookup failed: ${metaResp.code}")
        val fileName = json
            .parseToJsonElement(metaResp.body!!.string())
            .jsonObject["value"]!!
            .jsonPrimitive
            .content

        val fileUrl = HttpUrl.Builder()
            .scheme("http")
            .host(host)
            .port(port)
            .addPathSegments("api/files/$collection/$recordId/$fileName")
            .build()
        val fileResp = client.newCall(Request.Builder().url(fileUrl).build()).await()
        if (!fileResp.isSuccessful) throw IOException("File download failed: ${fileResp.code}")

        withContext(Dispatchers.IO) {
            Files.newOutputStream(destFile).use { out ->
                fileResp.body!!.byteStream().copyTo(out)
            }
        }
    }
}