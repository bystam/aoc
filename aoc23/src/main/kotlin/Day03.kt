
class Day03: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day03())
    }

    override fun task1(input: Input): Any {
        val schematic = Schematic(Grid2D.charMatrix(input.lines))

        val allNumbers = schematic.detectNumbers()
        val partNumbers = mutableSetOf<Number>()

        schematic.allSymbolAdjacentPoints().forEach { point ->
            allNumbers[point]?.let {
                partNumbers += it
            }
        }

        return partNumbers.sumOf { schematic.read(it) }
    }

    override fun task2(input: Input): Any {
        val schematic = Schematic(Grid2D.charMatrix(input.lines))

        val allNumbers = schematic.detectNumbers()
        val gearRatios = mutableListOf<Int>()

        schematic.allGearPoints().forEach { point ->
            val neighbors = schematic.chars.neighborsIncludingDiagonal(point)
            val partNumbers = neighbors.mapNotNull { allNumbers[it.point] }.toSet()
            if (partNumbers.size == 2) {
                val (a, b) = partNumbers.toList()
                gearRatios += schematic.read(a) * schematic.read(b)
            }
        }

        return gearRatios.sum()
    }

    data class Schematic(
        val chars: Grid2D<Char>
    ) {
        fun detectNumbers(): Map<Point2D, Number> {
            val numbers = mutableListOf<Number>()
            for (y in (0..<chars.height)) {
                var numberStart: Int? = null
                for (x in (0..<chars.width)) {
                    val char = chars[x, y]

                    if (numberStart == null && char.isDigit()) {
                        numberStart = x
                    }
                    if (numberStart != null && !char.isDigit()) {
                        numbers += Number(y, numberStart..< x)
                        numberStart = null
                    }
                }
                if (numberStart != null) {
                    numbers += Number(y, numberStart..< chars.width)
                }
            }
            val result = mutableMapOf<Point2D, Number>()
            numbers.forEach { num ->
                num.points().forEach {
                    result[it] = num
                }
            }
            return result
        }

        fun allSymbolAdjacentPoints(): Sequence<Point2D> {
            return chars.allPoints()
                .filter { !chars[it].isDigit() && chars[it] != '.' }
                .flatMap { point ->
                    chars.neighborsIncludingDiagonal(point).map { it.point }
                }
        }

        fun allGearPoints(): Sequence<Point2D> {
            return chars.allPoints()
                .filter { chars[it] == '*' }
        }

        fun read(number: Number): Int {
            var string = ""
            number.cols.forEach { x ->
                string += chars[x, number.row]
            }
            return string.toInt()
        }
    }

    data class Number(
        val row: Int,
        val cols: IntRange
    ) {
        fun overlaps(point: Point2D): Boolean {
            return row == point.y && cols.contains(point.x)
        }

        fun points(): List<Point2D> = cols.map { Point2D(it, row) }
    }
}
