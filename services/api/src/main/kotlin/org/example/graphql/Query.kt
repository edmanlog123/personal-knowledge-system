package org.example.graphql

import com.expediagroup.graphql.server.operations.Query
import org.example.domain.Note
import org.example.repository.NoteRepository
import org.example.search.SearchResult


class RootQuery: Query {

    

    fun ping(): String = "pong"

    fun allNotes(): List<Note> {
        return NoteRepository.all()
    }

    fun searchNotes(query: String): List<SearchResult> {
    return NoteRepository.search(query)
}
}
