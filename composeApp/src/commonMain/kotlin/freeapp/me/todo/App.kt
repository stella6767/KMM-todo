package freeapp.me.todo


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import freeapp.me.todo.presentation.home.HomeScreen
import freeapp.me.todo.presentation.todo.TodoAppScreen
import freeapp.me.todo.util.Logger
import freeapp.me.todo.util.getPlatform

@Composable
fun App() {

    Logger.i("App", "hello ${getPlatform().name}")

    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        //val navController = rememberNavController()
        TodoAppScreen()
        // HomeScreen()
    }
}

