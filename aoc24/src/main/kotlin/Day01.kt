import kotlin.math.absoluteValue

class Day01: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day01())
    }

    override fun task1(input: Input): Any {
        val (aList, bList) = input.parseLists()
        return aList.sorted().zip(bList.sorted())
            .sumOf { (a, b) ->
                (a - b).absoluteValue
            }
    }

    override fun task2(input: Input): Any {
        val (aList, bList) = input.parseLists()
        return aList.sumOf { a ->
            a * bList.count { it == a }
        }
    }

    private fun Input.parseLists(): Pair<List<Int>, List<Int>> {
        return lines
            .map {
                val (a, b) = it.split("   ")
                a.toInt() to b.toInt()
            }
            .fold(listOf<Int>() to listOf<Int>()) { (aList, bList), (a, b) ->
                (aList + a) to (bList + b)
            }
    }
}
