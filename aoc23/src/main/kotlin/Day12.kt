
object Day12: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day12)

    override fun task1(input: Input): Any {
        return input.lines.map { Record.from(it) }.sumOf { it.countTotalArrangements() }
    }

    override fun task2(input: Input): Any {
        return "TODO"
    }

    data class Record(
        val arrangement: String,
        val brokenGroups: List<Int>
    ) {

        fun countTotalArrangements(): Int {
            return countTotalArrangements(arrangement)
        }

        private fun countTotalArrangements(
            arrangementSoFar: String
        ): Int {
            if (arrangementSoFar == ".###.##.#...") {
                println("woo")
            }
            val brokenGroupsSoFar = brokenGroups(arrangementSoFar)
            val doneGroupsSoFar = brokenGroupsSoFar.dropLast(1)

            if (brokenGroupsSoFar.size > brokenGroups.size) {
                return 0 // impossible
            }
            for ((doneGroup, realGroup) in doneGroupsSoFar.zip(brokenGroups)) {
                if (doneGroup != realGroup) {
                    return 0 // impossible
                }
            }
            if (brokenGroupsSoFar.isNotEmpty()) {
                if (brokenGroupsSoFar[brokenGroupsSoFar.lastIndex] > brokenGroups[brokenGroupsSoFar.lastIndex]) {
                    return 0
                }
            }

            val nextUnknown = arrangementSoFar.indexOf('?')
            if (nextUnknown == -1) return if (brokenGroupsSoFar == brokenGroups) 1 else 0

            val ifOperational = countTotalArrangements(
                arrangementSoFar = arrangementSoFar.replaceRange(nextUnknown, nextUnknown + 1, ".")
            )
            val ifBroken = countTotalArrangements(
                arrangementSoFar = arrangementSoFar.replaceRange(nextUnknown, nextUnknown + 1, "#")
            )
            return ifOperational + ifBroken
        }

        private fun brokenGroups(arrangementSoFar: String): List<Int> {
            val groups = mutableListOf<Int>()

            var currentGroup: Int = 0
            for (index in arrangementSoFar.indices) {
                when (arrangementSoFar[index]) {
                    '#' -> currentGroup += 1
                    '.' -> {
                        if (currentGroup > 0) {
                            groups += currentGroup
                            currentGroup = 0
                        }
                    }
                    '?' -> break
                }
            }
            if (currentGroup > 0) {
                groups += currentGroup
                currentGroup = 0
            }
            return groups
        }

        companion object {
            fun from(string: String): Record {
                val (arrangement, groups) = string.split(" ")
                return Record(arrangement, groups.split(",").map { it.toInt() })
            }
        }
    }

}
