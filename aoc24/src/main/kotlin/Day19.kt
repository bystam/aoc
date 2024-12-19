object Day19 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day19)

    override fun task1(input: Input): Any {
        val lines = input.lines.toList()
        val towels = lines.first().split(", ")
        val patterns = lines.drop(2)
        return patterns.count {
            waysPatternCanBeBuilt(it, towels, mutableMapOf()) > 0
        }
    }

    override fun task2(input: Input): Any {
        val lines = input.lines.toList()
        val towels = lines.first().split(", ")
        val patterns = lines.drop(2)
        return patterns.sumOf {
            waysPatternCanBeBuilt(it, towels, mutableMapOf())
        }
    }

    private fun waysPatternCanBeBuilt(pattern: String, towels: List<String>, memory: MutableMap<String, Long>): Long {
        memory[pattern]?.let { return it }
        if (pattern.isEmpty()) return 1
        val count = towels
            .filter { pattern.startsWith(it) }
            .sumOf {
                waysPatternCanBeBuilt(pattern.substring(it.length), towels, memory)
            }
        memory[pattern] = count
        return count
    }
}
