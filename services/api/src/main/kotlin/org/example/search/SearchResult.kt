package org.example.search

import org.example.domain.Note

data class SearchResult(
    val note: Note,
    val snippet: String,
    val reason: String,
    val score: Int
)
