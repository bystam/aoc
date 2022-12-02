import java.lang.RuntimeException

fun main(args: Array<String>) = solve(Day2())

class Day2: Day(2) {
    override fun task1(input: Input): Any {
        var score = 0
        for (line in input.lines) {
            val (opponent, me) = line.split(" ").map { Hand.from(it) }
            val winPoints = if (me.beats(opponent))
                6
            else if (opponent.beats(me))
                0
            else 3
            score += (winPoints + me.points)
         }
        return score
    }

    override fun task2(input: Input): Any {
        var score = 0
        for (line in input.lines) {
            val (opponentString, resultString) = line.split(" ")
            val opponent = Hand.from(opponentString)
            val result = Result.values().first { it.string == resultString }
            val me = result.play(opponent)
            score += (result.score + me.points)
        }
        return score
    }

    enum class Hand(val points: Int) {
        ROCK(1), PAPER(2), SCISSOR(3);

        val beating: Hand get() = Hand.values()[(ordinal + 1) % 3]
        val losing: Hand get() = Hand.values()[(ordinal + 2) % 3]

        fun beats(opponent: Hand): Boolean = when (this) {
            ROCK -> opponent == SCISSOR
            PAPER -> opponent == ROCK
            SCISSOR -> opponent == PAPER
        }

        companion object {
            fun from(string: String): Hand = when(string) {
                "A", "X" -> ROCK
                "B", "Y" -> PAPER
                "C", "Z" -> SCISSOR
                else -> throw RuntimeException()
            }
        }
    }

    enum class Result(val string: String, val score: Int) {
        LOSE("X", 0), DRAW("Y", 3), WIN("Z", 6);

        fun play(opponent: Hand): Hand = when (this) {
            LOSE -> opponent.losing
            DRAW -> opponent
            WIN -> opponent.beating
        }
    }
}
