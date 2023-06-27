package ro.jf.bk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class BookkeeperApplication

fun main(args: Array<String>) {
    runApplication<BookkeeperApplication>(*args)
}
