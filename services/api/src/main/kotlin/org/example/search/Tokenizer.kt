package org.example.search

object Tokenizer {

    fun tokenize(text: String): List<String> =
        text
            .lowercase()
            .split(Regex("\\W+"))
            .filter { it.length > 1 }
}
