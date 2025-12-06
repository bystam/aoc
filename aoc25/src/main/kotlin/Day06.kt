import java.math.BigInteger

object Day06 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day06)

    override fun task1(input: Input): Any {
        val lines = input.lines.toList()
        val rows = lines.dropLast(1).map { line -> line.split(" ").filter { !it.isBlank() }.map { it.toInt() } }
        val ops = lines.last().split(" ").filter { !it.isBlank() }
        var total = BigInteger.ZERO
        for (col in rows.first().indices) {
            val operator = ops[col]

            var colTotal = rows[0][col].toBigInteger()

            for (row in (1..rows.lastIndex)) {
                when (operator) {
                    "+" -> colTotal += rows[row][col].toBigInteger()
                    "*" -> colTotal *= rows[row][col].toBigInteger()
                }
            }

            total += colTotal
        }
        return total
    }

    override fun task2(input: Input): Any {
        val lines = input.lines.toList()
        val rows = lines.dropLast(1)
        val ops = lines.last()

        val operatorIndices = ops.indices.filter { !ops[it].isWhitespace() }

        var total = BigInteger.ZERO
        for ((colStart, colEnd) in (operatorIndices + ops.length).windowed(2)) {
            val operator = ops[colStart]
            var colTotal = BigInteger.ZERO
            for (subColumn in colEnd-1 downTo colStart) {
                val string = StringBuilder()
                for (row in rows) {
                    if (row[subColumn].isDigit()) {
                        string.append(row[subColumn])
                    }
                }
                if (string.isEmpty()) continue
                val subcolValue = BigInteger(string.toString())
                if (colTotal == BigInteger.ZERO) {
                    colTotal = subcolValue
                } else {
                    when (operator) {
                        '+' -> colTotal += subcolValue
                        '*' -> colTotal *= subcolValue
                    }
                }
            }
            total += colTotal
        }

        return total
    }
}
