package org.example.graphql

import com.expediagroup.graphql.server.operations.Query

class RootQuery : Query {
    fun ping(): String = "pong"
}
