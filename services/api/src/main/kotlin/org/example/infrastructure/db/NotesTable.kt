package org.example.infrastructure.db

import org.jetbrains.exposed.sql.Table

object NotesTable : Table("notes") {
    val id = varchar("id", 36)
    val title = varchar("title", 255)
    val content = text("content")

    override val primaryKey = PrimaryKey(id)
}
