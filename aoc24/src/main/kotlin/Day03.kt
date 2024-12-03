object Day03 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day03)

    private val regex: Regex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")

    override fun task1(input: Input): Any {
        return regex.findAll(input.text()).sumOf {
            val (a, b) = it.destructured
            a.toInt() * b.toInt()
        }
    }

    override fun task2(input: Input): Any {
        val dos = input.text().split("do()")
            .joinToString("") {
                it.split("don't()")[0]
            }
        return regex.findAll(dos).sumOf {
            val (a, b) = it.destructured
            a.toInt() * b.toInt()
        }
    }
}
