package org.example

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.defaultGraphQLStatusPages
import org.example.graphql.RootQuery

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

    install(StatusPages) {
        defaultGraphQLStatusPages()
    }

    install(GraphQL) {
        schema {
            packages = listOf("org.example.graphql")
            queries = listOf(RootQuery())
        }
    }

    routing {
        graphQLPostRoute()
    }
}
