package freeapp.me.todo.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val isDone: Boolean = false,
    val createdAt: Instant = Clock.System.now() // 생성 시각 (kotlinx-datetime 사용)
) {

}
