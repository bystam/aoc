import kotlin.math.absoluteValue

object Day21 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day21)

    override val testInput: String
        get() = """
            029A
            980A
            179A
            456A
            379A
        """.trimIndent()

    override val skipReal: Boolean
        get() = true

    override fun task1(input: Input): Any {
        return input.lines.sumOf {
            val moves = generateMyMoves(it)
            moves.size * it.substring(0, 3).toInt()
        }
    }

    private fun generateMyMoves(keyCode: String): List<Move> {
        val keyPoints = keyCode.map { numpadPointOf(it) }

        val robot1 = RobotAtNumpad()
        val robot2 = RobotAtKeyboard()
        val robot3 = RobotAtKeyboard()

        val movesAfter1 = keyPoints.flatMap {
            robot1.moveTo(it)
        }
        val movesAfter2 = movesAfter1.split(Move.A).flatMap {
            robot2.smartHandle(it + Move.A)
        }
        val movesAfter3 = movesAfter2.split(Move.A).flatMap {
            robot3.smartHandle(it + Move.A)
        }
        println("""
            keyCode: $keyCode
            movesAfter1: ${movesAfter1.joinToString("") { it.char.toString() }}
            movesAfter2: ${movesAfter2.joinToString("") { it.char.toString() }}
            movesAfter3: ${movesAfter3.joinToString("") { it.char.toString() }}
        """.trimIndent())
        return movesAfter3
    }

    override fun task2(input: Input): Any {
        return "TODO"
    }

    abstract class Robot {
        abstract var arm: Point2D

        fun moveTo(point: Point2D): List<Move> {
            val moves = mutableListOf<Move>()
            val distance = arm.distance(point)
            repeat(distance.dx.absoluteValue) {
                moves += if (distance.dx < 0) Move.LEFT else Move.RIGHT
            }
            repeat(distance.dy.absoluteValue) {
                moves += if (distance.dy < 0) Move.UP else Move.DOWN
            }
            moves += Move.A

            for (move in moves) {
                arm += move.direction
            }

            return moves
        }
    }

    class RobotAtNumpad : Robot() {
        override var arm: Point2D = numpadPointOf('A')
    }

    class RobotAtKeyboard : Robot() {
        override var arm: Point2D = Move.A.position

        fun smartHandle(moves: List<Move>): List<Move> {
            val moveByType = moves.groupBy { it }.mapValues { it.value.size }
            val moveOrder = moveByType.keys.minus(Move.A).sortedBy { a ->
                a.position.manhattanDistance(arm)
            }

            val result = mutableListOf<Move>()
            for (move in moveOrder) {
                repeat(moveByType[move]!!) {
                    result += moveTo(move.position)
                }
            }
            result += moveTo(Move.A.position)
            return result
        }
    }

    enum class Move(val position: Point2D, val direction: Vec2D, val char: Char) {
        LEFT(Point2D(0, 1), Vec2D.west, '<'),
        RIGHT(Point2D(2, 1), Vec2D.east, '>'),
        UP(Point2D(1, 0), Vec2D.north, '^'),
        DOWN(Point2D(1, 1), Vec2D.south, 'v'),
        A(Point2D(2, 0), Vec2D(0, 0), 'A');
    }

    private fun numpadPointOf(char: Char): Point2D = when (char) {
        '0' -> Point2D(1, 3)
        '1' -> Point2D(0, 2)
        '2' -> Point2D(1, 2)
        '3' -> Point2D(2, 2)
        '4' -> Point2D(0, 1)
        '5' -> Point2D(1, 1)
        '6' -> Point2D(2, 2)
        '7' -> Point2D(0, 3)
        '8' -> Point2D(1, 0)
        '9' -> Point2D(2, 0)
        'A' -> Point2D(2, 3)
        else -> TODO()
    }

    private fun <T> List<T>.split(separator: T): List<List<T>> {
        val result = mutableListOf<MutableList<T>>()
        var current = mutableListOf<T>()
        for (element in this) {
            if (element == separator) {
                if (current.isNotEmpty()) {
                    result.add(current)
                    current = mutableListOf()
                }
            } else {
                current += element
            }
        }
        return result
    }
}
