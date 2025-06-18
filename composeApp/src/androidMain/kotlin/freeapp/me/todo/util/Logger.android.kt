package freeapp.me.todo.util

import android.util.Log // Android Logcat을 사용합니다.

actual fun platformLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?
) {

    when (level) {
        LogLevel.DEBUG -> Log.d(tag, message, throwable)
        LogLevel.INFO -> Log.i(tag, message, throwable)
        LogLevel.WARN -> Log.w(tag, message, throwable)
        LogLevel.ERROR -> Log.e(tag, message, throwable)
    }
}
