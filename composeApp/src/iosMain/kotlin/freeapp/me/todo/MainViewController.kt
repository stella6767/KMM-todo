package freeapp.me.todo


import androidx.compose.ui.window.ComposeUIViewController
import freeapp.me.todo.config.di.initKoin
import kotlinx.coroutines.launch

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
