object Day13: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day13)
    
    override fun task1(input: Input): Any {
        val clawMachines = input.text().split("\n\n").map { ClawMachine.from(it) }

        var total = 0
        for (clawMachine in clawMachines) {
            cheapest(clawMachine)?.let { (a, b) ->
                total += a * 3 + b
            }
        }

        return total
    }

    override fun task2(input: Input): Any {
        // X = A*dx + B*dx
        // Y = A*dy + B*dy
        val equationSystems = input.text().split("\n\n").map { ClawMachine.from(it) }
            .map { EquationSystem.from(it) }
        var total = 0L
        for (equationSystem in equationSystems) {
            equationSystem.solve()?.let { (a, b) ->
                total += a * 3 + b
            }
        }
        return total
    }

    private fun cheapest(clawMachine: ClawMachine): Pair<Int, Int>? {
        val prize = clawMachine.prize
        var point = Point2D.origin
        var b = 0
        var a = 0
        while (point.x < prize.x && point.y < prize.y && b < 100) {
            b += 1
            point = point.offset(clawMachine.b)
        }

        while (b >= 0) {
            point = point.offset(-clawMachine.b)
            b -= 1
            a = 0
            var chase = point
            while (chase.x < prize.x && chase.y < prize.y && a < 100) {
                a += 1
                chase = chase.offset(clawMachine.a)
            }
            if (chase == prize) {
                return a to b
            }
        }

        return null
    }

    data class ClawMachine(
        val a: Vec2D,
        val b: Vec2D,
        val prize: Point2D,
    ) {

        companion object {
            fun from(string: String): ClawMachine {
                val (a, b, prize) = string.split("\n")

                fun parseButton(string: String): Vec2D {
                    val (x, y) = string.split(", ")
                    return Vec2D(x.removePrefix("X+").toInt(), y.removePrefix("Y+").toInt())
                }

                fun parsePrize(string: String): Point2D {
                    val (x, y) = string.split(", ")
                    return Point2D(x.removePrefix("X=").toInt(), y.removePrefix("Y=").toInt())
                }

                return ClawMachine(
                    a = parseButton(a.removePrefix("Button A: ")),
                    b = parseButton(b.removePrefix("Button B: ")),
                    prize = parsePrize(prize.removePrefix("Prize: ")),
                )
            }
        }
    }


    data class EquationSystem(
        val x: Long,
        val y: Long,
        val ax: Long,
        val ay: Long,
        val bx: Long,
        val by: Long,
    ) {

        fun solve(): Pair<Long, Long>? {
            val aTop = (by * x - bx * y)
            val aBottom = (by * ax - bx * ay)
            if (aTop % aBottom != 0L) {
                return null
            }
            val a = aTop / aBottom

            val bTop = y - ay * a
            val bBottom = by
            if (bTop % bBottom != 0L) {
                return null
            }
            val b = bTop / bBottom
            return a to b
        }

        companion object {
            fun from(machine: ClawMachine): EquationSystem = EquationSystem(
                x = machine.prize.x + 10000000000000L,
                y = machine.prize.y + 10000000000000L,
                ax = machine.a.dx.toLong(),
                ay = machine.a.dy.toLong(),
                bx = machine.b.dx.toLong(),
                by = machine.b.dy.toLong(),
            )
        }
    }
}
