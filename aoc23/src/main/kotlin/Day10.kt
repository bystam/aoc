class Day10 : Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day10())
    }

    override fun task1(input: Input): Any {
        val map = Map(
            grid = Grid2D.charMatrix(input.lines)
        )
        val route = map.walkOneLap()
        return route.size / 2
    }

    override fun task2(input: Input): Any {
        val map = Map(
            grid = Grid2D.charMatrix(input.lines)
        )
        return countEnclosing(map)
    }

    private fun countEnclosing(map: Map): Int {
        val pipePoints = map.walkOneLap().toSet()
        var total = 0
        for (y in (0..<map.grid.height)) {
            var inside = false
            var flippingChar: Char? = null
            for (x in (0..<map.grid.width)) {
                val point = Point2D(x, y)
                if (pipePoints.contains(point)) {
                    when (map.grid[point]) {
                        '|' -> inside = !inside
                        flippingChar -> inside = !inside
                        'F' -> flippingChar = 'J'
                        'L' -> flippingChar = '7'
                    }
                } else if (inside) {
                    total += 1
                }
            }
        }
        return total
    }

    class Map(
        val grid: Grid2D<Char>
    ) {

        fun walkOneLap(): List<Point2D> {
            val start = grid.allPoints().first { grid[it] == 'S' }
            val startDirection = when {
                grid[start.offset(dx = -1)] in listOf('-', 'F', 'L') -> Vec2D.west
                grid[start.offset(dx = 1)] in listOf('-', 'J', '7') -> Vec2D.east
                grid[start.offset(dy = -1)] in listOf('|', '7', 'F') -> Vec2D.north
                grid[start.offset(dy = 1)] in listOf('-', 'J', 'L') -> Vec2D.south
                else -> TODO("WTF")
            }

            val result = mutableListOf(start)
            var current = start
            var currentDirection = startDirection
            while (true) {
                result += current
                current = current.offset(currentDirection)
                currentDirection = when (grid[current]) {
                    'L' -> when (currentDirection) {
                        Vec2D.south -> Vec2D.east
                        Vec2D.west -> Vec2D.north
                        else -> TODO("WTF")
                    }

                    'J' -> when (currentDirection) {
                        Vec2D.east -> Vec2D.north
                        Vec2D.south -> Vec2D.west
                        else -> TODO("WTF")
                    }

                    '7' -> when (currentDirection) {
                        Vec2D.east -> Vec2D.south
                        Vec2D.north -> Vec2D.west
                        else -> TODO("WTF")
                    }

                    'F' -> when (currentDirection) {
                        Vec2D.north -> Vec2D.east
                        Vec2D.west -> Vec2D.south
                        else -> TODO("WTF")
                    }

                    '-' -> currentDirection.also { assert(it.isHorizontal) }
                    '|' -> currentDirection.also { assert(it.isVertical) }
                    'S' -> break
                    else -> TODO("WTF")
                }
            }
            return result
        }

    }
}
