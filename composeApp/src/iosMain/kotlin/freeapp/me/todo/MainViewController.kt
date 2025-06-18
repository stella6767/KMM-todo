package freeapp.me.todo

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.ComposeUIViewController
import freeapp.me.todo.config.AppModule
import freeapp.me.todo.config.Factory
import freeapp.me.todo.config.LocalTodoViewModel
import kotlinx.coroutines.launch

fun MainViewController() = ComposeUIViewController {

    val scope = rememberCoroutineScope()

    if (!AppModule.isInitialized) {
        scope.launch {
            val iosFactory = Factory()
            AppModule.initialize(platformFactory = iosFactory, useLocalDb = true)
        }
        // 초기화 중일 때 로딩 UI 등을 여기에 표시할 수 있습니다.
    }

    val viewModel = remember { AppModule.todoViewModelFactory() }
    CompositionLocalProvider(
        LocalTodoViewModel provides viewModel,
    ) {
        App()
    }

}
