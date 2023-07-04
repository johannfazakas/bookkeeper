package ro.jf.bk.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class BookkeeperAccountService

fun main(args: Array<String>) {
    runApplication<BookkeeperAccountService>(*args)
}
