
object Day15: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day15)

    override fun task1(input: Input): Any {
        return input.text().split(",").sumOf {
            hash(it)
        }
    }

    override fun task2(input: Input): Any {
        val steps = input.text().split(",")
        val boxes = (0..<256).map { Box(it) }
        for (step in steps) {
            when {
                step.contains('=') -> {
                    val (label, focalLength) = step.split("=")
                    val box = boxes[hash(label)]
                    val lens = Lens(label, focalLength.toInt())
                    box.add(lens)
                }
                step.contains('-') -> {
                    val label = step.removeSuffix("-")
                    val box = boxes[hash(label)]
                    box.remove(label)
                }
                else -> TODO()
            }
        }
        return boxes.sumOf { it.focusingPower() }
    }

    private fun hash(string: String): Int {
        var current = 0
        string.forEach { c ->
            current += c.code
            current *= 17
            current %= 256
        }
        return current
    }

    class Box(
        private val boxNumber: Int
    ) {
        private val lenses = mutableListOf<Lens>()

        fun add(lens: Lens) {
            val existing = lenses.indexOfFirst { it.label == lens.label }.takeIf { it != -1 }
            if (existing != null) {
                lenses[existing] = lens
            } else {
                lenses.add(lens)
            }
        }

        fun remove(label: String): Lens? {
            val existing = lenses.indexOfFirst { it.label == label }.takeIf { it != -1 }
            return existing?.let {
                lenses.removeAt(it)
            }
        }

        fun focusingPower(): Int {
            var total = 0
            val boxNumberMultiplier = (boxNumber + 1)
            lenses.forEachIndexed { index, lens ->
                val slotNumber = index + 1
                total += boxNumberMultiplier * slotNumber * lens.focalLength
            }
            return total
        }
    }


    data class Lens(
        val label: String,
        val focalLength: Int
    )
}
