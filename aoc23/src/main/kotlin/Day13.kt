
object Day13: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day13)

    override fun task1(input: Input): Any {
        val iter = input.lines.iterator()
        val patterns = generateSequence {
            if (iter.hasNext()) Pattern.from(iter) else null
        }

        return patterns.sumOf { it.summary() }
    }

    override fun task2(input: Input): Any {
        val iter = input.lines.iterator()
        val patterns = generateSequence {
            if (iter.hasNext()) Pattern.from(iter) else null
        }

        return patterns.sumOf { it.smudgeFixedSummary() }
    }

    class Pattern(
        val original: Grid2D<Char>
    ) {

        override fun toString(): String = original.gridString()

        fun summary(): Int {
            findHorizontalReflectionPoints(original).firstOrNull()?.let {
                return 100 * it
            }
            return findHorizontalReflectionPoints(original.transposed()).first()
        }

        fun smudgeFixedSummary(): Int {
            val oldHorizontal = findHorizontalReflectionPoints(original).firstOrNull()
            original.allPoints().forEach { p ->
                original.flip(p)
                findHorizontalReflectionPoints(original).firstOrNull { it != oldHorizontal }?.let {
                    return 100 * it
                }
                original.flip(p)
            }

            val transposed = original.transposed()
            val oldVertical = findHorizontalReflectionPoints(transposed).firstOrNull()
            transposed.allPoints().forEach { p ->
                transposed.flip(p)
                findHorizontalReflectionPoints(transposed).firstOrNull { it != oldVertical }?.let {
                    return it
                }
                transposed.flip(p)
            }
            TODO()
        }

        private fun findHorizontalReflectionPoints(grid: Grid2D<Char>): List<Int> {
            return (1..grid.rows.lastIndex).filter { row ->
                val walkUp = grid.rows.subList(0, row).reversed()
                val walkDown = grid.rows.subList(row, grid.rows.size)
                walkUp.zip(walkDown).all { (up, down) -> up == down }
            }
        }

        private fun Grid2D<Char>.flip(point: Point2D) {
            when (this[point]) {
                '#' -> set(point, '.')
                '.' -> set(point, '#')
                else -> TODO("WTF")
            }
        }

        companion object {
            var count = 0

            fun from(iterator: Iterator<String>): Pattern {
                val lines = mutableListOf<String>()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next.isBlank()) break
                    lines += next
                }
                return Pattern(Grid2D.charMatrix(lines.asSequence()))
            }
        }
    }
}
