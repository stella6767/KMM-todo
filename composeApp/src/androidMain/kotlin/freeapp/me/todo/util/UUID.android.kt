package freeapp.me.todo.util

actual class UUID actual constructor() {
    actual fun randomUUID(): String {
        return java.util.UUID.randomUUID().toString()
    }
}
