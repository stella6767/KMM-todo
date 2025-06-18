package freeapp.me.todo


import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import freeapp.me.todo.config.LocalTodoViewModel
import freeapp.me.todo.view.screen.TodoAppScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    CompositionLocalProvider(LocalTodoViewModel provides LocalTodoViewModel.current) {
        TodoAppScreen()
    }
}
