package ro.jf.bk.user.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ro.jf.bk.user.api.transfer.CreateUserTO
import ro.jf.bk.user.api.transfer.UserTO
import ro.jf.bk.user.domain.service.UserService

@RestController
@RequestMapping("/user/v1/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping
    fun getUsers(): List<UserTO> {
        return userService.getUsers()
            .map(UserTO::fromDomain)
    }

    @GetMapping("/{username}")
    fun getUser(@PathVariable("username") username: String): UserTO {
        return userService.getUser(username)
            .let(UserTO::fromDomain)
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createUser(@RequestBody createUser: CreateUserTO): UserTO {
        return userService.createUser(createUser.username)
            .let(UserTO::fromDomain)
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable("username") username: String) {
        userService.deleteUser(username)
    }
}
