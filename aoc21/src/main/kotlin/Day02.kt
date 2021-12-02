
fun main(args: Array<String>) = solve(Day02())

class Day02: Day(2) {
    override fun task1(input: Input): Any {
        var depth = 0
        var position = 0
        input.capture("(\\w+) (\\d+)").forEach {
            val (direction, distance) = it
            when (direction) {
                "forward" -> position += distance.toInt()
                "up" -> depth -= distance.toInt()
                "down" -> depth += distance.toInt()
            }
        }
        return depth * position
    }

    override fun task2(input: Input): Any {
        var depth = 0
        var position = 0
        var aim = 0
        input.capture("(\\w+) (\\d+)").forEach {
            val (direction, distance) = it
            when (direction) {
                "forward" -> {
                    position += distance.toInt()
                    depth += distance.toInt() * aim
                }
                "up" -> aim -= distance.toInt()
                "down" -> aim += distance.toInt()
            }
        }
        return depth * position
    }
}