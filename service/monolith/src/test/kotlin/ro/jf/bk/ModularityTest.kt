package ro.jf.bk

import org.junit.jupiter.api.Test
import org.springframework.modulith.model.ApplicationModules

class ModularityTest {

    var modules: ApplicationModules = ApplicationModules.of(BookkeeperApplication::class.java)

    @Test
    fun testModules() {
        modules.verify()
    }
}
