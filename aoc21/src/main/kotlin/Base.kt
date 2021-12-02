import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Collectors

fun solve(
    day: Day
) {
    day.testInput?.let {
        val input = Input(it.lineSequence())
        println("Test solution task1: ${day.task1(input)}")
        println("Test solution task2: ${day.task2(input)}")
    }

    val input = Input(read(day.number).asSequence())
    println("Real solution task1: ${day.task1(input)}")
    println("Real solution task2: ${day.task2(input)}")
}

abstract class Day(
    val number: Int
) {

    open val testInput: String? = null

    open fun task1(input: Input): Any { return "Not implemented" }
    open fun task2(input: Input): Any { return "Not implemented" }
}

class Input(
    val lines: Sequence<String>
) {
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
    val url = URL("https://adventofcode.com/2021/day/$day/input")
    val connection = url.openConnection() as HttpURLConnection
    val session = "53616c7465645f5f6ec28f7dcffce6bba764b21642c4c662e1aabc9c70fe84f943249febc1e9159a045c97f5cfbc05b0"
    connection.setRequestProperty("Cookie", " session=$session")
    return BufferedReader(
        InputStreamReader(
            connection.inputStream
        )
    ).use { it.lines().collect(Collectors.toList()) }
}