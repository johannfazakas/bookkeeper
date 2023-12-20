package ro.jf.bk.account.api.transfer

data class ListTO<E>(
    val data: List<E>
)

fun <E> List<E>.toListTO(): ListTO<E> = ListTO(
    data = this.toList()
)
