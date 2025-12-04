object Day04 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day04)

    override fun task1(input: Input): Any {
        val grid = Grid2D.charMatrix(input.lines)
        return grid.allPoints()
            .filter { grid[it] == '@' }
            .count { p ->
                val adjacentRollCount = grid.neighborsIncludingDiagonal(p).count { it.value == '@' }
                adjacentRollCount < 4
            }
    }

    override fun task2(input: Input): Any {
        val grid = Grid2D.charMatrix(input.lines)
        val originalRolls = grid.allPoints().filter { grid[it] == '@' }
        val rolls = originalRolls.toMutableSet()

        while (true) {
            val toRemove = mutableSetOf<Point2D>()
            for (roll in rolls) {
                val adjacentRollCount = grid.neighborsIncludingDiagonal(roll).count { it.point in rolls }
                if (adjacentRollCount < 4) {
                    toRemove += roll
                }
            }

            if (toRemove.isEmpty()) break

            for (roll in toRemove) {
                rolls.remove(roll)
            }
        }
        return originalRolls.count() - rolls.size
    }
}
