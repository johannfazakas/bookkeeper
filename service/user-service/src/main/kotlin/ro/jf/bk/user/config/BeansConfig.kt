package ro.jf.bk.user.config

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import ro.jf.bk.user.handler.UserHandler

class BeansConfig : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        beans {
            bean<UserHandler>()
            bean { userRouter(ref()) }
        }.initialize(applicationContext)
    }
}
