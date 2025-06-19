package freeapp.me.todo.config.di


import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import freeapp.me.todo.config.db.AppDatabase
import freeapp.me.todo.config.db.DatabaseFactory
import freeapp.me.todo.config.network.HttpClientFactory
import freeapp.me.todo.data.network.TodoNetworkRepositoryImpl
import freeapp.me.todo.domain.model.Todo
import freeapp.me.todo.domain.repository.TodoRepository
import freeapp.me.todo.data.repository.TodoRoomRepositoryImpl
import freeapp.me.todo.util.Logger
import freeapp.me.todo.util.readResourceFile
import freeapp.me.todo.presentation.todo.TodoViewModel
import freeapp.me.todo.util.Platform
import freeapp.me.todo.util.getPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module // Koin 모듈 DSL을 위한 import

// 플랫폼별로 제공될 모듈을 위한 expect/actual 정의 (예: Context를 받는 AppDatabase)
expect val platformModule: Module

// 앱의 모든 공통 의존성을 정의하는 Koin 모듈
val appModule = module {
    // single: 싱글톤 객체를 제공합니다. (앱 전체에서 하나의 인스턴스만 존재)
    single { Json { ignoreUnknownKeys = true; isLenient = true; prettyPrint = true } }
    single { HttpClientFactory.create(get()) }

    single<AppDatabase> {
        val database = get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val todoDao = database.todoDao()
            val currentTodoCount = todoDao.count()

            if (currentTodoCount == 0) { // DB가 비어있을 경우에만 초기 데이터 삽입

                val jsonString = readResourceFile("files/todos.json")
                val todosToInsert =
                    get<Json>().decodeFromString(ListSerializer(Todo.serializer()), jsonString)
                todoDao.insert(todosToInsert) // 모든 Todo 삽입
                Logger.i("appModule", "Initial todos (${todosToInsert.size} items) inserted from JSON.")

            }
        }

        database
    }

    //single<TodoRepository> { TodoRoomRepositoryImpl(get()) }

    val baseUrl = if (getPlatform().name.contains("Android")) "http://10.0.2.2:8080"
    else "http://localhost:8080"

    single<TodoRepository> { TodoNetworkRepositoryImpl(get(), baseUrl) }

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
