package ro.jf.bk.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookkeeperUserService

fun main(args: Array<String>) {
    runApplication<BookkeeperUserService>(*args)
}
