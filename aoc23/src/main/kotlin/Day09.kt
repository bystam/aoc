
class Day09: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day09())
    }

    override fun task1(input: Input): Any {
        return input.lines
            .map { History.from(it) }
            .sumOf { it.predictNextLast() }
    }

    override fun task2(input: Input): Any {
        return input.lines
            .map { History.from(it) }
            .sumOf { it.predictNextFirst() }
    }

    class History(
        private val numbers: List<Long>
    ) {

        fun predictNextLast(): Long {
            val sequences = generateSequences()
            return sequences.indices.reversed().drop(1).fold(0L) { acc, index ->
                acc + sequences[index].last()
            }
        }

        fun predictNextFirst(): Long {
            val sequences = generateSequences()
            return sequences.indices.reversed().drop(1).fold(0L) { acc, index ->
                sequences[index].first() - acc
            }
        }

        private fun generateSequences(): List<List<Long>> {
            val result = mutableListOf(numbers)
            while (true) {
                val next = result.last().windowed(2).map { (a, b) -> b - a }
                result += next
                if (next.all { it == 0L }) {
                    break
                }
            }
            return result
        }

        companion object {
            fun from(line: String): History = History(
                line.splitIgnoreEmpty(" ").map { it.toLong() }
            )
        }
    }
}
