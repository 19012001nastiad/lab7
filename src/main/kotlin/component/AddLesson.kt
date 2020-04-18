package component
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.*
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import kotlin.browser.document

interface AppLessonProps : RProps {
    var newlesson: (String) -> Any }

val fAddLesson =
    functionalComponent<AppLessonProps> { props ->
        div {
            h3 { +"add name lesson"}
            input(type = InputType.text)
            {
                attrs.id = "Lesson"
            }
            input(type = InputType.submit) {
                attrs.onClickFunction =
                    {
                        val lesson = document.getElementById("Lesson")
                                as HTMLInputElement
                        val tmp = lesson.value
                        props.newlesson(tmp)
                    }
            }
        }
    }

fun RBuilder.addlesson(
    newlesson: (String) -> Any
) = child(fAddLesson) {
    attrs.newlesson = newlesson
}