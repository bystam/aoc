
fun main(args: Array<String>) = solve(Day1())

class Day1: Day(1) {
    override fun task1(input: Input): Any {
        return elves(input).max()
    }

    override fun task2(input: Input): Any {
        return elves(input).sorted()
            .takeLast(3)
            .sum()
    }

    private fun elves(input: Input): List<Int> {
        val elves = mutableListOf<Int>()
        var elf = 0
        for (line in input.lines) {
            if (line.isEmpty()) {
                elves.add(elf)
                elf = 0
            } else {
                elf += line.toInt()
            }
        }
        return elves
    }
}
