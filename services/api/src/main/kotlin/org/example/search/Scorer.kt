package org.example.search

import org.example.domain.Note

object Scorer {

    fun scoreWithReason(note: Note, queryTokens: List<String>): Pair<Int, String> {
        var score = 0
        val reasons = mutableListOf<String>()

        val titleTokens = Tokenizer.tokenize(note.title)
        val contentTokens = Tokenizer.tokenize(note.content)

        for (token in queryTokens) {
            if (token in titleTokens) {
                score += 3
                reasons.add("matched '$token' in title")
            }
            if (token in contentTokens) {
                score += 1
                reasons.add("matched '$token' in content")
            }
        }

        return score to reasons.distinct().joinToString(", ")
    }
}
