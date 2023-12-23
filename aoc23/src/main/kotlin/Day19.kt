object Day19: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day19)

    override val testInput: String = """
        px{a<2006:qkq,m>2090:A,rfg}
        pv{a>1716:R,A}
        lnx{m>1548:A,A}
        rfg{s<537:gd,x>2440:R,A}
        qs{s>3448:A,lnx}
        qkq{x<1416:A,crn}
        crn{x>2662:A,R}
        in{s<1351:px,qqz}
        qqz{s>2770:qs,m<1801:hdj,R}
        gd{a>3333:R,R}
        hdj{m>838:A,pv}

        {x=787,m=2655,a=1222,s=2876}
        {x=1679,m=44,a=2067,s=496}
        {x=2036,m=264,a=79,s=2244}
        {x=2461,m=1339,a=466,s=291}
        {x=2127,m=1623,a=2188,s=1013}
    """.trimIndent()

    override fun task1(input: Input): Any {
        val lines = input.lines.toList()
        val divider = lines.indexOf("")
        val workflows = lines.subList(0, divider).map { Workflow.from(it) }.associateBy { it.name }
        val parts = lines.subList(divider + 1, lines.size).map { Part.from(it) }
        val acceptedParts = parts
            .filter { runPart(workflows, it) == Accepted }
        return acceptedParts.sumOf { it.rating }
    }

    override fun task2(input: Input): Any {
        val lines = input.lines.toList()
        val divider = lines.indexOf("")
        val workflows = lines.subList(0, divider).map { Workflow.from(it) }.associateBy { it.name }
        val startRange = PartRange(
            Property.entries.associateWith { (1..4000) }.toMap()
        )

        val ranges = resolve2(workflows, workflows["in"]!!, listOf(startRange))

        var sum = 0L
        for (range in ranges) {
            var part = 1L
            range.ranges.values.forEach {
                part *= it.length
            }
            sum += part
        }

        return sum
    }

    private fun runPart(workflows: Map<String, Workflow>, part: Part): TerminalOutcome {
        var currentWorkflow = workflows["in"]!!

        while (true) {
            when (val outcome = currentWorkflow.run(part)) {
                is Next -> currentWorkflow = workflows[outcome.name]!!
                is TerminalOutcome -> return outcome
            }
        }
    }

    private fun resolve2(workflows: Map<String, Workflow>, current: Workflow, ranges: List<PartRange>): List<PartRange> {
        val added = mutableListOf<PartRange>()
        var workingRanges = ranges
        for (rule in current.rules) {
            val successes = mutableListOf<PartRange>()
            val failed = mutableListOf<PartRange>()
            for (range in workingRanges) {
                val (truthy, falsy) = rule.split(range)
                if (truthy != null) {
                    successes += truthy
                }
                if (falsy != null) {
                    failed += falsy
                }

                when (rule.outcome) {
                    is Next -> added += resolve2(workflows, workflows[rule.outcome.name]!!, successes)
                    Accepted -> added += successes
                    Rejected -> {}
                }
            }
            workingRanges = failed
        }
        return when (current.fallback) {
            is Next -> added + resolve2(workflows, workflows[current.fallback.name]!!, workingRanges)
            Accepted -> workingRanges
            Rejected -> emptyList()
        }
    }

    data class PartRange(
        val ranges: Map<Property, IntRange>
    ) {
        fun copyWith(property: Property, range: IntRange): PartRange {
            val copy = ranges.toMutableMap()
            copy[property] = range
            return PartRange(copy)
        }
    }

    data class Workflow(
        val name: String,
        val rules: List<Rule>,
        val fallback: Outcome
    ) {

        fun run(part: Part): Outcome {
            val ruleHit = rules.firstOrNull { it.test(part) }
            return ruleHit?.outcome ?: fallback
        }

        companion object {
            fun from(string: String): Workflow {
                fun outcome(str: String) = when (str) {
                    "A" -> Accepted
                    "R" -> Rejected
                    else -> Next(str)
                }

                val (name, rest) = string.split("{")
                val rulesEtc = rest.removeSuffix("}").split(",")
                val fallback = outcome(rulesEtc.last())
                val rules = rulesEtc.dropLast(1).map { ruleString ->
                    val property = Property.valueOf(ruleString.first().uppercase())
                    val dividerIndex = ruleString.indexOf(":")
                    val limit = ruleString.substring(2, dividerIndex).toInt()
                    val outcomeString = ruleString.substring(dividerIndex + 1)
                    Rule(
                        property = property,
                        comparison = Comparison.entries.first { it.char == ruleString[1] },
                        limit = limit,
                        outcome = outcome(outcomeString)
                    )
                }
                return Workflow(
                    name = name,
                    rules = rules,
                    fallback = fallback
                )
            }
        }
    }

    data class Rule(
        val property: Property,
        val comparison: Comparison,
        val limit: Int,
        val outcome: Outcome
    ) {

        fun split(range: PartRange): Pair<PartRange?, PartRange?> {
            val r = range.ranges[property]!!
            return when (comparison) {
                Comparison.LT -> when {
                    r.last <= limit -> Pair(range, null)
                    r.first >= limit -> Pair(null, range)
                    else -> Pair(
                        range.copyWith(property, (r.first..<limit)),
                        range.copyWith(property, (limit..r.last))
                    )
                }
                Comparison.GT -> when {
                    r.first >= limit -> Pair(range, null)
                    r.last <= limit -> Pair(null, range)
                    else -> Pair(
                        range.copyWith(property, (limit + 1..r.last)),
                        range.copyWith(property, (r.first..limit))
                    )
                }
            }
        }

        fun test(part: Part): Boolean = when (comparison) {
            Comparison.LT ->  part.ratings[property]!! < limit
            Comparison.GT ->  part.ratings[property]!! > limit
        }

        override fun toString(): String {
            return "${property.name}:$outcome"
        }

    }

    sealed interface Outcome
    sealed interface TerminalOutcome : Outcome
    data object Accepted : TerminalOutcome {
        override fun toString(): String = "A"
    }
    data object Rejected : TerminalOutcome {
        override fun toString(): String = "R"
    }
    data class Next(val name: String): Outcome {
        override fun toString(): String = name
    }

    data class Part(
        val ratings: Map<Property, Int>
    ) {

        val rating: Int get() = ratings.values.sum()

        companion object {
            fun from(string: String): Part {
                val (x, m, a, s) = string
                    .drop(1).dropLast(1) // remove curly brace
                    .split(",")
                    .map { it.split("=").last().toInt() }
                return Part(
                    mapOf(Property.X to x, Property.M to m, Property.A to a, Property.S to s)
                )
            }
        }
    }

    enum class Property {
        X, M, A, S
    }

    enum class Comparison(val char: Char) {
        GT('>'), LT('<');

        override fun toString(): String {
            return char.toString()
        }
    }

    private fun IntRange.intersect(other: IntRange): IntRange {
        return (maxOf(first, other.first)..minOf(last, other.last))
    }

    private val IntRange.length get() = if (isEmpty()) 0 else last - first + 1
}
