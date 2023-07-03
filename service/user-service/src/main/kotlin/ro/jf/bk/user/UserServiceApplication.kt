package ro.jf.bk.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ro.jf.bk.user.config.BeansConfig

@SpringBootApplication
class UserServiceApplication

fun main(args: Array<String>) {
    runApplication<UserServiceApplication>(*args) {
        addInitializers(BeansConfig())
    }
}
