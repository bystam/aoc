import kotlin.math.sign

fun main(args: Array<String>) = solve(Day05())

class Day05: Day(5) {
    override fun task1(input: Input): Any {
        val counts = mutableMapOf<Point, Int>()
        input.lines
            .map { Vent.parse(it) }
            .filter { it.isStraight }
            .flatMap { it.line }
            .forEach { point ->
                counts[point] = counts[point]?.inc() ?: 1
            }
        return counts.values.count { it > 1 }
    }

    override fun task2(input: Input): Any {
        val counts = mutableMapOf<Point, Int>()
        input.lines
            .map { Vent.parse(it) }
            .flatMap { it.line }
            .forEach { point ->
                counts[point] = counts[point]?.inc() ?: 1
            }
        return counts.values.count { it > 1 }
    }

    data class Vent(
        val from: Point,
        val to: Point
    ) {
        val isStraight: Boolean get() = from.x == to.x || from.y == to.y

        val line: Sequence<Point> get() = from.to(to)

        companion object {
            private val regex = Regex("""(\d+),(\d+) -> (\d+),(\d+)""")

            fun parse(line: String): Vent {
                val (x1, y1, x2, y2) = regex.matchEntire(line)!!.destructured
                return Vent(Point(x1.toInt(), y1.toInt()), Point(x2.toInt(), y2.toInt()))
            }
        }
    }

    data class Point(
        val x: Int,
        val y: Int
    ) {
        fun to(other: Point): Sequence<Point> {
            val dx = (other.x - x).sign
            val dy = (other.y - y).sign
            var current = this
            var last = false
            return generateSequence(this) {
                if (last) return@generateSequence null
                val next = Point(current.x + dx, current.y + dy)
                current = next
                last = current == other
                current
            }
        }
    }
}