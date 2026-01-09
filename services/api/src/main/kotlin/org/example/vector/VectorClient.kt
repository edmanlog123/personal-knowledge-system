package org.example.vector

import java.net.HttpURLConnection
import java.net.URL
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class VectorResult(
    val id: String,
    val score: Double
)

class VectorClient(
    private val baseUrl: String = "http://${System.getenv("VECTOR_HOST") ?: "localhost"}:${System.getenv("VECTOR_PORT") ?: "9090"}"
) {
    private val mapper = jacksonObjectMapper()

    fun upsert(id: String, vector: List<Float>) {
        val payload = mapOf(
            "id" to id,
            "vector" to vector
        )

        post("/upsert", payload)
    }

    fun search(vector: List<Float>, topK: Int = 5): List<VectorResult> {
        val payload = mapOf(
            "query_vector" to vector,
            "top_k" to topK
        )

        val response = post("/search", payload)
        val json = mapper.readTree(response)

        return json["results"].map {
            VectorResult(
                id = it["id"].asText(),
                score = it["score"].asDouble()
            )
        }
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
