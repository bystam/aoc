
class Day01: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day01())
    }

    override fun task1(input: Input): Any {
        return input.lines
            .map { line ->
                val firstDigit = line.first { it.isDigit() }
                val lastDigit = line.last { it.isDigit() }
                "$firstDigit$lastDigit".toInt()
            }
            .sum()
    }

    override fun task2(input: Input): Any {
        return input.lines
            .map { line ->
                val firstDigit = line.firstDigit()
                val lastDigit = line.lastDigit()
                "$firstDigit$lastDigit".toInt()
            }
            .sum()
    }

    private fun String.firstDigit(): Int = allDigits.minBy { (string, _) ->
        this.indexOf(string).takeUnless { it == -1 } ?: Int.MAX_VALUE
    }.value
    private fun String.lastDigit(): Int = allDigits.maxBy { (string, _) ->
        this.lastIndexOf(string).takeUnless { it == -1 } ?: Int.MIN_VALUE
    }.value

    private val allDigits: Map<String, Int> = mapOf(
        "1" to 1,
        "2" to 2,
        "3" to 3,
        "4" to 4,
        "5" to 5,
        "6" to 6,
        "7" to 7,
        "8" to 8,
        "9" to 9,
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )
}
