package org.example.graphql

import com.expediagroup.graphql.server.operations.Mutation
import org.example.domain.Note
import org.example.repository.NoteRepository

class NoteMutation : Mutation {

    private val repo = NoteRepository()

    fun addNote(title: String, content: String): Note {
        return repo.add(title, content)
    }
}
