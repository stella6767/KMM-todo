package freeapp.me.todo.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import freeapp.me.todo.util.Platform
import freeapp.me.todo.util.getPlatform
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun HomeScreen() {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // 배경색을 흰색으로 설정
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center, // 세로 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally // 가로 중앙 정렬
        ) {
            Text(
                text = getPlatform().name,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black // 텍스트 색상을 검은색으로 설정
            )
        }
    }
}
