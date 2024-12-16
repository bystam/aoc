
object Day16: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day16)

    override fun task1(input: Input): Any {
        val map = Grid2D.charMatrix(input.lines)
        val reindeer = map.allPoints().first { map[it] == 'S' }
        val goal = map.allPoints().first { map[it] == 'E' }
        map[reindeer] = '.'

        val distances = walkEverywhere(map, reindeer)
        return distances
            .filter { it.key.point == goal }
            .minOf { it.value }
    }

    override fun task2(input: Input): Any {
        val map = Grid2D.charMatrix(input.lines)
        val reindeer = map.allPoints().first { map[it] == 'S' }
        val goal = map.allPoints().first { map[it] == 'E' }
        map[reindeer] = '.'

        val distances = walkEverywhere2(map, reindeer)
        val goalSnapshot = distances
            .filter { it.key.point == goal }
            .minBy { it.value.score }
        return findAllPoints(goalSnapshot.value).size
    }

    private fun walkEverywhere(map: Grid2D<Char>, start: Point2D): MutableMap<Position, Int> {
        val queue = ArrayDeque<Pair<Position, Int>>()
        queue.add(Position(start, Vec2D.east) to 0)
        val visited = mutableMapOf<Position, Int>()
        while (queue.isNotEmpty()) {
            val (position, currentScore) = queue.removeFirst()
            val lowestSoFar = visited[position] ?: Int.MAX_VALUE
            if (currentScore >= lowestSoFar) {
                continue
            }

            visited[position] = currentScore

            val (point, direction) = position
            val nextDirection = listOf(
                direction to 1,
                direction.rotated(Rotation90.CLOCKWISE) to 1001,
                direction.rotated(Rotation90.COUNTERCLOCKWISE) to 1001,
                direction.rotated(Rotation90.CLOCKWISE).rotated(Rotation90.CLOCKWISE) to 2001,
            )
            for ((nextDir, score) in nextDirection) {
                val nextPoint = point.offset(nextDir)
                if (map[nextPoint] != '#') {
                    queue.add(Position(nextPoint, nextDir) to currentScore + score)
                }
            }
        }
        return visited
    }

    private fun walkEverywhere2(map: Grid2D<Char>, start: Point2D): MutableMap<Position, Snapshot> {
        val queue = ArrayDeque<Pair<Position, Snapshot>>()
        queue.add(Position(start, Vec2D.east) to Snapshot(0, start, mutableSetOf()))
        val visited = mutableMapOf<Position, Snapshot>()
        while (queue.isNotEmpty()) {
            val (position, currentSnapshot) = queue.removeFirst()
            val bestSnapshot = visited.getOrPut(position) { currentSnapshot }
            if (bestSnapshot.score < currentSnapshot.score) {
                continue
            } else if (bestSnapshot.score == currentSnapshot.score) {
                if (currentSnapshot != bestSnapshot) {
                    bestSnapshot.parents.add(currentSnapshot)
                    continue
                }
            }

            visited[position] = currentSnapshot

            val (point, direction) = position
            val nextDirection = listOf(
                direction to 1,
                direction.rotated(Rotation90.CLOCKWISE) to 1001,
                direction.rotated(Rotation90.COUNTERCLOCKWISE) to 1001,
                direction.rotated(Rotation90.CLOCKWISE).rotated(Rotation90.CLOCKWISE) to 2001,
            )
            for ((nextDir, score) in nextDirection) {
                val nextPoint = point.offset(nextDir)
                if (map[nextPoint] == '#') continue

                val nextPosition = Position(nextPoint, nextDir)

                val nextSnapshot = Snapshot(
                    score = currentSnapshot.score + score,
                    point = nextPoint,
                    parents = mutableSetOf(currentSnapshot),
                )

                if (map[nextPoint] != '#') {
                    queue.add(nextPosition to nextSnapshot)
                }
            }
        }
        return visited
    }

    private fun findAllPoints(snapshot: Snapshot): Set<Point2D> {
        return setOf(snapshot.point) + snapshot.parents.flatMap { findAllPoints(it) }
    }

    data class Position(
        val point: Point2D,
        val direction: Vec2D,
    )

    class Snapshot(
        val score: Int,
        val point: Point2D,
        val parents: MutableSet<Snapshot>,
    )
}
