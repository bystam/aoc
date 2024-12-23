object Day23 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day23)

    override fun task1(input: Input): Any {
        val graph = parseGraph(input)
        return graph.keys
            .flatMap { findLanGroup(it, graph, 3) }
            .filter { group -> group.any { it.startsWith("t") } }
            .toSet()
            .size
    }

    override fun task2(input: Input): Any {
        val graph = parseGraph(input)
        return findCliques(graph)
            .maxBy { it.size }
            .sorted()
            .joinToString(",")
    }

    private fun parseGraph(input: Input): Map<String, Set<String>> {
        val graph = mutableMapOf<String, MutableSet<String>>()
        input.lines.forEach { line ->
            val (a, b) = line.split("-")
            graph.getOrPut(a) { mutableSetOf() }.add(b)
            graph.getOrPut(b) { mutableSetOf() }.add(a)
        }
        return graph
    }

    private fun findLanGroup(from: String, graph: Map<String, Set<String>>, depth: Int): List<Set<String>> {
        data class Node(
            val computer: String,
            val walked: Set<String>
        )

        val success = mutableListOf<Node>()
        val queue = mutableListOf(Node(from, setOf()))
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if (current.walked.size == depth) {
                if (current.computer == from) {
                    success += current
                }
                continue
            }

            val neighbors = graph[current.computer]!!
                .filter { it !in current.walked }
            for (neighbor in neighbors) {
                queue += Node(neighbor, current.walked + neighbor)
            }
        }

        return success
            .map { it.walked }
    }

    private fun findCliques(graph: Map<String, Set<String>>): List<Set<String>> {
        val cliques = mutableListOf<Set<String>>()
        bronKerbosch(emptySet(), graph.keys, emptySet(), graph, cliques)
        return cliques
    }

    private fun bronKerbosch(
        r: Set<String>,
        p: Set<String>,
        x: Set<String>,
        graph: Map<String, Set<String>>,
        biggest: MutableList<Set<String>>
    ) {
        if (p.isEmpty() && x.isEmpty()) {
            biggest += r
        }

        val _p = p.toMutableSet()
        val _x = x.toMutableSet()
        for (v in p) {
            val newR = r + v
            val newP = _p.intersect(graph[v]!!)
            val newX = _x.intersect(graph[v]!!)
            bronKerbosch(newR, newP, newX, graph, biggest)
            _p.remove(v)
            _x.add(v)
        }
    }
}
