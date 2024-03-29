
fun main(args: Array<String>) = solve(Day01())

class Day01: Day(1) {
    override fun task1(input: Input): Any {
        return input.ints()
            .windowed(2)
            .count { it[0] < it[1] }
    }

    override fun task2(input: Input): Any {
        return input.ints()
            .windowed(3)
            .map { it.sum() }
            .windowed(2)
            .count { it[0] < it[1] }
    }
}