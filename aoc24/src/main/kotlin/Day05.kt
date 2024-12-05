object Day05 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day05)

    override val testInput: String = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent()

    override fun task1(input: Input): Any {
        val rules = input.lines.takeWhile { it.isNotBlank() }.map { Rule.from(it) }
        val updates = input.lines.toList().reversed().takeWhile { it.isNotBlank() }.map { Update.from(it) }

        return updates
            .filter { update -> rules.none { it.isSatisfied(update.pages) == false } }
            .sumOf { it.middlePage }
    }

    override fun task2(input: Input): Any {
        val rules = input.lines.takeWhile { it.isNotBlank() }.map { Rule.from(it) }.toList()
        val updates = input.lines.toList().reversed().takeWhile { it.isNotBlank() }.map { Update.from(it) }
        return updates
            .filterNot { update -> rules.none { it.isSatisfied(update.pages) == false } }
            .map { it.fixedAccordingTo(rules) }
            .sumOf { it.middlePage }
    }

    data class Rule(
        val first: Int,
        val second: Int
    ) {

        fun isSatisfied(pages: List<Int>): Boolean? {
            val a = pages.indexOf(first)
            val b = pages.indexOf(second)
            if (a == -1 || b == -1) {
                return null
            }
            return a < b
        }

        companion object {
            fun from(line: String): Rule {
                val (first, second) = line.split("|")
                return Rule(first.toInt(), second.toInt())
            }
        }
    }

    data class Update(
        val pages: List<Int>
    ) {

        val middlePage: Int = pages[pages.size/ 2]

        fun fixedAccordingTo(rules: List<Rule>): Update {
            val fixed = pages.toMutableList()
            while (true) {
                val violation = rules.firstOrNull { it.isSatisfied(fixed) == false }
                if (violation == null) break

                val firstIndex = fixed.indexOf(violation.first)
                val secondIndex = fixed.indexOf(violation.second)

                fixed[firstIndex] = violation.second
                fixed[secondIndex] = violation.first
            }
            return Update(fixed)
        }

        companion object {
            fun from(line: String): Update {
                return Update(line.split(",").map { it.toInt() })
            }
        }
    }
}
