package ro.jf.bk.account.api.model

data class ListTO<E>(
    val data: List<E>
) {
    companion object {
        fun <E> List<E>.toListTO(): ListTO<E> = ListTO(
            data = this.toList()
        )
    }
}
