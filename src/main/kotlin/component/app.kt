package component

import data.*
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.*

interface AppProps : RProps {
    var students: Array<Student> }

interface AppState : RState {
    var lessons: Array<Lessons>
    var presents: Array<Array<Boolean>> }

interface RouteNumberResult : RProps {
    var number: String

    class App : RComponent<AppProps, AppState>() {
        override fun componentWillMount() {
            state.lessons = lessonsList
            state.presents = Array(state.lessons.size) {
                Array(props.students.size) { false }
            }

        }

        fun newLesson(): (String) -> Any = { new_lesson ->
            setState {
                lessons += Lessons(new_lesson)
                presents += arrayOf(Array(props.students.size) { false })
            }
        }

        override fun RBuilder.render() {
            header {
                h1 { +"App" }
                nav {
                    ul {
                        li { navLink("/lessons") { +"Список предметов" } }
                        li { navLink("/students") { +"Список студентов" } }
                        li { navLink("/addLesson") { +"Добавить предмет" } }
                    }
                }
            }

            switch {
                route("/lessons",
                    exact = true,
                    render = {
                        lessonList(state.lessons)
                    }
                )
                route("/students",
                    exact = true,
                    render = {
                        studentList(props.students)
                    })
                route("/addLesson",
                    exact = true,
                    render = {
                        addlesson(newLesson())
                    }
                )
                route("/lessons/:number",
                    render = { route_props: RouteResultProps<RouteNumberResult> ->
                        val num = route_props.match.params.number.toIntOrNull() ?: -1
                        val subject = state.lessons.getOrNull(num)
                        if (subject != null)
                            lessonFull(
                                subject,
                                props.students,
                                state.presents[num]
                            ) { onClick(num, it) }
                        else
                            p { +"Нет такого предмета" }
                    }
                )
                route("/students/:number",
                    render = { route_props: RouteResultProps<RouteNumberResult> ->
                        val num = route_props.match.params.number.toIntOrNull() ?: -1
                        val student = props.students.getOrNull(num)
                        if (student != null)
                            studentFull(
                                state.lessons,
                                student,
                                state.presents.map {
                                    it[num]
                                }.toTypedArray()
                            ) { onClick(it, num) }
                        else
                            p { +"Нет такого студента" }
                    }
                )
            }
        }

        fun onClick(indexLesson: Int, indexStudent: Int) =
            { _: Event ->
                setState {
                    presents[indexLesson][indexStudent] =
                        !presents[indexLesson][indexStudent]
                }
            }


    }
}
fun RBuilder.app(
    students: Array<Student>
) = child(RouteNumberResult.App::class) {
    attrs.students = students

}