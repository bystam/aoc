import product

object Day08: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day08)

    override fun task1(input: Input): Any {
        val boxes = input.lines
            .map { line ->
                val (x, y, z) = line.split(",").map { it.toInt() }
                Point3D(x, y, z)
            }
            .toList()

        val pairs = mutableListOf<Pair>()
        for (i in 0..boxes.lastIndex) {
            for (k in i + 1..boxes.lastIndex) {
                pairs += Pair(boxes[i], boxes[k])
            }
        }

        pairs.sortBy { it.distance }

        val circuits = mutableMapOf<Point3D, Circuit>()
        for ((from, to) in pairs.take(1000)) {
            val fromCircuit = circuits[from] ?: Circuit(setOf(from))
            val toCircuit = circuits[to] ?: Circuit(setOf(to))

            val allPoints = fromCircuit.points + toCircuit.points
            val merged = Circuit(allPoints)
            for (p in allPoints) {
                circuits[p] = merged
                circuits[p] = merged
            }
        }

        val top3 = circuits.values.distinct().sortedByDescending { it.points.size }.take(3)

        return top3.map { it.points.size.toLong() }.product()
    }

    override fun task2(input: Input): Any {
        val boxes = input.lines
            .map { line ->
                val (x, y, z) = line.split(",").map { it.toInt() }
                Point3D(x, y, z)
            }
            .toList()

        val pairs = mutableListOf<Pair>()
        for (i in 0..boxes.lastIndex) {
            for (k in i + 1..boxes.lastIndex) {
                pairs += Pair(boxes[i], boxes[k])
            }
        }

        pairs.sortBy { it.distance }

        val circuits = mutableMapOf<Point3D, Circuit>()
        for ((from, to) in pairs) {
            val fromCircuit = circuits[from] ?: Circuit(setOf(from))
            val toCircuit = circuits[to] ?: Circuit(setOf(to))

            val allPoints = fromCircuit.points + toCircuit.points
            val merged = Circuit(allPoints)
            for (p in allPoints) {
                circuits[p] = merged
                circuits[p] = merged
            }

            if (merged.points.size == boxes.size) {
                return (from.x.toLong() * to.x.toLong())
            }
        }

        return "omg fail"
    }

    data class Pair(val p1: Point3D, val p2: Point3D) {
        val distance = p1.distanceTo(p2)
    }

    class Circuit(val points: Set<Point3D>)
}
