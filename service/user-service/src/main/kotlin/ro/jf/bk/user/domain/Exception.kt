package ro.jf.bk.user.domain

class UserExistsException(val username: String) : RuntimeException()
