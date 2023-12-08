import java.math.BigInteger

class Day08: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day08())
    }

    override fun task1(input: Input): Any {
        val instructions = input.lines.first()
        val nodes = input.lines.drop(2).map { Node.from(it) }.associateBy { it.id }
        val walk = Walk(instructions, nodes, "AAA")

        walk.stepUntil { it.id == "ZZZ" }

        return walk.steps
    }

    override fun task2(input: Input): Any {
        val instructions = input.lines.first()
        val nodes = input.lines.drop(2).map { Node.from(it) }.associateBy { it.id }
        val walks = nodes.values
            .filter { it.id.last() == 'A' }
            .map {
                Walk(instructions, nodes, it.id)
            }
        for (walk in walks) {
            walk.stepUntil { it.id.last() == 'Z' }
        }

        return lcm(walks.map { BigInteger.valueOf(it.steps.toLong()) })
    }

    private fun lcm(values: List<BigInteger>): BigInteger {
        if (values.isEmpty()) return BigInteger.ONE
        var lcm = values[0]
        for (i in 1 until values.size) {
            if (values[i].signum() != 0) {
                val gcd = lcm.gcd(values[i])
                if (gcd == BigInteger.ONE) {
                    lcm = lcm.multiply(values[i])
                } else {
                    if (values[i] != gcd) {
                        lcm = lcm.multiply(values[i].divide(gcd))
                    }
                }
            }
        }
        return lcm
    }


    class Walk(
        val instructions: String,
        val nodes: Map<String, Node>,
        val startingNodeId: String
    ) {
        var steps: Int = 0
        var currentNode: Node = nodes[startingNodeId]!!

        fun stepUntil(test: (Node) -> Boolean) {
            while (!test(currentNode)) {
                step()
            }
        }

        private fun step() {
            val instruction = instructions[steps % instructions.length]
            steps += 1
            val nextId = when (instruction) {
                'L' -> currentNode.left
                'R' -> currentNode.right
                else -> TODO()
            }
            currentNode = nodes[nextId]!!
        }
    }

    data class Node(
        val id: String,
        val left: String,
        val right: String
    ) {

        companion object {
            private val regex = Regex("([A-Z]{3}) = \\(([A-Z]{3}), ([A-Z]{3})\\)")
            fun from(line: String): Node {
                val (id, left , right) = regex.matchEntire(line)!!.destructured
                return Node(id, left, right)
            }
        }
    }
}
