
object Day15: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day15)

    override fun task1(input: Input): Any {
        val (mapString, walkString) = input.text().split("\n\n")
        val map = Grid2D.charMatrix(mapString.lines().asSequence())
        val walk = parseWalk(walkString)

        val state = State.from(map)
        for (direction in walk) {
            state.step(direction)
        }

        return state.boxes.sumOf {
            it.x + it.y * 100
        }
    }

    override fun task2(input: Input): Any {
        val (mapString, walkString) = input.text().split("\n\n")
        val map = Grid2D.charMatrix(mapString.lines().asSequence())
        val walk = parseWalk(walkString)

        val state = BigState.from(State.from(map))
        for (direction in walk) {
            state.step(direction)
        }

        return state.boxes.sumOf {
            it.left.x + it.left.y * 100
        }
    }

    private fun parseWalk(string: String): List<Vec2D> {
        return string.trim().mapNotNull {
            when (it) {
                '>' -> Vec2D.east
                '<' -> Vec2D.west
                '^' -> Vec2D.north
                'v' -> Vec2D.south
                else -> null
            }
        }
    }

    class State(
        val map: Grid2D<Char>,
        var robot: Point2D,
        val boxes: MutableSet<Point2D>,
    ) {

        fun step(direction: Vec2D) {
            var nextInPushDirection = robot
            val boxesToPush = generateSequence {
                nextInPushDirection = nextInPushDirection.offset(direction)
                nextInPushDirection.takeIf { it in boxes }
            }.toSet()
            if (map[nextInPushDirection] == '#') {
                return
            }

            boxes.removeAll(boxesToPush)
            boxes.addAll(boxesToPush.map { it.offset(direction) })
            robot = robot.offset(direction)
        }

        companion object {
            fun from(map: Grid2D<Char>): State {
                lateinit var robot: Point2D
                val boxes = mutableSetOf<Point2D>()
                for (point in map.allPoints()) {
                    when (map[point]) {
                        'O' -> boxes.add(point)
                        '@' -> robot = point
                        else -> continue
                    }
                    map[point] = '.'
                }
                return State(map, robot, boxes)
            }
        }
    }

    class BigState(
        val map: Grid2D<Char>,
        var robot: Point2D,
        val boxes: List<BigBox>,
    ) {

        fun step(direction: Vec2D) {
            var nextPushPoints = listOf(robot)
            val boxesToPush = generateSequence {
                nextPushPoints = nextPushPoints.map { it.offset(direction) }

                if (nextPushPoints.any { map[it] == '#' }) {
                    return@generateSequence null
                }

                val boxHits = boxes.filter { box -> nextPushPoints.any { box.contains(it) } }

                if (boxHits.isNotEmpty()) {
                    if (direction.isHorizontal) {
                        nextPushPoints = nextPushPoints.map { it.offset(direction) }
                    } else {
                        nextPushPoints = boxHits.flatMap { listOf(it.left, it.right) }
                    }
                }

                boxHits.takeIf { it.isNotEmpty() }
            }.flatten().toList()
            if (nextPushPoints.any { map[it] == '#' }) {
                return
            }

            for (box in boxesToPush) {
                box.left = box.left.offset(direction)
            }
            robot = robot.offset(direction)
        }

        companion object {
            fun from(smallState: State): BigState {
                val boxes = smallState.boxes.map { BigBox(Point2D(it.x * 2, it.y)) }
                val widenedRows = smallState.map.rows.map { row ->
                    row.flatMap { listOf(it, it) }
                }
                return BigState(
                    map = Grid2D(widenedRows),
                    robot = Point2D(smallState.robot.x * 2, smallState.robot.y),
                    boxes = boxes,
                )
            }
        }
    }

    data class BigBox(
        var left: Point2D,
    ) {
        val right: Point2D get() = left.offset(Vec2D.east)

        fun contains(point: Point2D): Boolean {
            return left == point || right == point
        }
    }

    private fun BigState.stringify(): String {
        return map.stringify { value, point ->
            when {
                point == robot -> '@'
                boxes.any { it.left == point } -> '['
                boxes.any { it.right == point } -> ']'
                else -> value
            }
        }
    }
}
