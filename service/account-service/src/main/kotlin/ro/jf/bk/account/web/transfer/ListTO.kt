package ro.jf.bk.account.web.transfer

data class ListTO<E>(
    val data: List<E>
) {
    companion object {
        fun <E> List<E>.toListTO(): ListTO<E> = ListTO(
            data = this.toList()
        )
    }
}
