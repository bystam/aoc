
fun main(args: Array<String>) = solve(Day3())

class Day3: Day(3) {
    override fun task1(input: Input): Any {
        return input.lines
            .map { it.substring(0, it.length / 2) to it.substring(it.length / 2) }
            .map { (c1, c2) -> c1.toSet().intersect(c2.toSet()).single() }
            .sumOf { it.priority }
    }

    override fun task2(input: Input): Any {
        return input.lines
            .chunked(3)
            .map { (r1, r2, r3) ->
                r1.toSet().intersect(r2.toSet()).intersect(r3.toSet()).single()
            }
            .sumOf { it.priority }
    }

    private val Char.priority: Int get() = when {
        ('a'..'z').contains(this) -> this - 'a' + 1
        ('A'..'Z').contains(this) -> this - 'A' + 27
        else -> TODO()
    }
}
