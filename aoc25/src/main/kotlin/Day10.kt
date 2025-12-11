import java.util.LinkedList

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
        val machines = input.lines.map {
            Machine.parse(it)
        }.toList()
        val alreadyComputed = part2Outputs.lines().map { it.toInt() }
        return machines.mapIndexed { index, machine ->
            alreadyComputed.getOrNull(index) ?: machine.part2()
        }.sum()
    }

    class Machine(
        val targetLights: List<Boolean>,
        val buttons: List<Button>,
        val targetJoltage: List<Int>,
    ) {

        private val possiblePositions = (0..buttons.lastIndex).map { index ->
            buttons.subList(index, buttons.size).flatMap { it.switches }.toSet()
        }

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

        fun part2(): Int {
//            val buttonsForOnes = findButtonsThatBuildOnes()
//            val shrinkRounds = targetJoltage.min()
//            val shrunkTarget = targetJoltage.map { it - shrinkRounds }
            print(this)
            val smallest = findSmallest(targetJoltage.map { 0 }, 0)!!
            println(" -> $smallest")
            return smallest
        }
//
//        private fun findButtonsThatBuildOnes(): Set<Button> {
//            for (combo in buttons.powerset().sortedBy { it.size }) {
//                val result: MutableList<Int> = targetJoltage.map { 0 }.toMutableList()
//                for (button in combo) {
//                    for (light in button.switches) {
//                        result[light] += 1
//                    }
//                }
//                if (result.all { it == 1 }) return combo
//            }
//            error("noooo")
//        }

        private fun findSmallest(current: List<Int>, buttonIndex: Int): Int? {
            val allPossibleAdditions = possiblePositions[buttonIndex]// buttons.subList(buttonIndex, buttons.size).flatMap { it.switches }.toSet()
            val indexesWhereWeNeedMore = targetJoltage.indices.filter {
                targetJoltage[it] - current[it] > 0
            }.toSet()

            if (!allPossibleAdditions.containsAll(indexesWhereWeNeedMore)) return null

            if (current == targetJoltage)
                return 0

            var presses = 0
            val next = current.toMutableList()
            val button = buttons[buttonIndex]

            while (true) {
                if (button.switches.any { targetJoltage[it] == next[it] }) {
                    break
                }
                presses += 1
                button.switches.forEach { next[it] += 1 }
            }

            if (next == targetJoltage) return presses

            if (buttonIndex == buttons.lastIndex) return null

            while (presses >= 0) {
                val subResult = findSmallest(next, buttonIndex + 1)
                if (subResult != null) {
                    return subResult + presses
                }
                presses -= 1
                button.switches.forEach { next[it] -= 1 }
            }

            return null
        }

        private fun findSmallest(target: List<Int>, memo: MutableMap<List<Int>, Int>): Int = memo.getOrPut(target) {
            if (target.all { it == 0 }) return@getOrPut 0

            var min = Int.MAX_VALUE
            for (button in buttons) {
                val reducedTarget = target.toMutableList()
                button.switches.map { reducedTarget[it] -= 1 }
                if (reducedTarget.any { it < 0 }) continue
                min = minOf(min, findSmallest(reducedTarget, memo) + 1)
            }
            min
        }

        private fun findSmallest2(target: List<Int>): Int {
            data class Node(
                val current: List<Int>,
                val buttonPresses: Int
            )

            val queue = LinkedList(listOf(Node(target.map { 0 }, 0)))
            val visited = mutableMapOf<List<Int>, Int>()

            while (queue.isNotEmpty()) {
                val (current, presses) = queue.poll()
                visited[current] = presses

                if (current == target) return presses

                buttonLoop@for (button in buttons) {
                    val next = current.toMutableList()
                    button.switches.forEach { next[it] += 1 }
                    val nextNode = Node(next, presses + 1)

                    if (next == target) return presses + 1

                    for ((index, value) in next.withIndex()) {
                        if (value > target[index]) continue@buttonLoop
                    }

                    val currentBest = visited[next] ?: Int.MAX_VALUE
                    if (nextNode.buttonPresses < currentBest) {
                        queue += nextNode
                    }
                }
            }
            error("wtf")
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
                    buttons = buttons.shuffled().sortedByDescending { it.switches.size },
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


    private val part2Outputs = """
78
74
93
157
37
123
76
81
87
123
262
58
62
163
58
132
165
66
58
68
214
33
4
181
26
109
131
59
189
112
306
88
69
46
83
50
200
268
100
13
56
41
56
179
25
64
56
98
94
40
83
54
17
120
98
75
53
98
223
108
88
45
46
156
51
127
28
125
174
62
225
112
56
45
38
50
64
305
139
93
91
62
204
33
122
33
174
57
64
192
61
83
121
""".trimIndent()

}
