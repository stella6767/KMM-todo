package freeapp.me.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import freeapp.me.todo.config.AppModule
import freeapp.me.todo.config.Factory
import freeapp.me.todo.config.LocalTodoViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            val androidFactory = Factory(application)
            AppModule.initialize(platformFactory = androidFactory, useLocalDb = true)
            setContent {
                val viewModel = remember { AppModule.todoViewModelFactory() }
                CompositionLocalProvider(LocalTodoViewModel provides viewModel) {
                    App() // App()이 여기서 LocalTodoViewModel에 접근 가능하도록 감싸져 있음
                }
            }

        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}



