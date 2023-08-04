import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {
    document.getElementById("root")
        ?.let { container -> createRoot(container).render(App.create()) }
        ?: error("Couldn't find root container!")
}
