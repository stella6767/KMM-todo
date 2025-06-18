package freeapp.me.todo.config.di


import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import freeapp.me.todo.config.db.AppDatabase
import freeapp.me.todo.config.db.AppDatabaseConstructor
import freeapp.me.todo.config.db.DatabaseFactory
import freeapp.me.todo.model.repository.TodoRepository
import freeapp.me.todo.model.repository.TodoRoomRepositoryImpl
import freeapp.me.todo.viewModel.TodoViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module // Koin 모듈 DSL을 위한 import

// 플랫폼별로 제공될 모듈을 위한 expect/actual 정의 (예: Context를 받는 AppDatabase)
expect val platformModule: Module

// 앱의 모든 공통 의존성을 정의하는 Koin 모듈
val appModule = module {
    // single: 싱글톤 객체를 제공합니다. (앱 전체에서 하나의 인스턴스만 존재)
    //single<AppDatabase> { AppDatabaseConstructor.initialize() }

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single<TodoRepository> { TodoRoomRepositoryImpl(get()) }
    // factory: 요청될 때마다 새로운 인스턴스를 제공합니다. (ViewModel에 적합)
    factory { TodoViewModel(get()) } // get()은 Koin 컨테이너에서 TodoRepository 인스턴스를 찾아 주입합니다.
}

// Koin을 초기화하는 함수
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, platformModule) // 공통 모듈과 플랫폼별 모듈을 함께 로드합니다.
    }
}
