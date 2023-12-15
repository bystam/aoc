
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
//        println("???.### 1,1,3 = " + Record.from("???.### 1,1,3").countTotalArrangements())
//        println(".??..??...?##. 1,1,3 = " + Record.from(".??..??...?##. 1,1,3").countTotalArrangements())
        println("?#?#?#?#?#?#?#? 1,3,1,6 = " + Record.from("?#?#?#?#?#?#?#? 1,3,1,6").countTotalArrangements())
//        println("????.#...#... 4,1,1 = " + Record.from("????.#...#... 4,1,1").countTotalArrangements())
//        println("????.######..#####. 1,6,5 = " + Record.from("????.######..#####. 1,6,5").countTotalArrangements())
//        println("?###???????? 3,2,1 = " + Record.from("?###???????? 3,2,1").countTotalArrangements())
        return input.lines.map { Record.from(it) }.sumOf { it.countTotalArrangements() }
    }

    override fun task2(input: Input): Any {
//        println(
//            Record.from("????.######..#####. 1,6,5").unfolded().countTotalArrangements()
//        )
        return ""// input.lines.map { Record.from(it).unfolded() }.sumOf { it.countTotalArrangements() }
    }

    data class Record(
        val arrangement: String,
        val brokenGroups: List<Int>
    ) {

        fun unfolded(): Record = Record(
            arrangement = arrangement + arrangement + arrangement + arrangement + arrangement,
            brokenGroups = brokenGroups + brokenGroups + brokenGroups + brokenGroups + brokenGroups
        )

        fun countTotalArrangements(): Int {
//            return countTotalArrangements(arrangement)
            return countTotalArrangements2(
                brokenGroups, 0
            )
        }

        private fun countTotalArrangements2(
            brokenGroupsLeft: List<Int>,
            fromIndex: Int
        ): Int {
            val group = brokenGroupsLeft.firstOrNull() ?: return 1
            var sum = 0
            var index = fromIndex
            while (true) {
                val beyondGroup = (index+group)
                if (beyondGroup > arrangement.length) {
                    break
                }
                val isPossibleMatch = arrangement.substring(index..<beyondGroup).all {
                    it == '#' || it == '?'
                }
                if (isPossibleMatch) {
                    sum += countTotalArrangements2(
                        brokenGroupsLeft.drop(1), beyondGroup + 1
                    )
                }
                index += 1
            }
            return sum
        }

        private fun countTotalArrangements(
            arrangementSoFar: String
        ): Int {
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
