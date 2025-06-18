package freeapp.me.todo.config


import androidx.compose.runtime.compositionLocalOf
import freeapp.me.todo.model.repository.TodoRepository
import freeapp.me.todo.model.repository.TodoRoomRepositoryImpl
import freeapp.me.todo.viewModel.TodoViewModel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object AppModule {

    private lateinit var factory: Factory

    lateinit var todoRepository: TodoRepository
        private set // 외부에서 직접 설정 못하게 private set

    // ViewModel 팩토리 (매번 새 ViewModel 인스턴스를 제공)
    val todoViewModelFactory: () -> TodoViewModel by lazy {
        { TodoViewModel(todoRepository) }
    }


    private var _isInitialized = false
    val isInitialized get() = _isInitialized

    private val initializationMutex = Mutex()

    suspend fun initialize(platformFactory: Factory, useLocalDb: Boolean = true) {
        // Mutex를 사용하여 초기화 블록에 대한 접근을 동기화합니다.
        initializationMutex.withLock {
            if (_isInitialized) return // 이미 초기화되었다면 바로 리턴

            this.factory = platformFactory

            // 어떤 TodoRepository 구현체를 사용할지 결정
            todoRepository = if (useLocalDb) {
                TodoRoomRepositoryImpl(factory.createRoomDatabase())
            } else {
                TodoRoomRepositoryImpl(factory.createRoomDatabase())
            }

            _isInitialized = true
        }
    }
}

val LocalTodoViewModel = compositionLocalOf<TodoViewModel> {
    // ViewModel이 제공되지 않았을 때의 기본값 또는 에러 메시지
    error("No TodoViewModel provided. Ensure AppModule.initialize() is called and LocalTodoViewModel is provided via CompositionLocalProvider.")
}
