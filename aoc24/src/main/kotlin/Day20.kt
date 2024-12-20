
object Day20: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day20)

    override fun task1(input: Input): Any {
        val map = Grid2D.charMatrix(input.lines)
        val start = map.allPoints().first { map[it] == 'S' }
        val end = map.allPoints().first { map[it] == 'E' }
        val path = findPath(map, start, end)
        return path.keys.flatMap { point ->
            findAllBasicCheatPotentials(point, path)
        }.count {
            it >= 100
        }
    }

    override fun task2(input: Input): Any {
        val map = Grid2D.charMatrix(input.lines)
        val start = map.allPoints().first { map[it] == 'S' }
        val end = map.allPoints().first { map[it] == 'E' }
        val path = findPath(map, start, end)
        return path.keys.flatMap { point ->
            findAllCoolCheatPotentials(point, path)
        }.count {
            it >= 100
        }
    }

    private fun findPath(map: Grid2D<Char>, start: Point2D, end: Point2D): Map<Point2D, Int> {
        val visited = LinkedHashSet<Point2D>()
        val queue = ArrayDeque<Point2D>().apply { add(start) }
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current in visited) continue
            visited.add(current)

            if (current == end)
                break

            for (direction in Vec2D.allOrthogonal) {
                val next = current + direction
                if (map[next] != '#') queue += next
            }
        }
        return visited.zip(visited.indices).associate { it }
    }

    private fun findAllBasicCheatPotentials(point: Point2D, track: Map<Point2D, Int>): List<Int> {
        val pointIndex = track[point]!!
        val result = mutableListOf<Int>()
        for (direction in Vec2D.allOrthogonal) {
            val jump = direction * 2
            val cheatDestination = point + jump
            val jumpIndex = track[cheatDestination] ?: continue
            val potential = jumpIndex - pointIndex - 2 // takes 2 picoseconds to actually travel
            if (potential > 0) {
                result += potential
            }
        }
        return result
    }

    private fun findAllCoolCheatPotentials(point: Point2D, track: Map<Point2D, Int>): List<Int> {
        val visited = mutableMapOf<Point2D, Int?>()
        val queue = ArrayDeque<Pair<Point2D, Int>>().apply { add(point to 0) }
        val pointIndex = track[point]!!
        while (queue.isNotEmpty()) {
            val (current, distance) = queue.removeFirst()
            if (current in visited) continue
            if (distance > 20) continue

            visited[current] = track[current]?.let { index ->
                val potential = index - pointIndex - distance
                potential.takeIf { it > 0 }
            }

            for (direction in Vec2D.allOrthogonal) {
                val next = current + direction
                queue += (next to distance+1)
            }
        }
        return visited.values.mapNotNull { it }
    }
}
