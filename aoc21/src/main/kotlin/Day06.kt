
fun main(args: Array<String>) = solve(Day06())

class Day06: Day(6) {

    override val testInput: String?
        get() = "3,4,3,1,2"

    override fun task1(input: Input): Any {
        val state = (0..8).map { 0L }.toMutableList()
        input.lines.single().split(",").map { it.toInt() }.forEach {
            state[it]++
        }

        repeat(80) { simulateDay(state) }

        return state.sum()
    }

    override fun task2(input: Input): Any {
        val state = (0..8).map { 0L }.toMutableList()
        input.lines.single().split(",").map { it.toInt() }.forEach {
            state[it]++
        }

        repeat(256) { simulateDay(state) }

        return state.sum()
    }

    private fun simulateDay(state: MutableList<Long>) {
        val births = state[0]
        state.indices.drop(1).forEach {
            state[it-1] = state[it]
        }
        state[6] += births
        state[8] = births
    }
}