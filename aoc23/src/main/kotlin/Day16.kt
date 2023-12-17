
object Day16: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day16)

    override fun task1(input: Input): Any {
        val cavern = Cavern(Grid2D.charMatrix(input.lines))
        return cavern.bfs(
            start = Beam(position = Point2D.origin, direction = Vec2D.east)
        ).size
    }

    override fun task2(input: Input): Any {
        val cavern = Cavern(Grid2D.charMatrix(input.lines))
        val allStarts = mutableListOf<Beam>()
        repeat(cavern.grid.width) { col ->
            allStarts += Beam(
                position = Point2D(x = col, y = 0),
                direction = Vec2D.south
            )
            allStarts += Beam(
                position = Point2D(x = col, y = cavern.grid.height - 1),
                direction = Vec2D.north
            )
        }
        repeat(cavern.grid.height) { row ->
            allStarts += Beam(
                position = Point2D(x = 0, y = row),
                direction = Vec2D.east
            )
            allStarts += Beam(
                position = Point2D(x = cavern.grid.width - 1, y = row),
                direction = Vec2D.west
            )
        }
        return allStarts.maxOf {
            cavern.bfs(it).size
        }
    }

    class Cavern(
        val grid: Grid2D<Char>
    ) {

        fun bfs(start: Beam): Set<Point2D> {
            val visited = mutableSetOf<Beam>()
            val queue = mutableListOf<Beam>(start)

            while (queue.isNotEmpty()) {
                val beam = queue.removeFirst()
                if (beam in visited) continue
                visited.add(beam)
                val neighbors = beam.stepInside(grid)
                queue += neighbors
            }

            return visited.map { it.position }.toSet()
        }
    }

    data class Beam(
        val position: Point2D,
        val direction: Vec2D
    ) {

        fun stepInside(grid: Grid2D<Char>): List<Beam> {
            val nextPosition = position.offset(direction)
            if (!grid.contains(nextPosition)) return emptyList()

            val newDirections = when (grid[nextPosition]) {
                '.' -> {
                    listOf(direction)
                }
                '/' -> {
                    val newDirection = when (direction.isHorizontal) {
                        true -> direction.rotated(Rotation90.COUNTERCLOCKWISE)
                        false -> direction.rotated(Rotation90.CLOCKWISE)
                    }
                    listOf(newDirection)
                }
                '\\' -> {
                    val newDirection = when (direction.isHorizontal) {
                        true -> direction.rotated(Rotation90.CLOCKWISE)
                        false -> direction.rotated(Rotation90.COUNTERCLOCKWISE)
                    }
                    listOf(newDirection)
                }
                '|' -> {
                    if (direction.isVertical) {
                        listOf(direction)
                    } else {
                        listOf(
                            direction.rotated(Rotation90.CLOCKWISE),
                            direction.rotated(Rotation90.COUNTERCLOCKWISE),
                        )
                    }
                }
                '-' -> {
                    if (direction.isHorizontal) {
                        listOf(direction)
                    } else {
                        listOf(
                            direction.rotated(Rotation90.CLOCKWISE),
                            direction.rotated(Rotation90.COUNTERCLOCKWISE),
                        )
                    }
                }
                else -> TODO()
            }
            return newDirections.map {
                Beam(nextPosition, it)
            }
        }
    }
}
