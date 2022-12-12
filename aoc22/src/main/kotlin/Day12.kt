import java.lang.RuntimeException
import java.util.LinkedList

class Day12: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day12())
    }

    override fun task1(input: Input): Any {
        val map = HeightMap.parse(input)
        return map.bfsFromStartToGoal()
    }

    override fun task2(input: Input): Any {
        val map = HeightMap.parse(input)
        return map.bfsFromGoalToBottom()
    }

    class HeightMap(
        private val start: Point2D,
        private val goal: Point2D,
        private val grid: Grid2D<Char>
    ) {

        fun bfsFromStartToGoal(): Int = bfs(
            from = start,
            toPredicate = { point -> point == goal },
            climbable = { from, to -> (to - from) <= 1 }
        )

        fun bfsFromGoalToBottom(): Int = bfs(
            from = goal,
            toPredicate = { point -> grid[point] == 'a' },
            climbable = { from, to -> (from - to) <= 1 } // going backwards
        )

        private fun bfs(from: Point2D, toPredicate: (Point2D) -> Boolean, climbable: (Char, Char) -> Boolean): Int {
            val visits = mutableMapOf(
                from to 0
            )
            val queue = LinkedList<Point2D>().apply { add(from) }
            while (!queue.isEmpty()) {
                val point = queue.removeFirst()
                val distance = visits[point]!!
                if (toPredicate(point)) return distance

                val elevation = grid[point]
                grid.neighbors(point).forEach {
                    val visited = visits.containsKey(it.point)
                    if (climbable(elevation, it.value) && !visited) {
                        queue.add(it.point)
                        visits[it.point] = distance + 1
                    }
                }
            }
            throw RuntimeException("Wtf")
        }

        companion object {
            fun parse(input: Input): HeightMap {
                val grid = Grid2D(input.lines.map { it.toMutableList() }.toList())
                var start: Point2D? = null
                var goal: Point2D? = null
                grid.allPoints().forEach { p ->
                    if (grid[p] == 'S') {
                        start = p
                        grid[p] = 'a'
                    } else if (grid[p] == 'E') {
                        goal = p
                        grid[p] = 'z'
                    }
                }
                return HeightMap(start!!, goal!!, grid)
            }
        }
    }
}
