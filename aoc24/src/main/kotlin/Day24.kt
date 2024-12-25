import java.math.BigInteger

object Day24 : Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day24)

    override fun task1(input: Input): Any {
        val wires = input.lines
            .takeWhile { it.isNotEmpty() }
            .associate { line ->
                val (name, charge) = line.split(": ")
                name to (charge == "1")
            }
            .toMutableMap()
        val gates = input.lines.toList().reversed()
            .takeWhile { it.isNotEmpty() }
            .map { line ->
                val (inputs, output) = line.split(" -> ")
                val (input1, operator, input2) = inputs.split(" ")
                Gate(input1 = input1, input2 = input2, output = output, operator = operator)
            }

        val unhandledGates = gates.toMutableList()
        while (unhandledGates.isNotEmpty()) {
            val next = unhandledGates.first {
                it.input1 in wires && it.input2 in wires
            }
            wires[next.output] = next.produce(wires[next.input1]!!, wires[next.input2]!!)
            unhandledGates.remove(next)
        }
        val numberAsBinary = wires.keys
            .filter { it.startsWith("z") }
            .sortedDescending()
            .joinToString("") { if (wires[it] == true) "1" else "0" }

        return BigInteger(numberAsBinary, 2)
    }

    override fun task2(input: Input): Any {
        val gates = input.lines.toList().reversed()
            .takeWhile { it.isNotEmpty() }
            .map { line ->
                val (inputs, output) = line.split(" -> ")
                val (input1, operator, input2) = inputs.split(" ")
                Gate(input1 = input1, input2 = input2, output = output, operator = operator)
            }
        val faulty = findGatesThatViolateRippleCarryAdderRules(gates)
        return faulty.map { it.output }.sorted().joinToString(",")
    }

    private fun findGatesThatViolateRippleCarryAdderRules(gates: List<Gate>): List<Gate> {
        val gatesByInput = mutableMapOf<String, MutableList<Gate>>()
        for (gate in gates) {
            gatesByInput.getOrPut(gate.input1, { mutableListOf() }).add(gate)
            gatesByInput.getOrPut(gate.input2, { mutableListOf() }).add(gate)
        }

        val maxZ = gates.filter { it.output.startsWith("z") }.maxOf { it.output }

        // The following are apparently all wires that violate the rules of "Ripple Carry Adders"
        // https://en.wikipedia.org/wiki/Adder_(electronics)#Ripple-carry_adder

        val nonXOREndings = gates.filter { g ->
            g.isEndOutput && g.output != maxZ && g.operator != "XOR"
        }

        val xorsInTheMiddle = gates.filter { g ->
            !g.isStartInput && !g.isEndOutput && g.operator == "XOR"
        }

        val startingXORsThatDontGoToXOR = gates.filter { g ->
            g.isStartInput && !g.input1.endsWith("00") && !g.input2.endsWith("00") &&
                    g.operator == "XOR" && gatesByInput[g.output]?.any { it.operator == "XOR" } == false
        }

        val startingANDsThatDontGoToOR = gates.filter { g ->
            g.isStartInput && !g.input1.endsWith("00") && !g.input2.endsWith("00") &&
                    g.operator == "AND" && gatesByInput[g.output]?.any { it.operator == "OR" } == false
        }

        return nonXOREndings + xorsInTheMiddle + startingXORsThatDontGoToXOR + startingANDsThatDontGoToOR
    }

    data class Gate(
        val input1: String,
        val input2: String,
        val output: String,
        val operator: String,
    ) {
        val isStartInput: Boolean
            get() = (input1.startsWith("x") || input2.startsWith("x")) &&
                    (input2.startsWith("x") || input2.startsWith("y"))

        val isEndOutput: Boolean get() = output.startsWith("z")

        fun produce(a: Boolean, b: Boolean): Boolean = when (operator) {
            "AND" -> a && b
            "OR" -> a || b
            "XOR" -> a xor b
            else -> TODO()
        }
    }
}
