import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.plusAssign
import kotlin.collections.windowed
import kotlin.math.absoluteValue

object Day09 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day09)

    override fun task1(input: Input): Any {
        val points = input.lines.map {
            val (x, y) = it.split(",")
            Point2D(x.toInt(), y.toInt())
        }.toList()

        val pairs = points
            .flatMapIndexed { i, point ->
                points.subList(i + 1, points.size).map {
                    Pair(point, it)
                }
            }

        return pairs.maxOf { (a, b) ->
            val dir = a.distance(b)
            (dir.dx.absoluteValue.toLong() + 1) * (dir.dy.absoluteValue.toLong() + 1)
        }
    }

    override fun task2(input: Input): Any {
        val reds = input.lines.map {
            val (x, y) = it.split(",")
            Point2D(x.toInt(), y.toInt())
        }.toList()

        val pairs = reds
            .flatMapIndexed { i, point ->
                reds.subList(i + 1, reds.size).map {
                    Pair(point, it)
                }
            }


        val outerBoundary = getOuterBoundary(reds)

        fun isLegal(from: Point2D, to: Point2D): Boolean {
            val rect = BoundingRect2D.enclosing(listOf(from, to))
            return outerBoundary.none { it in rect }
        }

        var largest = Long.MIN_VALUE
        for ((a, b) in pairs) {
            val dir = a.distance(b)
            val size = (dir.dx.absoluteValue.toLong() + 1) * (dir.dy.absoluteValue.toLong() + 1)
            if (size > largest && isLegal(a, b)) {
                largest = size
            }
        }
        return largest
    }

    private fun getOuterBoundary(reds: List<Point2D>): Set<Point2D> {
        val left = mutableSetOf<Point2D>()
        val right = mutableSetOf<Point2D>()
        for ((from, to, next) in (reds + reds.take(2)).windowed(3)) {
            val dir = from.distance(to).toUnitVector()
            val rightDir = dir.rotated(Rotation90.CLOCKWISE)
            val leftDir = dir.rotated(Rotation90.COUNTERCLOCKWISE)

            var point = from + dir
            while (point != to) {
                right += point + rightDir
                left += point + leftDir
                point += dir
            }

            val nextDir = to.distance(next).toUnitVector()
            when (nextDir) {
                rightDir -> {
                    // fill left with turn
                    left += to + leftDir // add point to the left of corner
                    left += to + leftDir + dir // add the outer corner
                    left += to + dir // add ahead after turn
                }

                leftDir -> {
                    // fill right with turn
                    // fill left with turn
                    right += to + rightDir // add point to the right of corner
                    right += to + rightDir + dir // add the outer corner
                    right += to + dir // add ahead after turn
                }

                else -> {
                    error("WTF")
                }
            }
        }
        return listOf(left, right).maxBy { it.size }
    }
}
