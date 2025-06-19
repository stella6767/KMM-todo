package freeapp.me.todo.presentation.todo

import freeapp.me.todo.domain.model.Todo

data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val isLast: Boolean = false,
    val error: String? = null
)
