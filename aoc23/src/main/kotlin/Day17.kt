import java.util.PriorityQueue

object Day17: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day17)

    override fun task1(input: Input): Any {
        val map = Grid2D(
            input.lines.map { line -> line.map { it.digitToInt() } }.toList()
        )
        return dijkstra(
            start = map.upperLeft,
            goal = map.lowerRight,
            map = map,
        )
    }

    override fun task2(input: Input): Any {
        val map = Grid2D(
            input.lines.map { line -> line.map { it.digitToInt() } }.toList()
        )
        return dijkstra2(
            start = map.upperLeft,
            goal = map.lowerRight,
            map = map,
        )
    }

    private fun dijkstra(
        start: Point2D,
        goal: Point2D,
        map: Grid2D<Int>
    ): Int {
        val startCrucible = BasicCrucible(start, Vec2D.east, 0, setOf(start))
        val dist = mutableMapOf(
            startCrucible to 0
        )
        val prev = mutableMapOf<BasicCrucible, BasicCrucible>()
        val queue = PriorityQueue<BasicCrucible> { a, b ->
            val aDist = dist[a] ?: Int.MAX_VALUE
            val bDist = dist[b] ?: Int.MAX_VALUE
            aDist.compareTo(bDist)
        }

        queue.add(startCrucible)

        while (queue.isNotEmpty()) {
            val crucible = queue.remove()
            for (neighbor in crucible.nextPositions(map)) {
                val alt = dist[crucible]!! + map[neighbor.position]
                if (alt < (dist[neighbor] ?: Int.MAX_VALUE)) {
                    dist[neighbor] = alt
                    prev[neighbor] = crucible
                    queue.add(neighbor)
                    if (neighbor.position == goal) return alt
                }
            }
        }

        return dist
            .filter { it.key.position == goal }
            .minOf { it.value }
    }

    private fun dijkstra2(
        start: Point2D,
        goal: Point2D,
        map: Grid2D<Int>
    ): Int {
        val startCrucible1 = UltraCrucible(start, Vec2D.east, 0, setOf(start))
        val startCrucible2 = UltraCrucible(start, Vec2D.south, 0, setOf(start))
        val dist = mutableMapOf(
            startCrucible1 to 0,
            startCrucible2 to 0,
        )
        val prev = mutableMapOf<UltraCrucible, UltraCrucible>()
        val queue = PriorityQueue<UltraCrucible> { a, b ->
            val aDist = dist[a] ?: Int.MAX_VALUE
            val bDist = dist[b] ?: Int.MAX_VALUE
            aDist.compareTo(bDist)
        }

        queue.add(startCrucible1)
        queue.add(startCrucible2)

        while (queue.isNotEmpty()) {
            val crucible = queue.remove()
            for (neighbor in crucible.nextPositions(map)) {
                val alt = dist[crucible]!! + neighbor.position.walk(crucible.position).sumOf { map[it] }
                if (alt < (dist[neighbor] ?: Int.MAX_VALUE)) {
                    dist[neighbor] = alt
                    prev[neighbor] = crucible
                    queue.add(neighbor)
                    if (neighbor.position == goal) return alt
                }
            }
        }

        return dist
            .filter { it.key.position == goal }
            .minOf { it.value }
    }

    interface Crucible {
        val position: Point2D
        fun nextPositions(map: Grid2D<Int>): List<Crucible>
    }

    class BasicCrucible(
        override val position: Point2D,
        val direction: Vec2D,
        val stepInDirection: Int,
        val visited: Set<Point2D>
    ) : Crucible {

        override fun nextPositions(map: Grid2D<Int>): List<BasicCrucible> {
            val positions = mutableListOf<BasicCrucible>()

            // left
            val left = direction.rotated(Rotation90.COUNTERCLOCKWISE)
            val leftPos = position.offset(left)
            if (leftPos !in visited && map.contains(leftPos)) {
                positions += BasicCrucible(
                    position = leftPos,
                    direction = left,
                    stepInDirection = 1,
                    visited = visited + leftPos
                )
            }
            val right = direction.rotated(Rotation90.CLOCKWISE)
            val rightPos = position.offset(right)
            if (rightPos !in visited && map.contains(rightPos)) {
                positions += BasicCrucible(
                    position = rightPos,
                    direction = right,
                    stepInDirection = 1,
                    visited = visited + rightPos
                )
            }
            val forwardPos = position.offset(direction)
            if (stepInDirection < 3 && forwardPos !in visited && map.contains(forwardPos)) {
                positions += BasicCrucible(
                    position = forwardPos,
                    direction = direction,
                    stepInDirection = stepInDirection + 1,
                    visited = visited + forwardPos
                )
            }
            return positions
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as BasicCrucible

            if (position != other.position) return false
            if (direction != other.direction) return false
            if (stepInDirection != other.stepInDirection) return false

            return true
        }

        override fun hashCode(): Int {
            var result = position.hashCode()
            result = 31 * result + direction.hashCode()
            result = 31 * result + stepInDirection
            return result
        }

        override fun toString(): String {
            return "{pos: $position, dir: $direction, step: $stepInDirection}"
        }
    }

    class UltraCrucible(
        override val position: Point2D,
        val direction: Vec2D,
        val stepInDirection: Int,
        val visited: Set<Point2D>
    ) : Crucible {

        override fun nextPositions(map: Grid2D<Int>): List<UltraCrucible> {
            val positions = mutableListOf<UltraCrucible>()

            val left = direction.rotated(Rotation90.COUNTERCLOCKWISE)
            val leftPos = position.offset(left * 4)
            if (leftPos !in visited && map.contains(leftPos)) {
                positions += UltraCrucible(
                    position = leftPos,
                    direction = left,
                    stepInDirection = 4,
                    visited = visited + leftPos
                )
            }

            val right = direction.rotated(Rotation90.CLOCKWISE)
            val rightPos = position.offset(right * 4)
            if (rightPos !in visited && map.contains(rightPos)) {
                positions += UltraCrucible(
                    position = rightPos,
                    direction = right,
                    stepInDirection = 4,
                    visited = visited + rightPos
                )
            }
            if (stepInDirection in (4..<10)) {
                val forwardPos = position.offset(direction)
                if (forwardPos !in visited  && map.contains(forwardPos)) {
                    positions += UltraCrucible(
                        position = forwardPos,
                        direction = direction,
                        stepInDirection = stepInDirection + 1,
                        visited = visited + forwardPos
                    )
                }
            }

            return positions
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as UltraCrucible

            if (position != other.position) return false
            if (direction != other.direction) return false
            if (stepInDirection != other.stepInDirection) return false

            return true
        }

        override fun hashCode(): Int {
            var result = position.hashCode()
            result = 31 * result + direction.hashCode()
            result = 31 * result + stepInDirection
            return result
        }

        override fun toString(): String {
            return "{pos: $position, dir: $direction, step: $stepInDirection}"
        }
    }
}
