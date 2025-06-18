package freeapp.me.todo.model.data

data class PageImpl<T>(
    val page: Long,
    val pageSize: Int,
    val totalCount: Long,
    val isLast: Boolean,
    val data: T,
) {

    val totalPage = if (pageSize == 0) {
        1L
    } else {
        (totalCount + pageSize - 1) / pageSize
    }
}
