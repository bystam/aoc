
object Day04: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day04)

    override val testInput: String = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent()

    override fun task1(input: Input): Any {
        val grid = Grid2D.charMatrix(input.lines)
        var count = 0
        for (point in grid.allPoints()) {
            for (dir in Vec2D.all) {
                if (grid.detectWord("XMAS", point, dir)) {
                    count += 1
                }
            }
        }
        return count
    }

    override fun task2(input: Input): Any {
        val grid = Grid2D.charMatrix(input.lines)
        val allMas = mutableSetOf<Triple<Point2D, Point2D, Point2D>>()
        for (point in grid.allPoints()) {
            for (dir in Vec2D.all) {
                if (grid.detectWord("MAS", point, dir)) {
                    allMas += Triple(
                        point, point.offset(dir), point.offset(dir * 2)
                    )
                }
            }
        }

        var count = 0
        for (candidate in allMas) {
            val otherStartToMakeX = Vec2D.allOrthogonal
                .map { candidate.first.offset(it * 2) }
            for (other in otherStartToMakeX) {
                for (possible in allMas.filter { it.first == other }) {
                    if (possible.second == candidate.second) {
                        count += 1
                    }
                }
            }
        }

        return count / 2 // found them all twice
    }

    private fun Grid2D<Char>.detectWord(word: String, from: Point2D, direction: Vec2D): Boolean {
        var point = from
        return word.all { char ->
            val actual = getSafe(point)
            point = point.offset(direction)
            actual == char
        }
    }
}
