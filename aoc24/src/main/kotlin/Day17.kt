
object Day17 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day17)

    override fun task1(input: Input): Any {
        val computer = Computer.from(input.text())
        return computer.runUntilHalt().joinToString(",")
    }

    override fun task2(input: Input): Any {
        val computer = Computer.from(input.text())
        return computer.figureOutProgram()
    }

    class Computer(
        val aStart: Long,
        val bStart: Long,
        val cStart: Long,
        val program: List<Long>,
    ) {
        private var a: Long = aStart
        private var b: Long = bStart
        private var c: Long = cStart
        private var pc: Int = 0

        fun runUntilHalt(): List<Long> {
            val output = mutableListOf<Long>()
            while (pc <= program.lastIndex) {
                val op = program[pc]
                when (op) {
                    0L -> a = a / 2.pow(combo()) // adv
                    1L -> b = b xor literal() // bxl
                    2L -> b = combo() % 8  // bst
                    3L -> {  // jnz
                        if (a != 0L) {
                            pc = literal().toInt()
                            continue
                        }
                    }

                    4L -> b = b xor c  // bxc
                    5L -> output.add(combo() % 8) // out
                    6L -> b = a / 2.pow(combo()) // bdv
                    7L -> c = a / 2.pow(combo()) // cdv
                }
                pc += 2
            }
            return output
        }

        fun figureOutProgram(): Long {
            var possibleResults = listOf<Long>()
            for (i in (1 .. program.size)) {
                val expected = program.takeLast(i)

                possibleResults = when {
                    possibleResults.isEmpty() -> (0L..7).filter { test ->
                        reset()
                        a = test
                        runUntilHalt().takeLast(i) == expected
                    }
                    else -> possibleResults.flatMap { result ->
                        (0L..7).filter { test ->
                            reset()
                            a = result + test
                            runUntilHalt().takeLast(i) == expected
                        }.map { result + it }
                    }
                }

                possibleResults = possibleResults.map { it * 8 }
            }
            return possibleResults.min() / 8
        }

        private fun reset() {
            pc = 0
            a = aStart
            b = bStart
            c = cStart
        }

        private fun combo(): Long = when (val operand = program[pc + 1]) {
            0L, 1L, 2L, 3L -> operand
            4L -> a
            5L -> b
            6L -> c
            else -> TODO()
        }

        private fun literal(): Long = program[pc + 1]

        private fun Int.pow(exponent: Long): Long {
            var result = 1L
            for (i in (0 until exponent)) {
                result *= this
            }
            return result
        }

        companion object {
            fun from(string: String): Computer {
                val lines = string.lines()
                return Computer(
                    aStart = lines[0].removePrefix("Register A: ").toLong(),
                    bStart = lines[1].removePrefix("Register B: ").toLong(),
                    cStart = lines[2].removePrefix("Register C: ").toLong(),
                    program = lines[4].removePrefix("Program: ").split(",").map { it.toLong() }
                )
            }
        }
    }
}
