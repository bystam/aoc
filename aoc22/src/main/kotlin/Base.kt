import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun solve(
    day: Day
) {
    day.testInput?.let {
        val input = Input(it.lineSequence())
        println("Test solution task1: ${day.task1(input)}")
        println("Test solution task2: ${day.task2(input)}")
    }

    val number = day::class.simpleName!!.removePrefix("Day").toInt()
    val input = Input(read(number).asSequence())
    println("Real solution task1: ${day.task1(input)}")
    println("Real solution task2: ${day.task2(input)}")
}

interface Day {

    val testInput: String? get() = null

    fun task1(input: Input): Any { return "Not implemented" }
    fun task2(input: Input): Any { return "Not implemented" }
}

class Input(
    val lines: Sequence<String>
) {
    fun text(): String = lines.joinToString("\n")

    fun ints(): Sequence<Int> = lines.map { it.toInt() }

    fun capture(pattern: String): Sequence<MatchResult.Destructured> {
        val regex = Regex(pattern)
        return lines.map {
            val match = regex.matchEntire(it)!!
            match.destructured
        }
    }
}

private fun read(day: Int): List<String> {
    val url = URL("https://adventofcode.com/2022/day/$day/input")
    val connection = url.openConnection() as HttpURLConnection
    val session = "53616c7465645f5fdefb246c6fc9743e1e24a102d4612e82792db02a4cc0d79cc6e2722942b8432599d823f80d3a750594438112cd6e9c77863175ed4de5c03c"
    connection.setRequestProperty("Cookie", " session=$session")
    return BufferedReader(InputStreamReader(connection.inputStream)).use {
        it.lineSequence().toList()
    }
}
