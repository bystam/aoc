
object Day08: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day08)

    override fun task1(input: Input): Any {
        val grid = Grid2D.charMatrix(input.lines)
        val antennas = mutableMapOf<Char, MutableList<Point2D>>()
        for (point in grid.allPoints()) {
            val antenna = grid[point]
            if (antenna == '.') {
                continue
            }
            antennas.getOrPut(antenna) { mutableListOf() }.add(point)
        }

        val allAntinodes = mutableSetOf<Point2D>()

        for (antennaPoints in antennas.values) {
            for ((a, b) in antennaPoints.allPairs()) {
                allAntinodes += b.offset(a.distance(b))
                allAntinodes += a.offset(b.distance(a))
            }
        }

        return allAntinodes.count { grid.contains(it) }
    }

    override fun task2(input: Input): Any {
        val grid = Grid2D.charMatrix(input.lines)
        val antennas = mutableMapOf<Char, MutableList<Point2D>>()
        for (point in grid.allPoints()) {
            val antenna = grid[point]
            if (antenna == '.') {
                continue
            }
            antennas.getOrPut(antenna) { mutableListOf() }.add(point)
        }

        val allAntinodes = mutableSetOf<Point2D>()
        fun walk(from: Point2D, direction: Vec2D) {
            var point = from
            while (true) {
                if (point !in grid) {
                    break
                }
                allAntinodes += point
                point = point.offset(direction)
            }
        }

        for (antennaPoints in antennas.values) {
            for ((a, b) in antennaPoints.allPairs()) {
                walk(b, a.distance(b))
                walk(a, b.distance(a))
            }
        }

        return allAntinodes.size
    }

    private fun <T> List<T>.allPairs(): List<Pair<T, T>> {
        if (size < 2) return emptyList()
        val result = mutableListOf<Pair<T, T>>()
        for (i in indices) {
            for (k in (i+1..lastIndex)) {
                result += (this[i] to this[k])
            }
        }
        return result
    }
}
