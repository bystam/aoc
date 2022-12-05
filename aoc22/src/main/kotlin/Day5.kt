import java.util.LinkedList

class Day5 : Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day5())

        private val regex = Regex("move (\\d+) from (\\d+) to (\\d+)")
    }

    override val testInput: String? = """
            [D]    
        [N] [C]    
        [Z] [M] [P]
         1   2   3 
        
        move 1 from 2 to 1
        move 3 from 1 to 3
        move 2 from 2 to 1
        move 1 from 1 to 2
    """.trimIndent()

    override fun task1(input: Input): Any {
        val (start, steps) = input.lines.joinToString("\n")
            .split("\n\n")
        val crates = parseStart(start)

        parseSteps(steps).forEach { (count, from, to) ->
            repeat(count) {
                crates[to]!!.addFirst(crates[from]!!.removeFirst())
            }
        }

        return (1..9).mapNotNull { crates[it]?.firstOrNull() }.joinToString("")
    }

    override fun task2(input: Input): Any {
        val (start, steps) = input.lines.joinToString("\n")
            .split("\n\n")
        val crates = parseStart(start)

        parseSteps(steps).forEach { (count, from, to) ->
            (0 until count)
                .map { crates[from]!!.removeFirst() }
                .reversed()
                .forEach {
                    crates[to]!!.addFirst(it)
                }
        }

        return (1..9).mapNotNull { crates[it]?.firstOrNull() }.joinToString("")
    }

    private fun parseStart(start: String): MutableMap<Int, LinkedList<Char>> {
        val crates = mutableMapOf<Int, LinkedList<Char>>()

        for (line in start.lines().dropLast(1)) {
            line.chunked(4).forEachIndexed { index, crate ->
                val crateId = index + 1
                val letter = crate[1]
                if (!letter.isWhitespace()) {
                    crates.getOrPut(crateId) { LinkedList() }.addLast(letter)
                }
            }
        }
        return crates
    }

    private fun parseSteps(steps: String): List<Triple<Int, Int, Int>> {
        return steps.lines()
            .map { regex.matchEntire(it)!!.destructured }
            .map {
                val (count, from, to) = it.toList().map { it.toInt() }
                Triple(count, from, to)
            }
    }
}
