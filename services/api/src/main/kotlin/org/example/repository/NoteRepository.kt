package org.example.repository

import org.example.domain.Note
import java.util.UUID

class NoteRepository {

    private val notes = mutableListOf<Note>()

    fun add(title: String, content: String): Note {
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content
        )
        notes.add(note)
        return note
    }

    fun all(): List<Note> = notes

    fun search(query: String): List<Note> {
        return notes.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.content.contains(query, ignoreCase = true)
        }
    }
}
