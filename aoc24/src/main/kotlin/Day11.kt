import java.math.BigInteger

object Day11: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day11)

    override fun task1(input: Input): Any {
        val stones = input.text().split(" ").map { it.toBigInteger() }.toMutableList()
        repeat(25) {
            for (i in stones.indices.reversed()) {
                val stone = stones[i]
                val stoneString = stone.toString()
                when {
                    stone == BigInteger.ZERO -> stones[i] = BigInteger.ONE
                    stoneString.length % 2 == 0 -> {
                        val left = stoneString.substring(0, stoneString.length / 2)
                        val right = stoneString.substring(stoneString.length / 2, stoneString.length)
                        stones[i] = left.toBigInteger()
                        stones.add(i + 1, right.toBigInteger())
                    }
                    else -> stones[i] = stone * BigInteger.valueOf(2024)
                }
            }
        }
        return stones.size
    }

    override fun task2(input: Input): Any {
        val stones = input.text().split(" ").map { it.toBigInteger() }.associateWith { 1L }.toMutableMap()

        repeat(75) {
            val creations = mutableMapOf<BigInteger, Long>()
            val destructions = mutableMapOf<BigInteger, Long>()
            for (stone in stones.keys) {
                val stoneString = stone.toString()
                val stoneCount = stones[stone]!!
                when {
                    stone == BigInteger.ZERO -> {
                        creations[BigInteger.ONE] = stoneCount
                    }
                    stoneString.length % 2 == 0 -> {
                        val left = stoneString.substring(0, stoneString.length / 2).toBigInteger()
                        val right = stoneString.substring(stoneString.length / 2, stoneString.length).toBigInteger()
                        creations[left] = (creations[left] ?: 0) + stoneCount
                        creations[right] = (creations[right] ?: 0) + stoneCount
                    }
                    else -> {
                        creations[stone.times(BigInteger.valueOf(2024))] = stoneCount
                    }
                }
                destructions[stone] = stoneCount
            }

            for ((stone, count) in creations) {
                stones[stone] = (stones[stone] ?: 0) + count
            }
            for ((stone, count) in destructions) {
                stones[stone] = (stones[stone] ?: 0) - count
                if (stones[stone] == 0L) {
                    stones.remove(stone)
                }
            }
        }
        return stones.values.sum()
    }
}
