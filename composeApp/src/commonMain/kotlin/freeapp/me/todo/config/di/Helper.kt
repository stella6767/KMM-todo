package freeapp.me.todo.config.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

// Koin을 초기화하는 함수
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, platformModule) // 공통 모듈과 플랫폼별 모듈을 함께 로드합니다.
    }
}
