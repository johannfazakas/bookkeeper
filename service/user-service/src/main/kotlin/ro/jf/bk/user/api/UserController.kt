package ro.jf.bk.user.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ro.jf.bk.user.api.transfer.CreateUserTO
import ro.jf.bk.user.api.transfer.ListTO
import ro.jf.bk.user.api.transfer.UserTO
import ro.jf.bk.user.exception.UserAlreadyExistsException
import ro.jf.bk.user.exception.UserNotFoundException
import ro.jf.bk.user.service.UserService

@RestController
@RequestMapping("/user/v1/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping
    fun getUsers(): ListTO<UserTO> {
        return userService.getUsers()
            .map(UserTO::fromDomain)
            .let(::ListTO)
    }

    @GetMapping("/{username}")
    fun getUser(@PathVariable("username") username: String): UserTO = try {
        userService.getUser(username).let(UserTO::fromDomain)
    } catch (e: UserNotFoundException) {
        throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createUser(@RequestBody createUser: CreateUserTO): UserTO = try {
        userService.createUser(createUser.username)
            .let(UserTO::fromDomain)
    } catch (e: UserAlreadyExistsException) {
        throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.message)
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable("username") username: String) {
        userService.deleteUser(username)
    }
}
