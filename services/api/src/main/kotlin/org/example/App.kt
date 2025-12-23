package org.example

import org.example.infrastructure.db.DatabaseFactory
import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.defaultGraphQLStatusPages
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import org.example.graphql.RootQuery
import org.example.graphql.NoteMutation


import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()


    install(StatusPages) {
        defaultGraphQLStatusPages()
    }

    install(GraphQL) {
        schema {
            packages = listOf("org.example")
            queries = listOf(RootQuery())
            mutations = listOf(NoteMutation())
        }
    }

    routing {
        graphQLPostRoute()
        graphiQLRoute()
    }
}
