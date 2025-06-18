package freeapp.me.todo.util

import platform.Foundation.NSLog

actual fun platformLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?
) {
    // iOS의 NSLog는 가변 인자를 받으므로, 메시지와 예외를 포맷팅하여 전달합니다.
    val logMessage = buildString {
        append("KMM[")
        append(level.name) // 로그 레벨
        append("] [")
        append(tag) // 태그
        append("]: ")
        append(message) // 메시지
        throwable?.let {
            append("\n")
            append(it.getStackTraceAsString()) // 예외 스택 트레이스 추가
        }
    }
    NSLog("%s", logMessage) // NSString 포맷으로 전달
}


fun Throwable.getStackTraceAsString(): String {
    // 더 자세한 스택 트레이스를 얻으려면 코틀린 런타임의 StackTraceElement API를 사용하거나,
    // Swift/Objective-C 브릿징을 통해 NSException 등을 활용해야 합니다.
    return this.message ?: "Unknown Throwable"
}
