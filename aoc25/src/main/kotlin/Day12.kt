object Day12 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day12)

    override fun task1(input: Input): Any {
        val chunks = input.text().split("\n\n")
        val presents = chunks.dropLast(1).map {
            Present(shape = it.lines().drop(1))
        }
        val regions = chunks.last().lines().map { Region.from(it) }

        return regions.count {
            val size = it.width * it.length
            val presentMinPossibleSize = it.presentCounts.withIndex().sumOf { (index, count) ->
                presents[index].size * count
            }
            size >= presentMinPossibleSize
        }
    }

    override fun task2(input: Input): Any {
        return "TODO"
    }

    data class Present(
        val shape: List<String>
    ) {
        override fun toString(): String = shape.joinToString("\n")

        val size: Int = shape.sumOf { line -> line.count { it == '#' } }
    }

    data class Region(
        val width: Int,
        val length: Int,
        val presentCounts: List<Int>,
    ) {
        override fun toString(): String = "${width}x${length}: ${presentCounts.joinToString(" ")}"

        companion object {
            fun from(line: String): Region {
                val (size, presents) = line.split(": ")
                val (width, length) = size.split("x")
                val presentCounts = presents.split(" ").map { it.toInt() }
                return Region(width.toInt(), length.toInt(), presentCounts)
            }
        }
    }
}
