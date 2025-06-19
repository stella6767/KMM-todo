package freeapp.me.todo.data.network

import freeapp.me.todo.data.dto.NetworkResponse
import freeapp.me.todo.data.dto.TodoSaveDto
import freeapp.me.todo.domain.model.Todo
import freeapp.me.todo.domain.repository.TodoRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow

class TodoNetworkRepositoryImpl(
    private val httpClient: HttpClient,
    private val baseUrl:String,
) : TodoRepository {



    override suspend fun getTodos(): Flow<List<Todo>> {
        throw NotImplementedError("Not implemented yet")
    }

    override suspend fun getTodosPaginated(
        currentPage: Int,
        pageSize: Int
    ): List<Todo> {

        val response = httpClient.get("$baseUrl/v2/todo/list") {
            url {
                parameters.append("page", currentPage.toString())
                parameters.append("size", pageSize.toString())
            }
        }

        return response.body<NetworkResponse<List<Todo>>>().data
    }

    override suspend fun getTodoById(id: Long): Todo? {
        val body =
            httpClient.get("$baseUrl/v2/todo/${id}").body<NetworkResponse<Todo>>()
        return body.data
    }

    override suspend fun insertTodo(todo: Todo): Todo {

        val response =
            httpClient.post("$baseUrl/todo") {
                contentType(ContentType.Application.Json) // JSON 형태로 보냄
                setBody(TodoSaveDto.fromTodo(todo))
            }

        return response.body<Todo>()
    }

    override suspend fun deleteTodoById(id: Long): Boolean {
        val response: HttpResponse = httpClient.delete("$baseUrl/v2/todo/$id")
        return response.status.isSuccess()
    }

    override suspend fun toggleTodoStatus(id: Long): Todo {
        val body =
            httpClient.put("$baseUrl/v2/todo/$id").body<NetworkResponse<Todo>>()
        return body.data
    }

    override suspend fun deleteAllTodos() {
        httpClient.delete("$baseUrl/v2/todo/list")
    }


}
