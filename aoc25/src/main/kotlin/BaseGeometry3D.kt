import kotlin.math.pow
import kotlin.math.sqrt

data class Point3D(
    val x: Int,
    val y: Int,
    val z: Int,
) {
    fun offset(dx: Int = 0, dy: Int = 0, dz: Int = 0): Point3D = Point3D(x + dx, y + dy, z + dz)
    fun offset(v: Vec3D): Point3D = offset(v.dx, v.dy, v.dz)
    override fun toString(): String = "($x,$y,$z)"

    fun distanceTo(other: Point3D): Double = sqrt((other.x - x).toDouble().pow(2) + (other.y - y).toDouble().pow(2) + (other.z - z).toDouble().pow(2))

    companion object {
        val origin: Point3D = Point3D(0, 0, 0)
    }
}

data class Vec3D(
    val dx: Int = 0,
    val dy: Int = 0,
    val dz: Int = 0,
) {

    companion object {
        val allOrthogonal: List<Vec3D> = listOf(
            Vec3D(dx = -1),
            Vec3D(dx = 1),
            Vec3D(dy = -1),
            Vec3D(dy = 1),
            Vec3D(dz = -1),
            Vec3D(dz = 1),
        )
    }

    operator fun plus(other: Vec3D): Vec3D = Vec3D(dx + other.dx, dy + other.dy, dz + other.dz)
}
