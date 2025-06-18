package freeapp.me.todo.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoResponse(
    @SerialName("todos")
    val feed: List<Todo>,
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("currentPage")
    val currentPage: Int,
)
