package ro.jf.bk.user.exception

class UserNotFoundException(username: String) : RuntimeException("User $username not found")

class UserAlreadyExistsException(username: String) : RuntimeException("User $username already exists")
