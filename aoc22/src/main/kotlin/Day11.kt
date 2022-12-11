import java.util.LinkedList

class Day11: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day11())
    }

    override val testInput: String? = """
        Monkey 0:
          Starting items: 79, 98
          Operation: new = old * 19
          Test: divisible by 23
            If true: throw to monkey 2
            If false: throw to monkey 3
        
        Monkey 1:
          Starting items: 54, 65, 75, 74
          Operation: new = old + 6
          Test: divisible by 19
            If true: throw to monkey 2
            If false: throw to monkey 0
        
        Monkey 2:
          Starting items: 79, 60, 97
          Operation: new = old * old
          Test: divisible by 13
            If true: throw to monkey 1
            If false: throw to monkey 3
        
        Monkey 3:
          Starting items: 74
          Operation: new = old + 3
          Test: divisible by 17
            If true: throw to monkey 0
            If false: throw to monkey 1
    """.trimIndent()

    override fun task1(input: Input): Any {
        val monkeys = input.text().split("\n\n").map { Monkey.parse(it) }
        repeat(20) {
            for (monkey in monkeys) {
                monkey.inspect(monkeys) { worry -> worry / 3 }
            }
        }
        val (a, b) = monkeys.sortedByDescending { it.inspections }.take(2)
        return a.inspections * b.inspections
    }

    override fun task2(input: Input): Any {
        val monkeys = input.text().split("\n\n").map { Monkey.parse(it) }
        val totalModulo = monkeys.fold(1L) { acc, monkey -> acc * monkey.testDivider }
        repeat(10000) {
            for (monkey in monkeys) {
                monkey.inspect(monkeys) { worry -> worry % totalModulo }
            }
        }
        val (a, b) = monkeys.sortedByDescending { it.inspections }.take(2)
        return a.inspections * b.inspections
    }

    class Monkey(
        val id: Int,
        val items: LinkedList<Long>,
        val operation: (Long) -> Long,
        val testDivider: Long,
        val trueDestination: Int,
        val falseDestination: Int,
    ) {

        var inspections: Long = 0

        fun inspect(monkeys: List<Monkey>, worryHandler: (Long) -> Long) {
            while (items.isNotEmpty()) {
                val item = items.removeFirst()
                val newLevel = worryHandler(operation(item))
                val destination = if ((newLevel % testDivider) == 0L) trueDestination else falseDestination
                monkeys[destination].items.add(newLevel)
                inspections++
            }
        }

        companion object {

            fun parse(string: String): Monkey {
                val (title, start, operation, test, ifTrue, ifFalse) = string
                    .split("\n").map { it.trim() }
                val id = title.removePrefix("Monkey ").removeSuffix(":").toInt()
                val items = start.removePrefix("Starting items: ").split(", ").map { it.toLong() }
                val (operator, term) = operation.removePrefix("Operation: new = old ").split(" ")
                val testDivider = test.removePrefix("Test: divisible by ").toLong()
                val trueDestination = ifTrue.removePrefix("If true: throw to monkey ").toInt()
                val falseDestination = ifFalse.removePrefix("If false: throw to monkey ").toInt()
                return Monkey(
                    id = id,
                    items = LinkedList(items),
                    operation = when (operator) {
                        "*" -> { old ->
                            old * (if (term == "old") old else term.toLong())
                        }
                        "+" -> { old ->
                            old + (if (term == "old") old else term.toLong())
                        }
                        else -> TODO()
                    },
                    testDivider = testDivider,
                    trueDestination = trueDestination,
                    falseDestination = falseDestination
                )
            }
        }
    }
}
