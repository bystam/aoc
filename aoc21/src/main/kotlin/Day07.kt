import kotlin.math.abs

fun main(args: Array<String>) = solve(Day07())

class Day07: Day(7) {

    override val testInput: String?
        get() = "16,1,2,0,4,2,7,1,2,14"

    override fun task1(input: Input): Any {
        val positions = input.lines.single().split(",").map { it.toInt() }.sorted()
        val median = (positions[positions.size / 2] + positions[positions.size / 2] + 1) / 2
        return positions.sumOf { abs(it - median) }
    }

    override fun task2(input: Input): Any {
        val positions = input.lines.single().split(",").map { it.toInt() }.sorted()
        val median = (positions[positions.size / 2] + positions[positions.size / 2] + 1) / 2

        var position = median
        var distance = task2Distance(positions, position)
        val delta = if (task2Distance(positions, position + 1) < distance) +1 else -1
        while (true) {
            position += delta
            val newDistance = task2Distance(positions, position)
            if (newDistance > distance) break
            distance = newDistance
        }
        return distance
    }

    private fun task2Distance(positions: List<Int>, destination: Int): Int = positions.sumOf { value ->
        val distance = abs(value - destination)
        distance * (distance + 1) / 2
    }
}