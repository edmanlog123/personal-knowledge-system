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

    fun search(query: String): List<Note> =
        transaction {
            NotesTable
                .select {
                    (NotesTable.title like "%$query%") or
                    (NotesTable.content like "%$query%")
                }
                .map {
                    Note(
                        id = it[NotesTable.id],
                        title = it[NotesTable.title],
                        content = it[NotesTable.content]
                    )
                }
        }
}
