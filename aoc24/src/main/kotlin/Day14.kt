
object Day14 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day14)

    override fun task1(input: Input): Any {
        val robots = input.lines.map { Robot.from(it) }.toList()
        repeat(100) {
            robots.forEach { it.step() }
        }

        // 101 0..49 50 51..101
        // 103 0..50 51 52..101
        var (a, b, c, d) = listOf(0, 0, 0, 0)
        for (robot in robots) {
            if (robot.position.x <= 49 && robot.position.y <= 50) {
                a += 1
            } else if (robot.position.x >= 51 && robot.position.y <= 50) {
                b += 1
            } else if (robot.position.x <= 49 && robot.position.y >= 52) {
                c += 1
            } else if (robot.position.x >= 51 && robot.position.y >= 52) {
                d += 1
            }
        }
        return a * b * c * d
    }

    override fun task2(input: Input): Any {
        val robots = input.lines.map { Robot.from(it) }.toList()

        repeat(100000) { seconds ->
            val robotPoints = robots.map { it.position }.toSet()
            if (maybeChristmasTree(robotPoints)) {
                println("Seconds: $seconds")
                println(draw(robotPoints))
                println("-----------------")
            }
            robots.forEach { it.step() }
        }
        return "TODO"
    }

    private fun maybeChristmasTree(robotPoints: Set<Point2D>): Boolean {
        // detect a pretty significant shape like
        //    X
        //   X X
        //  X   X
        // X     X
        return robotPoints.any {
            robotPoints.containsAll(
                listOf(
                    it.offset(dx = -1, dy = 1),
                    it.offset(dx = -2, dy = 2),
                    it.offset(dx = -3, dy = 3),
                    it.offset(dx = 1, dy = 1),
                    it.offset(dx = 2, dy = 2),
                    it.offset(dx = 3, dy = 3),
                )
            )
        }
    }

    private fun draw(robotPoints: Set<Point2D>): String {
        val string = StringBuilder()
        for (y in 0 until 103) {
            for (x in 0 until 101) {
                val point = Point2D(x, y)
                if (point in robotPoints) {
                    string.append("X")
                } else {
                    string.append(".")
                }
            }
            string.append("\n")
        }
        return string.toString()
    }

    data class Robot(
        var position: Point2D,
        val velocity: Vec2D,
    ) {
        fun step() {
            position = Point2D(
                x = (position.x + velocity.dx + 101) % 101,
                y = (position.y + velocity.dy + 103) % 103,
            )
        }

        companion object {
            fun from(string: String): Robot {
                val (p, v) = string.split(" ")
                val (x, y) = p.removePrefix("p=").split(",").map { it.toInt() }
                val (dx, dy) = v.removePrefix("v=").split(",").map { it.toInt() }
                return Robot(
                    position = Point2D(x, y),
                    velocity = Vec2D(dx, dy)
                )
            }
        }
    }
}
