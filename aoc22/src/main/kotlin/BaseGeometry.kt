data class Point2D(
    val x: Int,
    val y: Int,
) {
    fun offset(dx: Int = 0, dy: Int = 0): Point2D = Point2D(x + dx, y + dy)
    fun offset(v: Vec2D): Point2D = offset(v.dx, v.dy)
    fun distance(to: Point2D): Vec2D = Vec2D(to.x - x, to.y - y)

    override fun toString(): String = "($x,$y)"

    companion object {
        val origin: Point2D = Point2D(0, 0)
    }
}

data class Vec2D(
    val dx: Int,
    val dy: Int
) {
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

class Grid2D<T>(
    rows: List<List<T>>
) {

    private val data: MutableList<MutableList<T>> = rows.map { it.toMutableList() }.toMutableList()

    val width: Int = rows[0].size
    val height: Int = rows.size

    val rows: List<List<T>> get() = data

    operator fun get(x: Int, y: Int): T = data[y][x]
    operator fun get(p: Point2D): T = get(p.x, p.y)
    operator fun set(p: Point2D, value: T) { data[p.y][p.x] = value }

    fun contains(point: Point2D): Boolean {
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

    data class Square<T>(
        val value: T,
        val point: Point2D
    )
}
