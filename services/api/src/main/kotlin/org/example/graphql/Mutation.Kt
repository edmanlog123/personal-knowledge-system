package org.example.graphql

import com.expediagroup.graphql.server.operations.Mutation
import org.example.domain.Note
import org.example.repository.NoteRepository

class NoteMutation : Mutation {

    

    fun addNote(title: String, content: String): Note {
        return NoteRepository.add(title, content)
    }
}
