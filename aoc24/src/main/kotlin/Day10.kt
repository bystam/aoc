
object Day10: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day10)

    override fun task1(input: Input): Any {
        val map = Grid2D(input.lines.map { line -> line.map { it.digitToInt() } }.toList())

        return map.allPoints()
            .filter { map[it] == 0 }
            .sumOf { map.cringeTrailScore(it) }
    }

    override fun task2(input: Input): Any {
        val map = Grid2D(input.lines.map { line -> line.map { it.digitToInt() } }.toList())

        return map.allPoints()
            .filter { map[it] == 0 }
            .sumOf { map.basedTrailScore(it) }
    }

    private fun Grid2D<Int>.cringeTrailScore(start: Point2D): Int {
        val visited = mutableSetOf<Point2D>()
        val queue = mutableListOf(start)
        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()
            if (point in visited) continue
            visited += point

            val elevation = this[point]
            val hikable = neighbors(point).filter { it.value == elevation + 1 }
            for (neighbor in hikable) {
                queue += neighbor.point
            }
        }

        return visited.count { this[it] == 9 }
    }

    private fun Grid2D<Int>.basedTrailScore(start: Point2D): Int {
        val nines = mutableMapOf<Point2D, Int>()
        val queue = mutableListOf(Trail(start, LinkedHashSet()))
        while (queue.isNotEmpty()) {
            val trail = queue.removeFirst()
            val point = trail.current
            val elevation = this[point]

            if (elevation == 9) {
                nines[point] = (nines[point] ?: 0) + 1
                continue
            }


            val hikable = neighbors(point)
                .filter { it.value == elevation + 1 }
                .filter { it.point !in trail.history }
            for (neighbor in hikable) {
                queue += trail.appending(neighbor.point)
            }
        }

        return nines.values.sum()
    }

    data class Trail(
        val current: Point2D,
        val history: LinkedHashSet<Point2D>,
    ) {
        fun appending(point: Point2D) = Trail(
            current = point,
            history = LinkedHashSet(history).also {
                it.add(current)
            }
        )
    }
}
