package ro.jf.bk.user.web.transfer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

data class ListTO<E>(
    val data: List<E>
) {
    companion object {
        suspend fun <E> Flow<E>.toListTO(): ListTO<E> = ListTO(
            data = this.toList()
        )
    }
}
