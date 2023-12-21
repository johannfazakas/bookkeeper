package ro.jf.bk.account.domain.error

class ImportException(message: String, cause: Exception? = null): RuntimeException(message, cause)
