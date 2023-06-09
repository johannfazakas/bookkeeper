package ro.jf.stuff

import org.junit.jupiter.api.Test
import org.springframework.modulith.model.ApplicationModules

class ModularityTest {

    var modules: ApplicationModules = ApplicationModules.of(MoneyFlowApplication::class.java)

    @Test
    fun testModules() {
        modules.verify()
    }
}
