package freeapp.me.todo.util

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
