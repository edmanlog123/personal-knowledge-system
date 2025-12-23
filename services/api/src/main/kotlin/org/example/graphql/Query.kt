package org.example.graphql

import com.expediagroup.graphql.server.operations.Query
import org.example.domain.Note
import org.example.repository.NoteRepository

class RootQuery: Query {

    

    fun ping(): String = "pong"

    fun allNotes(): List<Note> {
        return NoteRepository.all()
    }

    fun searchNotes(query: String): List<Note> {
        return NoteRepository.search(query)
    }
}
