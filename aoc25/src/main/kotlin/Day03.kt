
object Day03: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day03)

    override fun task1(input: Input): Any {
        fun maxJoltage(bank: String): Int {
            return (0..<bank.lastIndex).maxOf { i ->
                (i+1..bank.lastIndex).maxOf { j ->
                    "${bank[i]}${bank[j]}"
                }
            }.toInt()
        }

        return input.lines.sumOf { maxJoltage(it) }
    }

    override fun task2(input: Input): Any {
        fun maxJoltage(bank: String, from: Int, to: Int): String {
            if (to > bank.lastIndex) return ""
            val range = bank.substring(from..to)
            val max = range.max()
            val maxIndex = range.indexOf(max) + from

            return "${max}${maxJoltage(bank, maxIndex + 1, to + 1)}"
        }

        return input.lines.sumOf {
            maxJoltage(it, 0, it.length - 12).toBigInteger()
        }
    }
}
