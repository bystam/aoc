
object Day18: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day18)

    override fun task1(input: Input): Any {
        val bytes = input.lines.map {
            val (x, y) = it.split(",")
            Point2D(x.toInt(), y.toInt())
        }.take(1024).toSet()

        val start = Point2D.origin
        val end = Point2D(70, 70)
        val rect = BoundingRect2D(start, end)
        return findPath(rect, bytes, start, end).size - 1
    }

    override fun task2(input: Input): Any {
        val bytes = input.lines.map {
            val (x, y) = it.split(",")
            Point2D(x.toInt(), y.toInt())
        }

        val start = Point2D.origin
        val end = Point2D(70, 70)
        val rect = BoundingRect2D(start, end)
        return bytes.scan(emptyList<Point2D>()) { acc, byte ->
            acc + byte
        }.first { bytesSoFar ->
            findPath(rect, bytesSoFar.toSet(), start, end).isEmpty()
        }.last()
    }

    private fun findPath(rect: BoundingRect2D, bytes: Set<Point2D>, start: Point2D, end: Point2D): List<Point2D> {
        val queue = ArrayDeque(listOf(start))
        val visited = mutableMapOf<Point2D, Point2D?>(
            start to null
        )
        var found = false
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current == end) {
                found = true
                break
            }

            for (dir in Vec2D.allOrthogonal) {
                val next = current.offset(dir)
                if (next in rect && !visited.containsKey(next) && next !in bytes) {
                    visited[next] = current
                    queue += next
                }
            }
        }

        if (!found) {
            return emptyList()
        }

        var point: Point2D? = end
        return generateSequence {
            val next = point
            point = visited[point]
            next
        }.toList()
    }
}
