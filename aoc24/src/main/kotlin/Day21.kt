object Day21 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day21)

    override fun task1(input: Input): Any {
        return input.lines.sumOf { line ->
            val totalDistance = shortestPath(line, 2, Numpad, mutableMapOf())
            val numberPart = line.removeSuffix("A").toLong()
            totalDistance * numberPart
        }
    }

    override fun task2(input: Input): Any {
        return input.lines.sumOf { line ->
            val totalDistance = shortestPath(line, 25, Numpad, mutableMapOf())
            val numberPart = line.removeSuffix("A").toLong()
            totalDistance * numberPart
        }
    }

    private fun shortestPath(
        keys: String,
        depth: Int,
        keyboard: Keyboard,
        cache: MutableMap<Pair<String, Int>, Long>
    ): Long {
        return cache.getOrPut(keys to depth) {
            "A$keys".windowed(2).sumOf { fromTo ->
                val (from, to) = (fromTo[0] to fromTo[1])
                val allPossiblePaths = keyboard.paths[from to to]!!

                if (depth == 0)
                    return@sumOf allPossiblePaths.minOf { it.length.toLong() }

                allPossiblePaths.minOf { path ->
                    shortestPath(path, depth - 1, Directional, cache)
                }
            }
        }
    }

    abstract class Keyboard(
        private val graph: Map<Char, List<Edge>>
    ) {
        val paths: Map<Pair<Char, Char>, List<String>> = buildMap {
            graph.keys.forEach { from ->
                graph.keys.forEach { to ->
                    put(Pair(from, to), allWalks(from, to))
                }
            }
        }

        private fun allWalks(from: Char, to: Char): List<String> {
            data class Step(
                val current: Char,
                val steps: List<Char>,
                val history: Set<Char>
            )

            val result = mutableListOf<String>()
            val queue = mutableListOf(Step(from, emptyList(), setOf(from)))
            while (queue.isNotEmpty()) {
                val (current, steps, history) = queue.removeFirst()

                if (current == to) {
                    result += steps.joinToString("") + "A"
                    continue
                }

                val neighbors = graph[current]!!
                    .filter { it.destination !in history }
                for ((dir, key) in neighbors) {
                    queue += Step(key, steps + dir, history + key)
                }
            }

            val shortest = result.minOf { it.length }
            return result.filter { it.length == shortest }
        }
    }

    object Numpad : Keyboard(
        graph = mapOf(
            'A' to listOf(
                Edge('<', '0'),
                Edge('^', '3')
            ),
            '0' to listOf(
                Edge('>', 'A'),
                Edge('^', '2')
            ),
            '1' to listOf(
                Edge('>', '2'),
                Edge('^', '4'),
            ),
            '2' to listOf(
                Edge('>', '3'),
                Edge('^', '5'),
                Edge('v', '0'),
                Edge('<', '1'),
            ),
            '3' to listOf(
                Edge('^', '6'),
                Edge('v', 'A'),
                Edge('<', '2'),
            ),
            '4' to listOf(
                Edge('>', '5'),
                Edge('^', '7'),
                Edge('v', '1'),
            ),
            '5' to listOf(
                Edge('>', '6'),
                Edge('^', '8'),
                Edge('v', '2'),
                Edge('<', '4'),
            ),
            '6' to listOf(
                Edge('^', '9'),
                Edge('v', '3'),
                Edge('<', '5'),
            ),
            '7' to listOf(
                Edge('>', '8'),
                Edge('v', '4'),
            ),
            '8' to listOf(
                Edge('>', '9'),
                Edge('v', '5'),
                Edge('<', '7'),
            ),
            '9' to listOf(
                Edge('v', '6'),
                Edge('<', '8'),
            ),
        )
    )

    object Directional : Keyboard(
        graph = mapOf(
            'A' to listOf(
                Edge('<', '^'),
                Edge('v', '>')
            ),
            '^' to listOf(
                Edge('>', 'A'),
                Edge('v', 'v')
            ),
            '>' to listOf(
                Edge('^', 'A'),
                Edge('<', 'v')
            ),
            'v' to listOf(
                Edge('^', '^'),
                Edge('<', '<'),
                Edge('>', '>'),
            ),
            '<' to listOf(
                Edge('>', 'v'),
            ),
        )
    )

    data class Edge(
        val direction: Char,
        val destination: Char
    )
}
