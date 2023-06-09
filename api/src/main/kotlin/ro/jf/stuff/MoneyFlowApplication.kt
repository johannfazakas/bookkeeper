package ro.jf.stuff

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class MoneyFlowApplication

fun main(args: Array<String>) {
	runApplication<MoneyFlowApplication>(*args)
}
