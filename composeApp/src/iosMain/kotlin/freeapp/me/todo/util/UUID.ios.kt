package freeapp.me.todo.util

actual class UUID actual constructor() {
    actual fun randomUUID(): String {

        return platform.Foundation.NSUUID().UUIDString()
    }
}
