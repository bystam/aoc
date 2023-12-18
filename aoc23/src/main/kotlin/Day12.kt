
object Day12: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day12)

    override val testInput: String? = """
        ???.### 1,1,3
        .??..??...?##. 1,1,3
        ?#?#?#?#?#?#?#? 1,3,1,6
        ????.#...#... 4,1,1
        ????.######..#####. 1,6,5
        ?###???????? 3,2,1
    """.trimIndent()

    override fun task1(input: Input): Any {
        return input.lines.map { Record.from(it) }.sumOf { it.countTotalArrangements() }
    }

    override fun task2(input: Input): Any {
        val records = input.lines.map { Record.from(it).unfolded() }

        return records.sumOf {
            it.countTotalArrangements()
        }
    }

    data class Record(
        val arrangement: String,
        val brokenGroups: List<Int>
    ) {

        fun unfolded(): Record = Record(
            arrangement = "$arrangement?$arrangement?$arrangement?$arrangement?$arrangement",
            brokenGroups = brokenGroups + brokenGroups + brokenGroups + brokenGroups + brokenGroups
        )

        fun countTotalArrangements(): Long {
            return count(arrangement, brokenGroups, mutableMapOf())
        }

        // stolen from https://github.com/eagely/adventofcode/blob/main/src/main/kotlin/solutions/y2023/Day12.kt
        // because I suck at programming and I hate this day
        private fun count(config: String, groups: List<Int>, cache: MutableMap<Pair<String, List<Int>>, Long>): Long {
            if (groups.isEmpty()) return if ("#" in config) 0 else 1
            if (config.isEmpty()) return 0

            return cache.getOrPut(config to groups) {
                var result = 0L
                if (config.first() in ".?")
                    result += count(config.drop(1), groups, cache)
                if (config.first() in "#?" && groups.first() <= config.length && "." !in config.take(groups.first()) && (groups.first() == config.length || config[groups.first()] != '#'))
                    result += count(config.drop(groups.first() + 1), groups.drop(1), cache)
                result
            }
        }

        companion object {
            fun from(string: String): Record {
                val (arrangement, groups) = string.split(" ")
                return Record(arrangement, groups.split(",").map { it.toInt() })
            }
        }
    }

}
