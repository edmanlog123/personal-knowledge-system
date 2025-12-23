package org.example.graphql

import com.expediagroup.graphql.server.operations.Query
import org.example.domain.Note
import org.example.repository.NoteRepository

class RootQuery: Query {

    private val repo = NoteRepository()

    fun ping(): String = "pong"

    fun allNotes(): List<Note> {
        return repo.all()
    }

    fun searchNotes(query: String): List<Note> {
        return repo.search(query)
    }
}
