import java.io.BufferedReader
import java.io.Closeable
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.util.stream.Collectors

fun solve(
    day: Day,
    customInput: String? = null
) {
    val input = if (customInput != null)
        Input(customInput.lines())
    else
        Input(read(day.number))
    println("Solution task1: ${day.task1(input)}")
    println("Solution task2: ${day.task2(input)}")
}

abstract class Day(
    val number: Int
) {
    open fun task1(input: Input): String { return "Not implemented" }
    open fun task2(input: Input): String { return "Not implemented" }
}

class Input(
    val lines: List<String>
) {
    fun ints() = lines.map { it.toInt() }
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