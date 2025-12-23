package org.example.repository

import org.example.domain.Note
import org.example.infrastructure.db.NotesTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object NoteRepository {

    fun add(title: String, content: String): Note =
        transaction {
            val id = java.util.UUID.randomUUID().toString()

            NotesTable.insert {
                it[NotesTable.id] = id
                it[NotesTable.title] = title
                it[NotesTable.content] = content
            }

            Note(id = id, title = title, content = content)
        }

    fun all(): List<Note> =
        transaction {
            NotesTable.selectAll().map {
                Note(
                    id = it[NotesTable.id],
                    title = it[NotesTable.title],
                    content = it[NotesTable.content]
                )
            }
        }

    fun search(query: String): List<org.example.search.SearchResult> {
    val tokens = org.example.search.Tokenizer.tokenize(query)

    return transaction {
        NotesTable
            .selectAll()
            .map {
                Note(
                    id = it[NotesTable.id],
                    title = it[NotesTable.title],
                    content = it[NotesTable.content]
                )
            }
            .map { note ->
                val (score, reason) =
                    org.example.search.Scorer.scoreWithReason(note, tokens)

                val snippet =
                    org.example.search.SnippetExtractor.extract(note.content, tokens)

                org.example.search.SearchResult(
                    note = note,
                    snippet = snippet,
                    reason = reason,
                    score = score
                )
            }
            .filter { it.score > 0 }
            .sortedByDescending { it.score }
    }
}


}
