package freeapp.me.todo.util

import todo.composeapp.generated.resources.Res

suspend fun readResourceFile(filePath: String): String {
    return Res.readBytes("files/todos.json").decodeToString()
}


