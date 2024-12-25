object Day25 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day25)

    override fun task1(input: Input): Any {
        val (locks, keys) = parseAll(input)
        return locks.sumOf { lock ->
            keys.count { key ->
                key.fitsIn(lock)
            }
        }
    }

    override fun task2(input: Input): Any {
        return "Done! :)"
    }

    private fun parseAll(input: Input): Pair<List<Lock>, List<Key>> {
        val locks = mutableListOf<Lock>()
        val keys = mutableListOf<Key>()

        for (thing in input.text().split("\n\n")) {
            if (thing.startsWith("#####")) {
                locks += Lock(parseHeights(thing.lines().drop(1)))
            }
            if (thing.startsWith(".....")) {
                keys += Key(parseHeights(thing.lines().reversed().drop(1)))
            }
        }

        return locks to keys
    }

    private fun parseHeights(lines: List<String>): List<Int> {
        val heights = mutableListOf(0, 0, 0, 0, 0)
        for (line in lines) {
            line.forEachIndexed { index, c ->
                if (c == '#') {
                    heights[index]++
                }
            }
        }
        return heights
    }

    data class Lock(
        val heights: List<Int>,
    )

    data class Key(
        val heights: List<Int>
    ) {
        fun fitsIn(lock: Lock): Boolean {
            return heights.zip(lock.heights).none { (k, l) ->
                k + l >= 6
            }
        }
    }
}
