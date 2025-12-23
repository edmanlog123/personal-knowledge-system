package org.example.search

object SnippetExtractor {

    fun extract(content: String, tokens: List<String>, window: Int = 40): String {
        val lower = content.lowercase()

        for (token in tokens) {
            val idx = lower.indexOf(token)
            if (idx != -1) {
                val start = maxOf(0, idx - window)
                val end = minOf(content.length, idx + token.length + window)
                return "..." + content.substring(start, end) + "..."
            }
        }
        return content.take(window * 2) + "..."
    }
}
