
class Day6: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day6())
    }

    override fun task1(input: Input): Any {
        val length = 4
        return length + input.text()
            .windowed(length)
            .indexOfFirst { chars ->
                chars.toSet().size == length
            }
    }

    override fun task2(input: Input): Any {
        val length = 14
        return length + input.text()
            .windowed(length)
            .indexOfFirst { chars ->
                chars.toSet().size == length
            }
    }
}
