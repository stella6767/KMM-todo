package freeapp.me.todo.util


/**
 * KMM 프로젝트를 위한 플랫폼 독립적인 로깅 유틸리티.
 * 실제 로깅 구현은 각 플랫폼의 `actual` 구현에서 제공됩니다.
 */

object Logger {
    // 디버그 로그
    fun d(tag: String, message: String) {
        log(LogLevel.DEBUG, tag, message, null)
    }

    // 정보 로그
    fun i(tag: String, message: String) {
        log(LogLevel.INFO, tag, message, null)
    }

    // 경고 로그
    fun w(tag: String, message: String) {
        log(LogLevel.WARN, tag, message, null)
    }

    // 에러 로그 (예외 포함)
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.ERROR, tag, message, throwable)
    }

    // 모든 로그를 처리하는 내부 함수
    private fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        // 실제 플랫폼별 로깅 함수 호출
        platformLog(level, tag, message, throwable)
    }
}

// 로그 레벨을 정의하는 enum
enum class LogLevel {
    DEBUG, INFO, WARN, ERROR
}

// ⭐️ expect 함수 선언: 각 플랫폼에서 실제 구현될 함수 ⭐️
// 이 함수는 'Logger' 객체 내부에서 호출됩니다.
expect fun platformLog(level: LogLevel, tag: String, message: String, throwable: Throwable?)
