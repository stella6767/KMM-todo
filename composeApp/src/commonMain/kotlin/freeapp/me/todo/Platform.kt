package freeapp.me.todo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform