package ro.jf.bk

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
// TODO(Johann) create separate integration profile
@ActiveProfiles("local")
class BookkeeperApplicationTest {

    @Test
    fun contextLoads() {
    }
}
