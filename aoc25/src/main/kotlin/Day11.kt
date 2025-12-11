import java.math.BigInteger

object Day11 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day11)

    override fun task1(input: Input): Any {
        val graph = mutableMapOf<String, Set<String>>()
        for (line in input.lines) {
            val (name, outputString) = line.split(": ")
            val outputs = outputString.split(" ")
            graph[name] = outputs.toSet()
        }
        return bfs(graph, start = "you").size
    }

    override fun task2(input: Input): Any {
        val graph = mutableMapOf<String, Set<String>>()
        for (line in input.lines) {
            val (name, outputString) = line.split(": ")
            val outputs = outputString.split(" ")
            graph[name] = outputs.toSet()
        }

        val inverted = invert(graph)

        // it has to be fft -> dac, because there are no paths from dac -> fft
        return numPaths(inverted, current = "out", "dac") *
                numPaths(inverted, current = "dac", "fft") *
                numPaths(inverted, current = "fft", "svr")
    }

    private fun bfs(graph: Map<String, Set<String>>, start: String, end: String = "out"): List<Set<String>> {
        val queue = mutableListOf(Pair(start, emptySet<String>()))

        val paths = mutableListOf<Set<String>>()
        while (queue.isNotEmpty()) {
            val (current, visited) = queue.removeFirst()

            if (current == end) {
                paths += visited
                continue
            }

            graph[current]?.forEach { neighbor ->
                if (neighbor !in visited) {
                    queue += Pair(neighbor, visited + current)
                }
            }
        }
        return paths
    }

    private fun invert(graph: Map<String, Set<String>>): Map<String, Set<String>> {
        val result = mutableMapOf<String, MutableSet<String>>()
        for ((from, tos) in graph) {
            for (to in tos) {
                result.getOrPut(to) { mutableSetOf() }.add(from)
            }
        }
        return result
    }

    private fun numPaths(
        inverted: Map<String, Set<String>>,
        current: String,
        end: String,
        memo: MutableMap<Pair<String, String>, BigInteger> = mutableMapOf(),
    ): BigInteger = memo.getOrPut(Pair(current, end)) {
        if (current == end) return@getOrPut BigInteger.ONE
        val previous = inverted[current] ?: emptySet()
        previous.sumOf { node ->
            numPaths(inverted, node, end, memo)
        }
    }
}
