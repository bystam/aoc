
object Day12: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day12)

    override fun task1(input: Input): Any {
        val regions = findRegions(input)
        return regions.sumOf { it.fencePrice() }
    }

    override fun task2(input: Input): Any {
        val regions = findRegions(input)
        return regions.sumOf { it.bulkDiscountPrice() }
    }

    private fun findRegions(input: Input): MutableList<Region> {
        val map = Grid2D.charMatrix(input.lines)
        val totalVisited = mutableSetOf<Point2D>()

        val regions = mutableListOf<Region>()
        for (point in map.allPoints()) {
            if (point in totalVisited) continue
            val region = map.findRegion(point)
            totalVisited += region.points
            regions += region
        }
        return regions
    }

    private fun Grid2D<Char>.findRegion(start: Point2D): Region {
        val plant = this[start]
        val visited = mutableSetOf(start)
        val queue = mutableListOf(start)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            val regionNeighbors = neighbors(current)
                .filter { it.point !in visited }
                .filter { it.value == plant }

            for (neighbor in regionNeighbors) {
                visited += neighbor.point
                queue.add(neighbor.point)
            }
        }
        return Region(plant, visited)
    }

    data class Region(
        val plant: Char,
        val points: Set<Point2D>,
    ) {
        fun fencePrice(): Int {
            val area = points.size
            var perimeter = 0
            for (point in points) {
                for (dir in Vec2D.allOrthogonal) {
                    val neighbor = point.offset(dir)
                    if (neighbor !in points) {
                        perimeter += 1
                    }
                }
            }

            return perimeter * area
        }

        fun bulkDiscountPrice(): Int {
            val area = points.size
            val outerPerimeter = LinkedHashSet<Pair<Point2D, Vec2D>>()
            for (point in points) {
                for (dir in Vec2D.allOrthogonal) {
                    val neighbor = point.offset(dir)
                    if (neighbor !in points) {
                        outerPerimeter += neighbor to dir
                    }
                }
            }

            var sides = 0
            while (outerPerimeter.isNotEmpty()) {
                sides += 1
                val (point, dir) = outerPerimeter.removeFirst()

                // prune in one direction
                val oneDirection =  dir.rotated(Rotation90.CLOCKWISE)
                var next = point.offset(oneDirection)
                while ((next to dir) in outerPerimeter) {
                    outerPerimeter.remove(next to dir)
                    next = next.offset(oneDirection)
                }

                // prune in other direction
                val otherDirection =  dir.rotated(Rotation90.COUNTERCLOCKWISE)
                next = point.offset(otherDirection)
                while ((next to dir) in outerPerimeter) {
                    outerPerimeter.remove(next to dir)
                    next = next.offset(otherDirection)
                }
            }
            return sides * area
        }
    }
}
