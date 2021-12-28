import java.util.*

fun main(args: Array<String>) = solve(Day11())

class Day11 : Day(11) {

    override val testInput: String = """
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """.trimIndent()

    override fun task1(input: Input): Any {
        val grid = Grid2D(
            input.lines.map { line ->
                line.map { Character.getNumericValue(it) }
            }.toList()
        )

        return (0 until 100)
            .sumOf { step(grid) }
    }

    override fun task2(input: Input): Any {
        val grid = Grid2D(
            input.lines.map { line ->
                line.map { Character.getNumericValue(it) }
            }.toList()
        )

        val totalOctopuses = grid.width * grid.height
        return (1 until Int.MAX_VALUE)
            .first { step(grid) == totalOctopuses }
    }

    private fun step(grid: Grid2D<Int>): Int {
        // step 1
        grid.allPoints().forEach { grid[it] = grid[it] + 1 }

        // step 2
        val toFlash = LinkedList<Point2D>().apply {
            addAll(grid.allPoints().filter { grid[it] > 9 })
        }
        val flashed = toFlash.toMutableSet()
        while (toFlash.isNotEmpty()) {
            val next = toFlash.removeFirst()
            val neighbors = grid.neighborsIncludingDiagonal(next)
                .filter { !flashed.contains(it.point) }
            for (neighbor in neighbors) {
                val newValue = neighbor.value + 1
                grid[neighbor.point] = newValue
                if (newValue > 9) {
                    toFlash.add(neighbor.point)
                    flashed.add(neighbor.point)
                }
            }
        }

        // step 3
        flashed.forEach {
            grid[it] = 0
        }

        return flashed.size
    }

    private fun print(grid: Grid2D<Int>) {
        val string = grid.rows.joinToString("\n") {
            it.joinToString("")
        }
        println(string)
        println()
    }
}