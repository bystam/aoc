import kotlin.math.max
import kotlin.math.min

object Day05 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day05)

    override fun task1(input: Input): Any {
        val (freshText, availableText) = input.text().split("\n\n")
        val fresh = freshText
            .split("\n").map { line ->
                val (start, end) = line.split("-")
                (start.toLong()..end.toLong())
            }
        val available = availableText.split("\n").map { it.toLong() }
        return available.count { id -> fresh.any { range -> id in range } }
    }

    override fun task2(input: Input): Any {
        val (freshText, _) = input.text().split("\n\n")
        val fresh = freshText
            .split("\n").map { line ->
                val (start, end) = line.split("-")
                (start.toLong()..end.toLong())
            }.sortedBy { it.first }
            .toMutableList()

        var i = 0
        while (i < fresh.lastIndex) {
            val current = fresh[i]
            val next = fresh[i + 1]
            if (current.last >= next.first) {
                // overlap
                fresh[i] = (min(current.first, next.first)..max(current.last, next.last))
                fresh.removeAt(i + 1)
            } else {
                i += 1
            }
        }
        return fresh.sumOf { (it.last - it.first + 1) }
    }
}
