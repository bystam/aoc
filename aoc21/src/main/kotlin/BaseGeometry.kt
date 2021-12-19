
data class Point2D(
    val x: Int,
    val y: Int,
) {
    fun offset(dx: Int = 0, dy: Int = 0) = Point2D(x + dx, y + dy)
}

class Grid2D<T>(
    val data: List<List<T>>
) {

    val width: Int = data[0].size
    val height: Int = data.size

    operator fun get(x: Int, y: Int): T = data[y][x]
    operator fun get(p: Point2D): T = get(p.x, p.y)

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

    data class Square<T>(
        val value: T,
        val point: Point2D
    )
}