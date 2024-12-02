import kotlin.math.absoluteValue

object Day02: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day02)

    override val testInput: String = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent()

    override fun task1(input: Input): Any {
        val rows = input.lines.map { line ->
            line.split(" ").map { it.toInt() }
        }.toList()
        val grid = Grid2D(rows)
        return grid.rows.count {
            it.isSafe(dampening = false)
        }
    }

    override fun task2(input: Input): Any {
        val rows = input.lines.map { line ->
            line.split(" ").map { it.toInt() }
        }.toList()
        val grid = Grid2D(rows)
        return grid.rows.count {
            it.isSafe(dampening = true) || it.reversed().isSafe(dampening = true)
        }
    }

    private fun List<Int>.isSafe(dampening: Boolean): Boolean {
        val ascending = get(1) > get(0)

        for (col in (0 until lastIndex)) {
            val current = get(col)
            val next = get(col + 1)
            val diff = (current - next).absoluteValue
            val safe = (ascending && current < next || !ascending && current > next) && diff >= 1 && diff <= 3

            if (!safe) {
                if (dampening) {
                    val copy = toMutableList()
                    copy.removeAt(col + 1)
                    return copy.isSafe(dampening = false)
                }
                return false
            }
        }
        return true
    }
}
