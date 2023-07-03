package ro.jf.bk.user.transfer

data class ListTO<E>(
    val data: List<E>
) {
    companion object {
        fun <E> List<E>.toListTO(): ListTO<E> = ListTO(
            data = this
        )
    }
}
