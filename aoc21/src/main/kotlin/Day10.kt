import java.util.*

fun main(args: Array<String>) = solve(Day10())

class Day10: Day(10) {

    override val testInput: String = """
    [({(<(())[]>[[{[]{<()<>>
    [(()[<>])]({[<{<<[]>>(
    {([(<{}[<>[]}>{[]{[(<()>
    (((({<>}<{<{<>}{[]{[]{}
    [[<[([]))<([[{}[[()]]]
    [{[{({}]{}}([{[{{{}}([]
    {<[[]]>}<{[{[{[]{()[[[]
    [<(<(<(<{}))><([]([]()
    <{([([[(<>()){}]>(<<{{
    <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent()

    override fun task1(input: Input): Any {
        val total = input.lines.sumOf { line ->
            val state = LinkedList<Bracket>()
            for (char in line) {
                Bracket.openLookup[char]?.let { state.addLast(it) }
                Bracket.closeLookup[char]?.let {
                    if (state.last == it) state.removeLast()
                    else return@sumOf it.corruptScore
                }
            }
            return@sumOf 0
        }
        return total
    }

    override fun task2(input: Input): Any {
        val incompletes = input.lines.mapNotNull { line ->
            val state = LinkedList<Bracket>()
            for (char in line) {
                Bracket.openLookup[char]?.let { state.addLast(it) }
                Bracket.closeLookup[char]?.let {
                    if (state.last == it) state.removeLast()
                    else return@mapNotNull null
                }
            }
            return@mapNotNull state
        }
        val scores = incompletes.map { state ->
            state.reversed().fold(0L) { acc, bracket -> 5L * acc + bracket.autocompleteScore }
        }.toList().sorted()
        assert(scores.size % 2 == 1)
        return scores[scores.size / 2]
    }

    enum class Bracket(
        val open: Char,
        val close: Char,
        val corruptScore: Int,
        val autocompleteScore: Int,
    ) {
        ROUND('(', ')', 3, 1),
        SQUARE('[', ']', 57, 2),
        CURLY('{', '}', 1197, 3),
        ANGLE('<', '>', 25137, 4);

        companion object {
            val openLookup = values().associateBy { it.open }
            val closeLookup = values().associateBy { it.close }
        }
    }
}