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

    fun searchNotes(query: String): List<org.example.search.SearchResult> {
    val lexicalResults = NoteRepository.search(query)

    val vectorClient = org.example.vector.VectorClient()
    
    val embeddingClient = org.example.vector.EmbeddingClient()
    val queryVector = embeddingClient.embed(query)


    val semanticResults = vectorClient.search(queryVector).map {
        org.example.search.SemanticHit(it.id, it.score)
    }

    return org.example.search.HybridRanker.fuse(
        lexical = lexicalResults,
        semantic = semanticResults
    )
}

}
