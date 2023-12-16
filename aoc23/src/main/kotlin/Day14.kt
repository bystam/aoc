
object Day14: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day14)

    override fun task1(input: Input): Any {
        val platform = Platform(Grid2D.charMatrix(input.lines))
        platform.tiltUntilStable(Vec2D.north)
        return platform.score()
    }

    override fun task2(input: Input): Any {
        val platform = Platform(Grid2D.charMatrix(input.lines))

        val laps = 1_000_000_000
        val scores = mutableListOf<Int>()
        val states = mutableListOf<Set<Point2D>>()
        val cycle = (0..laps).firstNotNullOf {
            platform.tiltUntilStable(Vec2D.north)
            platform.tiltUntilStable(Vec2D.west)
            platform.tiltUntilStable(Vec2D.south)
            platform.tiltUntilStable(Vec2D.east)
            scores += platform.score()
            states += platform.rocks.toSet()

            detectCycle(states)
        }

        val cycleLength = (cycle.last - cycle.first) + 1

        val cyclingScores = scores.drop(cycle.first)
        val fullLaps = (laps - cycle.first) / cycleLength
        val offsetInCycle = (laps - cycle.first) % fullLaps

        return cyclingScores[offsetInCycle - 1]
    }

    private fun <T> detectCycle(scores: List<T>): IntRange? {
        var tortoise = scores.lastIndex
        var hare = tortoise - 1

        while (hare >= 0) {
            var t = tortoise
            var h = hare
            var match = true
            while (h < tortoise) {
                if (scores[t] != scores[h]) {
                    match = false
                    break
                }
                t += 1
                h += 1
            }
            if (match) {
                return (tortoise..scores.lastIndex)
            }
            tortoise -= 1
            hare -= 2
        }
        return null
    }

    class Platform(
        val grid: Grid2D<Char>,
    ) {
        val rocks: MutableSet<Point2D>

        init {
            val rocks = mutableSetOf<Point2D>()
            for (p in grid.allPoints()) {
                if (grid[p] == 'O') {
                    rocks += p
                    grid[p] = '.'
                }
            }
            this.rocks = rocks
        }

        fun tiltUntilStable(direction: Vec2D) {
            val stopped = mutableSetOf<Point2D>()
            while (tick(direction, stopped) > 0) {}
        }

        fun tick(direction: Vec2D, stopped: MutableSet<Point2D>): Int {
            val rocksBefore = rocks.size
            var moved = 0
            val moving = this.rocks.minus(stopped)

            val sorted = when (direction) {
                Vec2D.north -> moving.sortedBy { it.y }
                Vec2D.south -> moving.sortedByDescending { it.y }
                Vec2D.west -> moving.sortedBy { it.x }
                Vec2D.east -> moving.sortedByDescending { it.x }
                else -> TODO()
            }

            for (rock in sorted) {
                val possibleNextPoint = rock.offset(direction)
                if (possibleNextPoint in rocks) {
                    stopped += possibleNextPoint
                    continue
                }
                if (grid.contains(possibleNextPoint) && grid[possibleNextPoint] == '.' && possibleNextPoint !in rocks) {
                    rocks.remove(rock)
                    rocks.add(possibleNextPoint)
                    moved += 1
                }
            }
            require(rocks.size == rocksBefore)
            return moved
        }

        fun score(): Int {
            return rocks.sumOf {
                (grid.height - it.y)
            }
        }
    }
}
