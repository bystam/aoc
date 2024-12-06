
object Day06: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day06)

    override fun task1(input: Input): Any {
        val map = Grid2D.charMatrix(input.lines)
        var guard = map.allPoints().first { map[it] == '^' }
        var guardDirection = Vec2D.north
        map[guard] = '.'

        val visited = mutableSetOf<Point2D>()
        while (map.contains(guard)) {
            visited += guard

            while (map.getSafe(guard.offset(guardDirection)) == '#') {
                guardDirection = guardDirection.rotated(Rotation90.CLOCKWISE)
            }
            guard = guard.offset(guardDirection)
        }
        return visited.size
    }

    override fun task2(input: Input): Any {
        val map = Grid2D.charMatrix(input.lines)
        val guardStart = map.allPoints().first { map[it] == '^' }
        val existingObstacles = map.allPoints().filter { map[it] == '#' }.toSet()
        map[guardStart] = '.'

        var count = 0
        val possiblePlaces = map.allPoints().minus(guardStart).minus(existingObstacles)
        for (possible in possiblePlaces) {
            map[possible] = '#'
            var guard = guardStart
            var guardDirection = Vec2D.north
            val visited = mutableSetOf<Pair<Point2D, Vec2D>>()
            while (map.contains(guard)) {
                val current = (guard to guardDirection)
                if (current in visited) {
                    count += 1
                    break
                }

                visited += current

                while (map.getSafe(guard.offset(guardDirection)) == '#') {
                    guardDirection = guardDirection.rotated(Rotation90.CLOCKWISE)
                }
                guard = guard.offset(guardDirection)
            }
            map[possible] = '.'
        }

        return count
    }
}
