package freeapp.me.todo.data.dto

import freeapp.me.todo.domain.model.Todo


data class TodoSaveDto(
    val content: String,
    val status: String,
) {

    companion object {
        fun fromTodo(todo: Todo): TodoSaveDto {
            return TodoSaveDto(
                content = todo.text,
                status = "NORMAL",
            )
        }
    }
}


data class TodoUpdateDto(
    val id: Long,
    val content: String,
    val status: String,
    val isFinish: Boolean,
){

    companion object {
        fun fromTodo(todo: Todo): TodoUpdateDto {
            return TodoUpdateDto(
                id = todo.id,
                content = todo.text,
                status = "NORMAL",
                isFinish = todo.isDone
            )
        }
    }
}
