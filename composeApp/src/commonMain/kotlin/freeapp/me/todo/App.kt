package freeapp.me.todo


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import freeapp.me.todo.view.screen.TodoAppScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App() {

    MaterialTheme {
        TodoAppScreen()
    }


}
