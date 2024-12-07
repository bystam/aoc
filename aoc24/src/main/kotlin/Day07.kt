
object Day07: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day07)

    override fun task1(input: Input): Any {
        return input.lines
            .map { Equation.from(it) }
            .filter { it.isValid() }
            .sumOf { it.result }
    }

    override fun task2(input: Input): Any {
        return input.lines
            .map { Equation.from(it) }
            .filter { it.isValid2() }
            .sumOf { it.result }
    }

    private fun allPermutations(sequence: List<Long>): List<Long> {
        if (sequence.size == 1) return listOf(sequence.first())
        val operand = sequence.first()
        val rest = sequence.drop(1)

        val recursed = allPermutations(rest)
        return recursed.map { it + operand } + recursed.map { it * operand }
    }

    private fun allPermutations2(sequence: List<Long>): List<Long> {
        if (sequence.size == 1) return listOf(sequence.first())
        val operand = sequence.first()
        val rest = sequence.drop(1)

        val recursed = allPermutations2(rest)
        return recursed.map { it + operand } +
                recursed.map { it * operand } +
                recursed.map { (it.toString() + operand.toString()).toLong() }
    }

    data class Equation(
        val result: Long,
        val operands: List<Long>
    ) {

        fun isValid(): Boolean {
            val allPermutations = allPermutations(operands.reversed())
            return allPermutations.contains(result)
        }

        fun isValid2(): Boolean {
            val allPermutations = allPermutations2(operands.reversed())
            return allPermutations.contains(result)
        }

        companion object {
            fun from(line: String): Equation {
                val (result, operands) = line.split(":")
                return Equation(
                    result = result.toLong(),
                    operands = operands.split(" ").mapNotNull { it.toLongOrNull() }
                )
            }
        }
    }
}
