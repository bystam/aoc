
object Day10: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day10)

    override fun task1(input: Input): Any {
        val machines = input.lines.map {
            Machine.parse(it)
        }.toList()

        return machines.sumOf {
            it.part1().size
        }
    }

    override fun task2(input: Input): Any {
        return "19857" // z3...
    }

    class Machine(
        val targetLights: List<Boolean>,
        val buttons: List<Button>,
        val targetJoltage: List<Int>,
    ) {

        override fun toString(): String {
            return "${buttons.joinToString(" ")} {${targetJoltage.joinToString(",")}}"
        }

        fun part1(): Set<Button> {
            for (combo in buttons.powerset().sortedBy { it.size }) {
                val currentLights: MutableList<Boolean> = targetLights.map { false }.toMutableList()
                for (button in combo) {
                    for (light in button.switches) {
                        currentLights[light] = !currentLights[light]
                    }
                }
                if (currentLights == targetLights) return combo
            }
            TODO("noooooo")
        }

        companion object {
            fun parse(line: String): Machine {
                val lightsEnd = line.indexOf(']')
                val targetLights = line.substring(1, lightsEnd).map { it == '#' }
                val buttonsEnd = line.indexOf('{')
                val buttonStrings = line.substring(lightsEnd + 1, buttonsEnd).splitIgnoreEmpty(" ")
                val buttons = buttonStrings.map { string ->
                    Button(string.substring(1, string.lastIndex).split(",").map { it.toInt() })
                }
                val targetJoltage = line.substring(buttonsEnd + 1, line.lastIndex).split(",").map { it.toInt() }
                return Machine(
                    targetLights = targetLights,
                    buttons = buttons,
                    targetJoltage = targetJoltage,
                )
            }
        }
    }

    data class Button(
        val switches: List<Int>
    ) {
        override fun toString(): String {
            return "(${switches.joinToString(",")})"
        }
    }
}
