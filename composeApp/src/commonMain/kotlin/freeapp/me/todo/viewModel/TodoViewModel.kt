package freeapp.me.todo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import freeapp.me.todo.model.data.PageImpl
import freeapp.me.todo.model.data.Todo
import freeapp.me.todo.model.repository.TodoRepository
import freeapp.me.todo.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoViewModel(
    private val todoRepository: TodoRepository,
) : ViewModel() {

    private val PAGE_SIZE = 20

    private val _requestPage = MutableStateFlow(1)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasMorePages = MutableStateFlow(true)
    val hasMorePages: StateFlow<Boolean> = _hasMorePages.asStateFlow()

    // ⭐️ 누적된 전체 Todo 목록 (UI에 노출) ⭐️
    private val _allTodos = MutableStateFlow<List<Todo>>(emptyList())
    val allTodos: StateFlow<List<Todo>> = _allTodos.asStateFlow()

    // 새 Todo 입력 필드의 텍스트 상태
    private val _newTodoText = MutableStateFlow("")
    val newTodoText: StateFlow<String> = _newTodoText.asStateFlow()

    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 1) // 데이터 새로고침 트리거


    init {
        viewModelScope.launch {
            loadTodos()
        }
    }


    private suspend fun loadTodos() {

        _refreshTrigger
            .onStart { emit(Unit) } // ViewModel 초기화 시 첫 로드를 위해 트리거 한 번 발생
            .flatMapLatest { // 트리거 발생 시 _requestPage 스트림을 재시작
                // _requestPage Flow가 변경될 때마다 실제 페이지 로드 Flow를 생성
                _requestPage.flatMapLatest { page ->
                    _isLoading.value = true
                    Logger.d("loadTodos", "Requesting page: $page")
                    todoRepository.getTodosPaginated(page, PAGE_SIZE)
                }
            }.scan(PageImpl(1, 0, 0, false, emptyList<Todo>())) { acc, newPageImpl ->
                if (newPageImpl.page == 1L) {
                    newPageImpl
                } else {
                    newPageImpl.copy(data = acc.data + newPageImpl.data)
                }
            }.collectLatest { accumulatedPageImpl ->
                _allTodos.value = accumulatedPageImpl.data
                _hasMorePages.value = !accumulatedPageImpl.isLast
                Logger.d(
                    "loadTodos",
                    "Accumulated todos: ${accumulatedPageImpl.data.size}, IsLast: ${accumulatedPageImpl.isLast}, TotalPages: ${accumulatedPageImpl.totalPage}, CurrentPage: ${accumulatedPageImpl.page}"
                )
                _isLoading.value = false
            }
    }


    private fun refreshData() {
        viewModelScope.launch {
            _requestPage.value = 1
            _allTodos.value = emptyList()
            _hasMorePages.value = true
            _refreshTrigger.emit(Unit)
            Logger.d("refreshData", "Data refresh triggered.")
        }
    }


    fun loadNextPage() {
        if (_isLoading.value || !_hasMorePages.value) {
            Logger.d(
                "loadNextPage",
                "Not loading next page. Is Loading: ${_isLoading.value}, Has More: ${_hasMorePages.value}"
            )
            return
        }
        _requestPage.value = _requestPage.value + 1
        Logger.d("loadNextPage", "Triggered next page load: ${_requestPage.value}")
    }


    /**
     * 새 Todo를 추가합니다.
     * 입력 필드가 비어있지 않은 경우에만 저장합니다.
     */
    fun addTodo() {
        viewModelScope.launch {
            val currentText = _newTodoText.value.trim() // 입력 텍스트 양쪽 공백 제거
            if (currentText.isNotBlank()) { // 텍스트가 비어있지 않은지 확인
                // 새로운 Todo 객체를 생성합니다. ID는 간단하게 현재 타임스탬프를 사용합니다.
                val newTodo = Todo(text = currentText)
                todoRepository.insertTodo(newTodo) // Repository를 통해 Todo를 저장합니다.
                _newTodoText.value = "" // Todo 추가 후 입력 필드를 초기화합니다.
                refreshData()
                Logger.i("test", "insert todo!! ")
            }
        }
    }

    /**
     * 새 Todo 입력 필드의 텍스트를 업데이트합니다.
     */
    fun updateNewTodoText(text: String) {
        _newTodoText.value = text
    }


    /**
     * 특정 Todo의 완료 상태를 토글합니다.
     * @param id 토글할 Todo의 고유 ID
     */
    fun toggleTodoStatus(id: Long) {
        viewModelScope.launch {
            //DB 업데이트
            val updatedTodo =
                todoRepository.toggleTodoStatus(id)
            //UI 동기화
            _allTodos.update { currentTodos ->
                currentTodos.map { todo ->
                    if (todo.id == updatedTodo.id) updatedTodo else todo
                }
            }
        }
    }

    /**
     * 특정 Todo를 삭제합니다.
     * @param id 삭제할 Todo의 고유 ID
     */
    fun deleteTodo(id: Long) {
        viewModelScope.launch {
            todoRepository.deleteTodoById(id)
        }
    }

    /**
     * 모든 Todo를 삭제합니다.
     */
    fun clearAllTodos() {
        viewModelScope.launch {
            todoRepository.deleteAllTodos()
        }
    }

}
