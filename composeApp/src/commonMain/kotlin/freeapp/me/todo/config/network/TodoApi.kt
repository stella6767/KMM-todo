package freeapp.me.todo.config.network

import freeapp.me.todo.model.data.Todo
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

class TodoApi(
    private val httpClient: HttpClient,
) {

    private val baseUrl = "http://10.0.2.2:8080"

    suspend fun getTodos(): List<Todo> {
        return httpClient.get("$baseUrl/todos").body()
    }

    suspend fun getTodoById(id: Long): Todo? {
        return httpClient.get("$baseUrl/todos/$id").body()
    }

    suspend fun createTodo(todo: Todo): Todo {
        return httpClient.post("$baseUrl/todos") {
            contentType(ContentType.Application.Json) // 요청 Content-Type 설정
            setBody(todo) // 요청 본문에 Todo 객체 직렬화
        }.body()
    }

    suspend fun updateTodo(todo: Todo): Todo {
        return httpClient.put("$baseUrl/todos/${todo.id}") {
            contentType(ContentType.Application.Json)
            setBody(todo)
        }.body()
    }

    suspend fun deleteTodo(id: Long): Boolean {
        val response: HttpResponse = httpClient.delete("$baseUrl/todos/$id")
        return response.status.isSuccess() // HTTP 상태 코드가 2xx이면 성공
    }

    suspend fun toggleTodoStatus(id: Long, isDone: Boolean): Todo? {
        // API가 토글을 위한 전용 엔드포인트를 제공하거나, PATCH/PUT 요청으로 상태만 업데이트한다고 가정
        return httpClient.put("$baseUrl/todos/$id") { // 또는 PATCH
            contentType(ContentType.Application.Json)
            setBody(mapOf("isDone" to isDone)) // isDone 필드만 업데이트
        }.body()
    }

    suspend fun deleteAllTodos(): Boolean {
        val response: HttpResponse = httpClient.delete("$baseUrl/todos")
        return response.status.isSuccess()
    }


}
