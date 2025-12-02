object Day02 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day02)

    override fun task1(input: Input): Any {
        fun isInvalidId(value: Long): Boolean {
            val string = value.toString()
            val len = string.length
            return (len % 2 == 0) && string.substring(0..<len / 2) == string.substring(len / 2 ..<len)
        }

        var sum = 0L
        for (range in input.text().split(",")) {
            val (start, end) = range.split("-")
            for (id in start.toLong()..end.toLong()) {
                if (isInvalidId(id)) {
                    sum += id
                }
            }
        }
        return sum
    }

    override fun task2(input: Input): Any {
        fun isInvalidId(value: Long): Boolean {
            val string = value.toString()
            val len = string.length
            for (size in 1..len/2) {
                if (string.chunked(size).toSet().size == 1) {
                    return true
                }
            }
            return false
        }

        var sum = 0L
        for (range in input.text().split(",")) {
            val (start, end) = range.split("-")
            for (id in start.toLong()..end.toLong()) {
                if (isInvalidId(id)) {
                    sum += id
                }
            }
        }
        return sum
    }
}
