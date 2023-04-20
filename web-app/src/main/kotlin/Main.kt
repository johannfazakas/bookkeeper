import kotlinx.browser.document
import react.Fragment
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.h1

fun main() {
    document.bgColor = "orange"
    val container = document.getElementById("root") ?: error("Couldn't find root container!")
    createRoot(container).render(Fragment.create {
        h1 {
            +"Welcome! This is an app! Will decide later what should it do It uses React+Kotlin/JS!"
        }
    })
}
