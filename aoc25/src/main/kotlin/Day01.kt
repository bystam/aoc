import kotlin.math.absoluteValue

class Day01: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day01())
    }

    override fun task1(input: Input): Any {
        var dial = 50
        var count = 0
        for (line in input.lines) {
            when (line.first()) {
                'L' -> dial -= line.drop(1).toInt()
                'R' -> dial += line.drop(1).toInt()
            }
            dial = dial.mod(100)

            if (dial == 0) {
                count += 1
            }
        }
        return count
    }

    override fun task2(input: Input): Any {
        var dial = 50
        var count = 0
        for (line in input.lines) {
            val direction = line.first()
            val clicks = line.drop(1).toInt()
            repeat(clicks) {
                when (direction) {
                    'L' -> dial -= 1
                    'R' -> dial += 1
                }
                dial = dial.mod(100)
                if (dial == 0) {
                    count += 1
                }
            }
        }
        return count
    }
}
