package ro.jf.stuff

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoneyFlowApplication

fun main(args: Array<String>) {
	runApplication<MoneyFlowApplication>(*args)
}
