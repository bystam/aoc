import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

fun solve(
    day: Day
) {

    fun timed(task: () -> Any): Pair<Any, Long> {
        val start = System.currentTimeMillis()
        val ret = task()
        val time = System.currentTimeMillis() - start
        return ret to time
    }

    day.testInput?.let {
        val input = Input(it.lineSequence())
        val (ret1, time1) = timed { day.task1(input) }
        println("Test solution task1: $ret1 (time $time1 ms)")
        val (ret2, time2) = timed { day.task2(input) }
        println("Test solution task2: $ret2 (time $time2 ms)")
    }

    if (!day.skipReal) {
        val number = day::class.simpleName!!.removePrefix("Day").toInt()
        val input = Input(read(number).asSequence())
        val (ret1, time1) = timed { day.task1(input) }
        println("Real solution task1: $ret1 (time $time1 ms)")
        val (ret2, time2) = timed { day.task2(input) }
        println("Real solution task2: $ret2 (time $time2 ms)")
    }
}

interface Day {

    val testInput: String? get() = null
    val skipReal: Boolean get() = false

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
    val url = URI("https://adventofcode.com/2024/day/1/input").toURL()
    val connection = url.openConnection() as HttpURLConnection
    val session = System.getenv("AOC_TOKEN")
    connection.setRequestProperty("Cookie", " session=$session")
    return BufferedReader(InputStreamReader(connection.inputStream)).use {
        it.lineSequence().toList()
    }
}
