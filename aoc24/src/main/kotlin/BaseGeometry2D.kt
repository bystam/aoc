import kotlin.math.absoluteValue

data class Point2D(
    val x: Int,
    val y: Int,
) {
    fun offset(dx: Int = 0, dy: Int = 0): Point2D = Point2D(x + dx, y + dy)
    fun offset(v: Vec2D): Point2D = offset(v.dx, v.dy)
    fun distance(to: Point2D): Vec2D = Vec2D(to.x - x, to.y - y)
    fun manhattanDistance(to: Point2D): Int = distance(to).manhattanLength

    operator fun plus(dir: Vec2D): Point2D = offset(dir)

    fun walk(toExcluding: Point2D): List<Point2D> {
        val result = mutableListOf<Point2D>()
        val direction = this.distance(toExcluding).toUnitVector()
        var point = this
        while (point != toExcluding) {
            result += point
            point = point.offset(direction)
        }
        return result
    }

    override fun toString(): String = "($x,$y)"

    companion object {
        val origin: Point2D = Point2D(0, 0)
    }
}

data class BoundingRect2D(
    val min: Point2D,
    val max: Point2D
) {

    operator fun contains(point: Point2D): Boolean {
        return min.x <= point.x && min.y <= point.y &&
                max.x >= point.x && max.y >= point.y;
    }

    companion object {
        fun enclosing(points: Iterable<Point2D>): BoundingRect2D {
            val minX = points.minOf { it.x }
            val minY = points.minOf { it.y }
            val maxX = points.maxOf { it.x }
            val maxY = points.maxOf { it.y }
            return BoundingRect2D(
                min = Point2D(minX, minY),
                max = Point2D(maxX, maxY),
            )
        }
    }
}

data class Vec2D(
    val dx: Int = 0,
    val dy: Int = 0
) {

    val isOrthogonal: Boolean get() = dx == 0 || dy == 0
    val isHorizontal: Boolean get() = dy == 0
    val isVertical: Boolean get() = dx == 0

    val manhattanLength: Int = dx.absoluteValue + dy.absoluteValue

    fun toUnitVector(): Vec2D {
        assert(isOrthogonal)
        return Vec2D(
            dx = if (dx == 0) 0 else dx / dx.absoluteValue,
            dy = if (dy == 0) 0 else dy / dy.absoluteValue,
        )
    }

    fun rotated(rotation: Rotation90): Vec2D = when (rotation) {
        Rotation90.CLOCKWISE -> Vec2D(dx = -dy, dy = dx)
        Rotation90.COUNTERCLOCKWISE -> Vec2D(dx = dy, dy = -dx)
    }

    operator fun times(factor: Int): Vec2D = Vec2D(factor * dx, factor * dy)
    operator fun unaryMinus(): Vec2D = Vec2D(-dx, -dy)

    companion object {
        val north = Vec2D(0, -1)
        val south = Vec2D(0, 1)
        val east = Vec2D(1, 0)
        val west = Vec2D(-1, 0)
        val northWest = north + west
        val northEast = north + east
        val southWest = south + west
        val southEast = south + east

        val allOrthogonal: List<Vec2D> = listOf(north, east, south, west)
        val allDiagonal: List<Vec2D> = listOf(northWest, northEast, southWest, southEast)
        val all: List<Vec2D> = allOrthogonal + allDiagonal
    }

    operator fun plus(other: Vec2D): Vec2D = Vec2D(dx + other.dx, dy + other.dy)
}

enum class Rotation90 {
    CLOCKWISE, COUNTERCLOCKWISE
}

class Grid2D<T>(
    rows: List<List<T>>
) {

    private val data: MutableList<MutableList<T>> = rows.map { it.toMutableList() }.toMutableList()

    val width: Int = rows[0].size
    val height: Int = rows.size
    val upperLeft: Point2D get() = Point2D.origin
    val lowerRight: Point2D get() = Point2D(x = width - 1, y = height - 1)

    val rows: List<List<T>> get() = data

    operator fun get(x: Int, y: Int): T = data[y][x]
    operator fun get(p: Point2D): T = get(p.x, p.y)
    operator fun set(p: Point2D, value: T) { data[p.y][p.x] = value }

    fun getSafe(p: Point2D): T? {
        return if (contains(p)) get(p.x, p.y) else null
    }

    operator fun contains(point: Point2D): Boolean {
        return point.x in 0 until width && point.y in 0 until height
    }

    fun allPoints(): Sequence<Point2D> = (0 until height).asSequence()
        .flatMap { y ->
            (0 until width).map { x -> Point2D(x, y) }
        }

    fun neighbors(p: Point2D): List<Square<T>> = listOf(
        p.offset(dy = -1),
        p.offset(dx = -1),
        p.offset(dx = 1),
        p.offset(dy = 1),
    )
        .filter { it.x in 0 until width && it.y in 0 until height }
        .map { Square(get(it), it) }

    fun neighborsIncludingDiagonal(p: Point2D): List<Square<T>> = listOf(
        p.offset(dy = -1),
        p.offset(dx = -1),
        p.offset(dx = 1),
        p.offset(dy = 1),
        p.offset(dx = -1, dy = -1),
        p.offset(dx = -1, dy = 1),
        p.offset(dx = 1, dy = -1),
        p.offset(dx = 1, dy = 1),
    )
        .filter { it.x in 0 until width && it.y in 0 until height }
        .map { Square(get(it), it) }

    fun walk(start: Point2D, dir: Vec2D): Sequence<Square<T>> = walk(start, dir.dx, dir.dy)

    fun walk(start: Point2D, dx: Int, dy: Int): Sequence<Square<T>> {
        var p: Point2D? = start
        fun next(): Point2D? {
            p = p?.offset(dx, dy)?.takeIf { contains(it) }
            return p
        }
        return generateSequence(next()) { next() }
            .map { Square(get(it), it) }
    }

    fun transposed(): Grid2D<T> {
        val numRows = this.width
        val numCols = this.height
        val rows = mutableListOf<List<T>>()
        repeat(numRows) { r ->
            val row = mutableListOf<T>()
            repeat(numCols) { c ->
                row += this.get(x = r, y = c)
            }
            rows += row
        }
        assert(rows.size == this.rows.first().size)
        assert(rows.first().size == this.rows.size)
        return Grid2D(rows)
    }

    fun stringify(transform: (value: T, point: Point2D, ) -> Char): String {
        val builder = StringBuilder()
        rows.forEachIndexed { y, row ->
            row.forEachIndexed { x, value ->
                builder.append(transform(value, Point2D(x, y)))
            }
            builder.append('\n')
        }
        return builder.toString()
    }

    data class Square<T>(
        val value: T,
        val point: Point2D
    )

    companion object {
        fun charMatrix(lines: Sequence<String>): Grid2D<Char> {
            val rows = lines.map { line -> line.map { it } }.toList()
            return Grid2D(rows)
        }
    }
}

fun Grid2D<Char>.gridString(): String = rows.joinToString("\n") {
    it.joinToString("")
}
