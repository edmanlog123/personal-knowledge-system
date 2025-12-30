package org.example.graphql

import com.expediagroup.graphql.server.operations.Mutation
import org.example.domain.Note
import org.example.repository.NoteRepository
import org.example.vector.EmbeddingClient
import org.example.vector.VectorClient

class NoteMutation : Mutation {

    private val embeddingClient = EmbeddingClient()
    private val vectorClient = VectorClient()

    fun addNote(title: String, content: String): Note {
        val note = NoteRepository.add(title, content)
        
        // Generate embedding and store in vector DB
        val embedding = embeddingClient.embed("$title $content")
        vectorClient.upsert(note.id, embedding)
        
        return note
    }
}
