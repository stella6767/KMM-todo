package freeapp.me.todo.model.network

import io.ktor.client.HttpClient

interface TodoApi {}


class TodoApiImpl(
    private val client: HttpClient,
) : TodoApi {




}
