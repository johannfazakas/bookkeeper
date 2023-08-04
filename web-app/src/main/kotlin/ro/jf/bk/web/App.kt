package ro.jf.bk.web

import ro.jf.bk.web.integration.model.UsersResponse
import ro.jf.bk.web.integration.getUsers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import react.useEffectOnce
import react.useState

val App = FC<Props> {
    var users: UsersResponse? by useState(UsersResponse.empty())

    val mainScope = MainScope()

    useEffectOnce {
        mainScope.launch {
            users = getUsers()
        }
    }

    h1 { +"Welcome! This is an app! Bookkeeper app! Or is it celebration planner?" }

    users?.data?.forEach { user ->
        p { +user.username }
    }
}
