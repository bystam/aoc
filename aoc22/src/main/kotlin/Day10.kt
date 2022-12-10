import kotlin.math.sign

class Day10: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day10())
    }

    override fun task1(input: Input): Any {
        var cycle = 1
        var x = 1
        var sum = 0
        val triggerPoints = listOf(20, 60, 100, 140, 180, 220)
        fun signalStrength(): Int = cycle * x
        fun step() {
            cycle += 1
            if (cycle in triggerPoints) {
                sum += signalStrength()
            }
        }

        input.lines.forEach { line ->
            val parts = line.split(" ")
            when (parts.first()) {
                "noop" -> step()
                "addx" -> {
                    step()
                    step()
                    x += parts.last().toInt()
                }
            }
        }

        return sum
    }

    override fun task2(input: Input): Any {
        var cycle = 1
        var x = 1
        val pixels = mutableListOf<Char>()
        fun step() {
            val column = ((cycle-1) % 40)
            if (column in (x-1..x+1)) {
                pixels.add('#')
            } else {
                pixels.add('.')
            }
            cycle += 1
        }

        val foreverish = input.lines + input.lines + input.lines + input.lines + input.lines + input.lines

        for (line in foreverish) {
            val parts = line.split(" ")
            when (parts.first()) {
                "noop" -> step()
                "addx" -> {
                    step()
                    step()
                    x += parts.last().toInt()
                }
            }
            if (pixels.size >= 240)
                break
        }
        return "\n" + pixels.chunked(40).joinToString("\n") { String(it.toCharArray()) }
    }
}
