package freeapp.me.todo.viewModel

import freeapp.me.todo.model.data.Todo

data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val hasMorePages: Boolean = true,
    val error: String? = null
)
