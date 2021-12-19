import java.util.*

fun main(args: Array<String>) = solve(Day09())

class Day09: Day(9) {

    override val testInput: String = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent()

    override fun task1(input: Input): Any {
        val grid = Grid2D(
            input.lines.map { line ->
                line.map { Character.getNumericValue(it) }
            }.toList()
        )
        val sum = grid.allPoints()
            .filter { point ->
                val height = grid[point]
                grid.neighbors(point).all { it.value > height }
            }
            .sumOf { grid[it] + 1 }
        return sum
    }

    override fun task2(input: Input): Any {
        val grid = Grid2D(
            input.lines.map { line ->
                line.map { Character.getNumericValue(it) }
            }.toList()
        )
        val lowPoints = grid.allPoints()
            .filter { point ->
                val height = grid[point]
                grid.neighbors(point).all { it.value > height }
            }
        val (a, b, c) = lowPoints.map { basinSize(grid, it) }
            .sortedDescending()
            .take(3)
            .toList()
        return a * b * c
    }

    private fun basinSize(grid: Grid2D<Int>, start: Point2D): Int {
        val queue = LinkedList<Point2D>().apply { add(start) }
        val visited = mutableSetOf<Point2D>()
        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()
            if (visited.contains(point)) continue
            visited.add(point)
            val value = grid[point]
            val neighbors = grid.neighbors(point)
                .filter { it.value < 9 && it.value > value }
                .filter { !visited.contains(it.point) }
                .map { it.point }
            queue.addAll(neighbors)
        }
        return visited.size
    }
}