package freeapp.me.todo.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import freeapp.me.todo.util.Logger
import freeapp.me.todo.view.component.TodoItemRow
import freeapp.me.todo.viewModel.TodoViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TodoAppScreen(
    viewModel: TodoViewModel = koinViewModel<TodoViewModel>(),
) {


    //todo loaing 상태표시와 transaction readme 작성

    // ViewModel의 StateFlow를 Compose 상태로 변환하여 UI 업데이트에 사용
    val todos by viewModel.allTodos.collectAsState()
    val newTodoText by viewModel.newTodoText.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val hasMorePages by viewModel.hasMorePages.collectAsState()
    val listState = rememberLazyListState()


    Logger.i("TodoAppScreen", "hello")


    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index } // 마지막 보이는 아이템 인덱스
            .map { it == (listState.layoutInfo.totalItemsCount - 1) } // 마지막 아이템이 보이는지 여부
            .distinctUntilChanged() // 중복 호출 방지
            .filter { it && !isLoading && hasMorePages } // 마지막 아이템이 보이고, 로딩 중이 아니고, 더 로드할 페이지가 있으면
            .collect {
                viewModel.loadNextPage() // 다음 페이지 로드 요청
            }
    }


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
            if (todos.isEmpty() && !isLoading && !hasMorePages) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No todos yet! Add one above.")
                }
            } else {
                LazyColumn(
                    state = listState,
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


                    if (isLoading && hasMorePages) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                }
            }
        }
    }

}
