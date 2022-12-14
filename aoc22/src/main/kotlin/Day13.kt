class Day13: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day13())
    }

    override val testInput: String = """
        [1,1,3,1,1]
        [1,1,5,1,1]

        [[1],[2,3,4]]
        [[1],4]

        [9]
        [[8,7,6]]

        [[4,4],4,4]
        [[4,4],4,4,4]

        [7,7,7,7]
        [7,7,7]

        []
        [3]

        [[[]]]
        [[]]

        [1,[2,[3,[4,[5,6,7]]]],8,9]
        [1,[2,[3,[4,[5,6,0]]]],8,9]
    """.trimIndent()

    override fun task1(input: Input): Any {
        val pairs = input.text().split("\n\n")
            .map { pair ->
                val (lhs, rhs) = pair.split("\n")
                Part.parse(lhs) to Part.parse(rhs)
            }
        return pairs.mapIndexed { index, (lhs, rhs) ->
            if (lhs.match(rhs)!!) index + 1 else 0
        }.sum()
    }

    override fun task2(input: Input): Any {
        val packets = input.lines
            .filter { it.isNotEmpty() }
            .map { Part.parse(it) }
            .toMutableList()
        val div1 = Part.parse("[[2]]")
        val div2 = Part.parse("[[6]]")
        packets += listOf(div1, div2)

        packets.sort()
        return (packets.indexOf(div1) + 1) * (packets.indexOf(div2) + 1)
    }

    sealed class Part : Comparable<Part> {
        data class Integer(
            val value: Int
        ) : Part()
        data class IntList(
            val parts: MutableList<Part> = mutableListOf()
        ) : Part()

        fun match(rhs: Part): Boolean? {
            return when (this) {
                is Integer -> when (rhs) {
                    is Integer -> if (value < rhs.value) true else if (value > rhs.value) false else null
                    is IntList -> IntList(mutableListOf(Integer(value))).match(rhs)
                }
                is IntList -> when (rhs) {
                    is IntList -> {
                        val default = when {
                            parts.size < rhs.parts.size -> true
                            parts.size > rhs.parts.size -> false
                            else -> null
                        }
                        parts.zip(rhs.parts).firstNotNullOfOrNull { (nestedLhs, nestedRhs) ->
                            nestedLhs.match(nestedRhs)
                        } ?: default
                    }
                    is Integer -> this.match(IntList(mutableListOf(Integer(rhs.value))))
                }
            }
        }

        override fun compareTo(other: Part): Int = when (this.match(other)) {
            true -> -1
            false -> 1
            null -> 0
        }

        override fun toString(): String = when (this) {
            is IntList -> parts.toString()
            is Integer -> value.toString()
        }

        companion object {
            fun parse(string: String): IntList {
                val stack = mutableListOf<IntList>()
                lateinit var outermost: IntList
                var index = 0
                while (index < string.length) {
                    if (string[index] == '[') {
                        stack.add(IntList())
                        index += 1
                        continue
                    }
                    if (string[index] == ']') {
                        val completed = stack.removeLast()
                        stack.lastOrNull()?.parts?.add(completed) ?: run { outermost = completed }
                        index += 1
                        continue
                    }
                    if (string[index] in '0'..'9') {
                        if (string[index + 1] == '0') {
                            val int = string.substring(index..index+1).toInt()
                            stack.last().parts.add(Integer(int))
                            index += 2
                        } else {
                            val int = string[index].digitToInt()
                            stack.last().parts.add(Integer(int))
                            index += 1
                        }
                        continue
                    }
                    index += 1
                }
                return outermost
            }
        }
    }

    private fun String.indexOfEither(startIndex: Int, vararg find: Char) {
        val indices = find.map { this.indexOf(it, startIndex) }.filter { it != -1 }.min()
    }
}
