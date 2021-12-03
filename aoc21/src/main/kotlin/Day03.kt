
fun main(args: Array<String>) = solve(Day03())

class Day03: Day(3) {
    override fun task1(input: Input): Any {
        val state = "011110111101".toMutableList()
        state.indices.forEach { index ->
            state[index] = mostCommon(input.lines, index)
        }
        val gamma = state.joinToString("")
        val epsilon = state.flipped().joinToString("")
        return Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2)
    }

    override fun task2(input: Input): Any {
        var currentIndex = 0
        var candidates = input.lines.toMutableList()
        while (candidates.size > 1) {
            val bit = mostCommon(candidates.asSequence(), currentIndex)
            candidates.removeIf { it[currentIndex] != bit }
            currentIndex += 1
        }
        val ogRating = candidates.single()

        currentIndex = 0
        candidates = input.lines.toMutableList()
        while (candidates.size > 1) {
            val bit = mostCommon(candidates.asSequence(), currentIndex).flipped()
            candidates.removeIf { it[currentIndex] != bit }
            currentIndex += 1
        }
        val csRating = candidates.single()

        return Integer.parseInt(ogRating, 2) * Integer.parseInt(csRating, 2)
    }

    private fun mostCommon(input: Sequence<String>, index: Int): Char {
        var state = 0
        input.forEach {
            if (it[index] == '1') state++
            else state--
        }
        return if (state >= 0) '1' else '0'
    }

    private fun MutableList<Char>.flipped() = map { it.flipped() }

    private fun MutableList<Char>.flipAt(index: Int) {
        if (this[index] == '1') this[index] = '0'
        else this[index] = '1'
    }

    private fun Char.flipped() = if (this == '1') '0' else '1'
}