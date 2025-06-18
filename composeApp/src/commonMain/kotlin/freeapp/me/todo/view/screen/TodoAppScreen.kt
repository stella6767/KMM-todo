package freeapp.me.todo.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import freeapp.me.todo.config.LocalTodoViewModel
import freeapp.me.todo.view.component.TodoItemRow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoAppScreen() {
    // ViewModel의 StateFlow를 Compose 상태로 변환하여 UI 업데이트에 사용


    val viewModel = LocalTodoViewModel.current


    val todos by viewModel.todos.collectAsState()
    val newTodoText by viewModel.newTodoText.collectAsState()


    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("KMM Todo App") },
                    actions = {
                        IconButton(onClick = viewModel::clearAllTodos) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear all todos")
                        }
                    }
                )
            },
            floatingActionButton = {
                // 필요하다면 여기에 FAB 추가 (예: 새 Todo 추가 다이얼로그 호출)
                // 현재는 입력 필드가 고정되어 있으므로 사용하지 않음
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // 새 Todo 추가 섹션
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newTodoText,
                        onValueChange = viewModel::updateNewTodoText,
                        label = { Text("New Todo") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Button(
                        onClick = viewModel::addTodo,
                        enabled = newTodoText.isNotBlank() // 입력 필드가 비어있지 않을 때만 활성화
                    ) {
                        Text("Add")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Todo 리스트 섹션
                if (todos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No todos yet! Add one above.")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f), // 남은 공간 차지
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(todos.size) { index ->
                            val todo = todos[index]
                            TodoItemRow(
                                todo = todo,
                                onToggle = { viewModel.toggleTodoStatus(todo.id) },
                                onDelete = { viewModel.deleteTodo(todo.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
