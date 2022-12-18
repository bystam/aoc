import java.util.LinkedList

class Day18: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day18())
    }

    override val testInput: String = """
        2,2,2
        1,2,2
        3,2,2
        2,1,2
        2,3,2
        2,2,1
        2,2,3
        2,2,4
        2,2,6
        1,2,5
        3,2,5
        2,1,5
        2,3,5
    """.trimIndent()

    override fun task1(input: Input): Any {
        val droplets = input.lines
            .map { it.split(",") }
            .map { (x, y, z) -> Point3D(x.toInt(), y.toInt(), z.toInt()) }
            .toSet()

        var totalSides = 0
        for (p in droplets) {
            totalSides += p.neighbors().count { !droplets.contains(it) }
        }
        return totalSides
    }

    override fun task2(input: Input): Any {
        val droplets = input.lines
            .map { it.split(",") }
            .map { (x, y, z) -> Point3D(x.toInt(), y.toInt(), z.toInt()) }
            .toSet()
        val outerBox = Box(
            min = Point3D(
                x = droplets.minOf { it.x },
                y = droplets.minOf { it.y },
                z = droplets.minOf { it.z },
            ),
            max = Point3D(
                x = droplets.maxOf { it.x },
                y = droplets.maxOf { it.y },
                z = droplets.maxOf { it.z },
            )
        )
        var totalSides = 0
        for (p in droplets) {
            val freeSides = p.neighbors().filter { !droplets.contains(it) }
            totalSides += freeSides.count { !isAirpocket(it, droplets, outerBox) }
        }
        return totalSides
    }

    private fun isAirpocket(point: Point3D, droplets: Set<Point3D>, box: Box): Boolean {
        val queue = LinkedList<Point3D>().apply { addLast(point) }
        val visited = mutableSetOf<Point3D>()
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            if (visited.contains(next)) continue
            visited.add(next)
            if (!box.contains(next)) {
                // We made it out of the box
                return false
            }

            val airNeighbors = next.neighbors().filter { !droplets.contains(it) }
            queue.addAll(airNeighbors)
        }
        return true // could not get out
    }

    private fun Point3D.neighbors(): List<Point3D> = Vec3D.allOrthogonal.map { this.offset(it) }

    data class Box(
        val min: Point3D,
        val max: Point3D,
    ) {
        fun contains(p: Point3D): Boolean {
            return p.x >= min.x && p.y >= min.y && p.z >= min.z && p.x <= max.x && p.y <= max.y && p.z <= max.z
        }
    }
}
