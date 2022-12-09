
class Day9: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day9())
    }

    override val testInput: String? = """
        R 4
        U 4
        L 3
        D 1
        R 4
        D 1
        L 5
        R 2
    """.trimIndent()

    override fun task1(input: Input): Any {
        var head = Point2D.origin
        var tail = Point2D.origin
        val visited = mutableSetOf(tail)
        val moves = input.capture("([A-Z]) (\\d+)")
            .map { (dir, steps) ->
                Move(Direction.valueOf(dir), steps.toInt())
            }

        for (move in moves) {
            val vec = move.direction.value
            repeat(move.steps) {
                head = head.offset(vec)
                tail = tail.pointChasing(head)
                visited.add(tail)
            }
        }

        return visited.size
    }

    override fun task2(input: Input): Any {
        val knots = (0..9).map { Point2D.origin }.toMutableList()
        val visited = mutableSetOf(knots.last())
        val moves = input.capture("([A-Z]) (\\d+)")
            .map { (dir, steps) ->
                Move(Direction.valueOf(dir), steps.toInt())
            }

        for (move in moves) {
            val vec = move.direction.value
            repeat(move.steps) {
                knots[0] = knots[0].offset(vec)
                knots.indices.drop(1).forEach {
                    knots[it] = knots[it].pointChasing(knots[it - 1])
                }
                visited.add(knots.last())
            }
        }

        return visited.size
    }

    private fun Point2D.pointChasing(head: Point2D): Point2D {
        if (head == this) return this
        val tailNeighbors = Vec2D.all.map { this.offset(it) }.toSet()
        if (tailNeighbors.contains(head)) return this // still close enough
        val simpleHeadNeighbors = Vec2D.allOrthogonal.map { head.offset(it) }
        simpleHeadNeighbors.intersect(tailNeighbors).firstOrNull()?.let {
            return it
        }
        val headNeighbors = Vec2D.all.map { head.offset(it) }
        return headNeighbors.intersect(tailNeighbors).single()
    }

    data class Move(
        val direction: Direction,
        val steps: Int
    )

    enum class Direction(val value: Vec2D) {
        R(Vec2D.east),
        L(Vec2D.west),
        U(Vec2D.north),
        D(Vec2D.south);
    }
}
