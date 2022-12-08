class Day8 : Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day8())
    }

    override val testInput: String = """
        30373
        25512
        65332
        33549
        35390
    """.trimIndent()

    override fun task1(input: Input): Any {
        val grid = Grid2D(
            input.lines
                .map { row -> row.map { Tree(it.digitToInt()) } }
                .toList()
        )
        return grid.allPoints()
            .count { grid.treeIsVisibleAt(it) }
    }

    override fun task2(input: Input): Any {
        val grid = Grid2D(
            input.lines
                .map { row -> row.map { Tree(it.digitToInt()) } }
                .toList()
        )
        return grid.allPoints()
            .maxOf { grid.scenicScore(it) }
    }

    private fun Grid2D<Tree>.treeIsVisibleAt(point: Point2D): Boolean {
        val height = this[point].height
        val directions = listOf(
            Vec2D(dx = -1, dy = 0),
            Vec2D(dx = 1, dy = 0),
            Vec2D(dx = 0, dy = -1),
            Vec2D(dx = 0, dy = 1),
        )
        return directions.any { dir ->
            walk(point, dir).all { it.value.height < height }
        }
    }

    private fun Grid2D<Tree>.scenicScore(point: Point2D): Int {
        val height = this[point].height
        val directions = listOf(
            Vec2D(dx = -1, dy = 0),
            Vec2D(dx = 1, dy = 0),
            Vec2D(dx = 0, dy = -1),
            Vec2D(dx = 0, dy = 1),
        )
        return directions.map { dir ->
            // wtf
            var hitBigTree = false
            val trees = walk(point, dir).takeWhile {
                val ret = it.value.height < height
                hitBigTree = !ret
                ret
            }.count()
            trees + if (hitBigTree) 1 else 0
        }.product()
    }

    data class Tree(
        val height: Int
    )
}
