import java.math.BigInteger

object Day18: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day18)

    override fun task1(input: Input): Any {
        val instructions = input.lines.map(Instruction::fromTask1)
        val corners = instructions.scan(Point2D.origin) { acc, instr ->
            acc.offset(instr.direction * instr.steps)
        }.toList()
        return measureSizeOfPool(corners)
    }

    override fun task2(input: Input): Any {
        val instructions = input.lines.map(Instruction::fromTask2)
        val corners = instructions.scan(Point2D.origin) { acc, instr ->
            acc.offset(instr.direction * instr.steps)
        }.toList()
        return measureSizeOfPool(corners)
    }

    private fun measureSizeOfPool(corners: List<Point2D>): BigInteger {
        var result = BigInteger.ZERO
        var totalEdgeDistance = 0L
        corners
            .windowed(2)
            .forEach { (a, b) ->
                val ax = BigInteger.valueOf(a.x.toLong())
                val bx = BigInteger.valueOf(b.x.toLong())
                val ay = BigInteger.valueOf(a.y.toLong())
                val by = BigInteger.valueOf(b.y.toLong())
                result += ax * by - bx * ay
                totalEdgeDistance += a.manhattanDistance(b).toLong()
            }
        result /= BigInteger.TWO
        result += BigInteger.valueOf(totalEdgeDistance / 2)
        result += BigInteger.ONE
        return result
    }

    data class Instruction(
        val directionChar: Char,
        val steps: Int,
    ) {

        val direction: Vec2D = when (directionChar) {
            'U' -> Vec2D.north
            'D' -> Vec2D.south
            'L' -> Vec2D.west
            'R' -> Vec2D.east
            else -> TODO()
        }

        companion object {
            fun fromTask1(string: String): Instruction {
                val (dir, steps) = string.split(" ")
                return Instruction(
                    directionChar = dir.single(),
                    steps = steps.toInt(),
                )
            }
            fun fromTask2(string: String): Instruction {
                val (_, _, last) = string.split(" ")
                val number = last.drop(2).dropLast(1)
                val directionChar = when (number.last()) {
                    '0' -> 'R'
                    '1' -> 'D'
                    '2' -> 'L'
                    '3' -> 'U'
                    else -> TODO()
                }
                val steps = number.substring(0, 5).toInt(16)
                return Instruction(
                    directionChar = directionChar,
                    steps = steps,
                )
            }
        }
    }
}
