import java.lang.RuntimeException
import kotlin.math.absoluteValue

class Day15 : Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day15())
    }

    override fun task1(input: Input): Any {
        val sensors = input.capture("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
            .map { (sx, sy, bx, by) ->
                Sensor(Point2D(sx.toInt(), sy.toInt()), Point2D(bx.toInt(), by.toInt()))
            }
            .toList()

        val testRow = 2000000
        val cols = mutableSetOf<Int>().also { cols ->
            sensors.forEach { cols.addAll(it.reachesColumns(onRow = testRow)) }
        }

        sensors
            .filter { it.closestBeacon.y == testRow }
            .forEach {
                cols.remove(it.closestBeacon.x)
            }
        return cols.size
    }

    override fun task2(input: Input): Any {
        val sensors = input.capture("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
            .map { (sx, sy, bx, by) ->
                Sensor(Point2D(sx.toInt(), sy.toInt()), Point2D(bx.toInt(), by.toInt()))
            }
            .toList()

        val candidates = mutableListOf<Point2D>()
        for (sensor in sensors) {
            for (point in sensor.justOutsideReach()) {
                if (sensors.none { it.reaches(point) }) {
                    candidates.add(point)
                    return point.x.toLong() * 4000000 + point.y.toLong()
                }
            }
        }
        throw RuntimeException()
    }

    data class Sensor(
        val location: Point2D,
        val closestBeacon: Point2D
    ) {
        val reach: Int = location.manhattanDistance(closestBeacon)

        fun reachesColumns(onRow: Int): IntRange {
            val verticalDistance = (location.y - onRow).absoluteValue
            val colReach = reach - verticalDistance
            return (location.x - colReach)..(location.x + colReach)
        }

        fun reaches(point: Point2D): Boolean {
            return location.manhattanDistance(point) <= reach
        }

        fun justOutsideReach(): Set<Point2D> {
            val result = mutableSetOf<Point2D>()
            val justOutsideReach = reach + 1
            val top = location.offset(dy = -justOutsideReach)
            val bottom = location.offset(dy = justOutsideReach)
            val left = location.offset(dx = -justOutsideReach)
            val right = location.offset(dx = justOutsideReach)
            result.addAll(top.walk(left))
            result.addAll(left.walk(bottom))
            result.addAll(bottom.walk(right))
            result.addAll(right.walk(top))
            result.removeIf {
                it.x < 0 || it.x > 4000000 || it.y < 0 || it.y > 4000000
            }
            return result
        }
    }
}
