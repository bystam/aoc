
object Day22: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day22)

    override fun task1(input: Input): Any {
        return input.lines.map { it.toLong() }
            .map { generate(it, 2000).last() }
            .sum()
    }

    override fun task2(input: Input): Any {
        val merchants = input.lines.map { it.toLong() }
            .map { start -> generate(start, 2000).map { it.toInt() % 10 } }
            .map { Merchant(it) }
            .toList()

        val allFourDiffs = merchants
            .flatMap { merchant ->
                merchant.indexOfSequences.keys
            }
            .toSet()
        return allFourDiffs.maxOf { diff ->
            merchants.sumOf { it.firstPriceOfSequence(diff) }
        }
    }

    private fun generate(number: Long, times: Int): List<Long> {
        var result = number
        return (0 until times).map {
            val next = result
            result = next(result)
            next
        }
    }

    private fun next(number: Long): Long {
        var result = prune(number xor (number * 64))
        result = prune(result xor (result / 32))
        result = prune(result xor (result * 2048))
        return result
    }

    private fun prune(number: Long): Long = number % 16777216

    class Merchant(
        val prices: List<Int>,
    ) {
        val diffs: List<Int> = prices.windowed(2)
            .map { (a, b) -> b - a }
        val indexOfSequences: Map<List<Int>, List<Int>> = diffs.windowed(4).withIndex()
            .groupBy { (index, diff) -> diff }
            .mapValues { (_, value) -> value.map { it.index } }

        fun firstPriceOfSequence(sequence: List<Int>): Int {
            val index = indexOfSequences[sequence]?.min() ?: return 0
            return prices[index + 4]
        }
    }
}
