fun main(args: Array<String>) = solve(Day08())

class Day08: Day(8) {

    companion object {
        val numbers = listOf("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg")
            .map { it.toSet() }
    }

    override val testInput: String = """
        acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
    """.trimIndent()

    override fun task1(input: Input): Any {
        return input.lines
            .map { Entry.parse(it) }
            .flatMap { it.output }
            .count { listOf(2, 3, 4, 7).contains(it.length) }
    }

    override fun task2(input: Input): Any {
        val entries = input.lines.map { Entry.parse(it) }

        var total = 0
        for (entry in entries) {
            val solution = mutableMapOf<Char, Char>()
            val all = entry.input.map { it.toSet() }
            val one = all.single { it.size == 2 }
            val seven = all.single { it.size == 3 }
            val eight = all.single { it.size == 7 }
            val zeroSixNine = all.filter { it.size == 6 }
            val twoThreeFive = all.filter { it.size == 5 }
            val six = zeroSixNine.single { !it.containsAll(one) }
            val five = twoThreeFive.single { six.minus(it).size == 1 }
            val three = twoThreeFive.single { it.containsAll(one) }

            solution['a'] = seven.minus(one).single()
            solution['c'] = eight.minus(six).single()
            solution['f'] = one.minus(solution['c']!!).single()
            solution['e'] = six.minus(five).single()
            solution['b'] = eight.minus(three).minus(solution['e']!!).single()
            val zero = zeroSixNine.single { it != six && it.contains(solution['e']!!) }
            solution['d'] = eight.minus(zero).single()
            solution['g'] = eight.minus(solution.values.toSet()).single()

            val reversed = solution.entries.associateBy({ it.value }) { it.key }

            val outputValue = entry.output.joinToString("") { decode(it, reversed) }
            total += outputValue.toInt()
        }

        return total
    }

    private fun decode(digit: String, solution: Map<Char, Char>): String {
        val number = digit.map { solution[it]!! }.toSet()
        return numbers.indexOf(number).toString()
    }

    data class Entry(
        val input: List<String>,
        val output: List<String>
    ) {
        override fun toString(): String = "${input.joinToString(" ")} | ${output.joinToString(" ")}"

        companion object {
            fun parse(line: String): Entry {
                val (input, output) = line.split("|")
                return Entry(
                    input = input.trim().split(" "),
                    output = output.trim().split(" "),
                )
            }
        }
    }
}