package freeapp.me.todo

import androidx.compose.ui.window.ComposeUIViewController
import freeapp.me.todo.config.di.initKoin

fun MainViewController() = ComposeUIViewController(
    //configure = initKoin()
) { App() }
