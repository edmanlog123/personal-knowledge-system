package org.example.vector

import java.net.HttpURLConnection
import java.net.URL
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class EmbeddingClient(
    private val baseUrl: String = "http://localhost:8081"
) {
    private val mapper = jacksonObjectMapper()

    fun embed(text: String): List<Float> {
        val payload = mapOf("text" to text)
        val response = post("/embed", payload)
        val json = mapper.readTree(response)
        return json["vector"].map { it.floatValue() }
    }

    private fun post(path: String, body: Any): String {
        val url = URL("$baseUrl$path")
        val conn = url.openConnection() as HttpURLConnection

        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json")
        conn.doOutput = true

        conn.outputStream.use {
            it.write(mapper.writeValueAsBytes(body))
        }

        return conn.inputStream.bufferedReader().readText()
    }
}
