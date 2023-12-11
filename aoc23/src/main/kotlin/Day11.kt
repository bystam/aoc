
class Day11: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day11())
    }

    override fun task1(input: Input): Any {
        val space = Space(expand(Grid2D.charMatrix(input.lines)))

        val sum = space.galaxyPoints().sumOf { a ->
            space.galaxyPoints().sumOf { b ->
                a.manhattanDistance(b)
            }
        }

        return sum / 2 // we visit all of them twice
    }

    override fun task2(input: Input): Any {
        val space = Space(Grid2D.charMatrix(input.lines))

        var sum = 0L
        val points = space.galaxyPoints().toList()
        for (i in points.indices) {
            for (j in (i..<points.size)) {
                val a = points[i]
                val b = points[j]
                sum += a.manhattanDistance(b) + space.getTotalExpansionBetween(a, b)
            }
        }

        return sum
    }

    class Space(
        val grid: Grid2D<Char>
    ) {

        val emptyRows: Set<Int> = (0..<grid.height).mapNotNull { row ->
            row.takeIf { grid.rows[row].all { it == '.' } }
        }.toSet()

        val emptyCols: Set<Int> = (0..<grid.width).mapNotNull { col ->
            col.takeIf { grid.rows.indices.all { row -> grid.rows[row][col] == '.' } }
        }.toSet()

        fun galaxyPoints(): Sequence<Point2D> = grid.allPoints()
            .filter { grid[it] == '#' }

        fun getTotalExpansionBetween(a: Point2D, b: Point2D): Int {
            val xDist = (minOf(a.x, b.x)..maxOf(a.x, b.x))
            val yDist = (minOf(a.y, b.y)..maxOf(a.y, b.y))
            var total = 0
            emptyRows.forEach { row ->
                if (yDist.contains(row)) {
                    total += 1000000 - 1
                }
            }
            emptyCols.forEach { col ->
                if (xDist.contains(col)) {
                    total += 1000000 - 1
                }
            }
            return total
        }

        override fun toString(): String = grid.rows.joinToString("\n") { it.joinToString("") }
    }

    private fun expand(grid: Grid2D<Char>): Grid2D<Char> {
        val rows = grid.rows.map { it.toMutableList() }.toMutableList()
        val emptyRowIndices = (0..<grid.height).mapNotNull { row ->
            row.takeIf { rows[row].all { it == '.' } }
        }
        val emptyColIndices = (0..<grid.width).mapNotNull { col ->
            col.takeIf { rows.indices.all { row -> rows[row][col] == '.' } }
        }
        for (row in rows) {
            for (c in emptyColIndices.reversed()) {
                row.add(c, row[c])
            }
        }
        for (r in emptyRowIndices.reversed()) {
            rows.add(r, rows[r])
        }
        return Grid2D(rows)
    }
}
