
object Day07: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day07)

    override fun task1(input: Input): Any {
        val grid = Grid2D.charMatrix(input.lines)
        var splits = 0
        var beams = setOf(grid.allPoints().first { grid[it] == 'S' })
        while (beams.first().y < grid.height-1) {
            val nextBeams = mutableSetOf<Point2D>()
            for (beam in beams) {
                val next = beam.offset(dy = 1)
                if (grid[next] == '^') {
                    nextBeams.add(next.offset(dx = -1))
                    nextBeams.add(next.offset(dx = 1))
                    splits += 1
                } else {
                    nextBeams.add(next)
                }
            }
            beams = nextBeams
        }
        return splits
    }

    override fun task2(input: Input): Any {
        val grid = Grid2D.charMatrix(input.lines)

        fun timelines(from: Point2D, memo: MutableMap<Point2D, Long>): Long = memo.getOrPut(from) {
            var hit = from
            while (hit in grid && grid[hit] != '^') {
                hit = hit.offset(dy = 1)
            }
            if (hit !in grid) return 1
            timelines(hit.offset(dx = -1), memo) + timelines(hit.offset(dx = 1), memo)
        }

        return timelines(
            from = grid.allPoints().first { grid[it] == 'S' },
            memo = mutableMapOf(),
        )
    }
}
