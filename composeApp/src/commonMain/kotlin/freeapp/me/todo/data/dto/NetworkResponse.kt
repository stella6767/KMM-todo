package freeapp.me.todo.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class NetworkResponse<T>(
    var resultMsg: String,
    var data: T
)
