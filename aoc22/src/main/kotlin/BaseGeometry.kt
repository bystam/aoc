
data class Point2D(
    val x: Int,
    val y: Int,
) {
    fun offset(dx: Int = 0, dy: Int = 0) = Point2D(x + dx, y + dy)

    override fun toString(): String = "($x,$y)"
}

class Grid2D<T>(
    input: List<List<T>>
) {

    private val data: MutableList<MutableList<T>> = input.map { it.toMutableList() }.toMutableList()

    val width: Int = input[0].size
    val height: Int = input.size

    val rows: List<List<T>> get() = data

    operator fun get(x: Int, y: Int): T = data[y][x]
    operator fun get(p: Point2D): T = get(p.x, p.y)
    operator fun set(p: Point2D, value: T) { data[p.y][p.x] = value }

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

    data class Square<T>(
        val value: T,
        val point: Point2D
    )
}