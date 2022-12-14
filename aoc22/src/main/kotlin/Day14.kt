import java.lang.Integer.max

class Day14: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day14())
    }

    override val testInput: String = """
        498,4 -> 498,6 -> 496,6
        503,4 -> 502,4 -> 502,9 -> 494,9
    """.trimIndent()

    override fun task1(input: Input): Any {
        val paths = input.lines
            .map { line ->
                line.split(" -> ").map {
                    val (x, y) = it.split(",")
                    Point2D(x.toInt(), y.toInt())
                }
            }
        val cave = Cave()
        paths.forEach { cave.fillRock(it) }

        while (cave.step1()) { }

        return cave.restingSandCount
    }

    override fun task2(input: Input): Any {
        val paths = input.lines
            .map { line ->
                line.split(" -> ").map {
                    val (x, y) = it.split(",")
                    Point2D(x.toInt(), y.toInt())
                }
            }
        val cave = Cave()
        paths.forEach { cave.fillRock(it) }

        while (cave.step2()) { }

        return cave.restingSandCount
    }

    class Cave {

        private val sandStart = Point2D(500, 0)
        private var pouringSand: Point2D? = null
        var restingSandCount = 0

        private var floorY = 0

        private val grid: Grid2D<Space> = Grid2D(
            rows = (0..200).map {
                (0..1500).map { Space.EMPTY }
            }
        )

        fun fillRock(path: List<Point2D>) {
            path.windowed(2).forEach { (from, to) ->
                from.walk(toExcluding = to).forEach {
                    grid[it] = Space.ROCK
                    floorY = max(floorY, it.y + 2)
                }
            }
            grid[path.last()] = Space.ROCK
        }

        fun step1(): Boolean {
            val sandPoint = pouringSand ?: run {
                pouringSand = sandStart
                sandStart
            }
            if (sandPoint.y == grid.height - 1) {
                return false // reached bottom, bail
            }
            val candidates = listOf(
                sandPoint.offset(dy = 1),
                sandPoint.offset(dx = -1, dy = 1),
                sandPoint.offset(dx = 1, dy = 1),
            )
            val next = candidates.firstOrNull { grid[it] == Space.EMPTY }
            if (next == null) { // resting
                grid[sandPoint] = Space.SAND
                restingSandCount += 1
                pouringSand = null
            } else {
                pouringSand = next
            }
            return true
        }

        fun step2(): Boolean {
            if (grid[sandStart] == Space.SAND) return false
            val sandPoint = pouringSand ?: run {
                pouringSand = sandStart
                sandStart
            }
            if (sandPoint.y == floorY - 1) {
                grid[sandPoint] = Space.SAND
                restingSandCount += 1
                pouringSand = null
                return true
            }
            val candidates = listOf(
                sandPoint.offset(dy = 1),
                sandPoint.offset(dx = -1, dy = 1),
                sandPoint.offset(dx = 1, dy = 1),
            )
            val next = candidates.firstOrNull { grid[it] == Space.EMPTY }
            if (next == null) { // resting
                grid[sandPoint] = Space.SAND
                restingSandCount += 1
                pouringSand = null
            } else {
                pouringSand = next
            }
            return true
        }
    }

    enum class Space {
        EMPTY, ROCK, SAND
    }
}
