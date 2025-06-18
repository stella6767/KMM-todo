package freeapp.me.todo.viewModel

import freeapp.me.todo.model.data.Todo
import freeapp.me.todo.model.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel(
    private val todoRepository: TodoRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main) // UI 스레드에서 작업
) {

    // UI에 노출할 Todo 리스트 상태
    private val _todos
    = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow() // 읽기 전용으로 노출

    // 새 Todo 입력 필드의 텍스트 상태
    private val _newTodoText = MutableStateFlow("")
    val newTodoText: StateFlow<String> = _newTodoText.asStateFlow()

    init {
        // ViewModel 초기화 시 Todo 리스트 로드 시작
        coroutineScope.launch {
            todoRepository.getTodos().collect { todos ->
                _todos.value = todos // 리포지토리에서 변경이 있을 때마다 UI 상태 업데이트
            }
        }
    }


    /**
     * 새 Todo를 추가합니다.
     * 입력 필드가 비어있지 않은 경우에만 저장합니다.
     */
    fun addTodo() {
        coroutineScope.launch {
            val currentText = _newTodoText.value.trim() // 입력 텍스트 양쪽 공백 제거
            if (currentText.isNotBlank()) { // 텍스트가 비어있지 않은지 확인
                // 새로운 Todo 객체를 생성합니다. ID는 간단하게 현재 타임스탬프를 사용합니다.
                val newTodo = Todo(text = currentText)
                todoRepository.insertTodo(newTodo) // Repository를 통해 Todo를 저장합니다.
                _newTodoText.value = "" // Todo 추가 후 입력 필드를 초기화합니다.
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
        coroutineScope.launch {
            todoRepository.toggleTodoStatus(id)
        }
    }

    /**
     * 특정 Todo를 삭제합니다.
     * @param id 삭제할 Todo의 고유 ID
     */
    fun deleteTodo(id: Long) {
        coroutineScope.launch {
            todoRepository.deleteTodoById(id)
        }
    }

    /**
     * 모든 Todo를 삭제합니다.
     */
    fun clearAllTodos() {
        coroutineScope.launch {
            todoRepository.deleteAllTodos()
        }
    }

}
