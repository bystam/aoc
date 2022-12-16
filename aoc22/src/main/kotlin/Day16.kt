class Day16 : Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day16())
    }

    override val testInput: String = """
        Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        Valve BB has flow rate=13; tunnels lead to valves CC, AA
        Valve CC has flow rate=2; tunnels lead to valves DD, BB
        Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
        Valve EE has flow rate=3; tunnels lead to valves FF, DD
        Valve FF has flow rate=0; tunnels lead to valves EE, GG
        Valve GG has flow rate=0; tunnels lead to valves FF, HH
        Valve HH has flow rate=22; tunnel leads to valve GG
        Valve II has flow rate=0; tunnels lead to valves AA, JJ
        Valve JJ has flow rate=21; tunnel leads to valve II
    """.trimIndent()

    override fun task1(input: Input): Any {
        val state = Cave(parse(input))
        return state.maxPressureOnlyMe()
    }

    override fun task2(input: Input): Any {
        val state = Cave(parse(input))
        return state.maxPressureMeAndElephant()
    }

    private fun parse(input: Input): List<Valve> {
        return input
            .capture("Valve ([A-Z]+) has flow rate=(\\d+); tunnel[s]? lead[s]? to valve[s]? ([A-Z, ]+)")
            .map { (id, rate, tunnels) ->
                Valve(id, rate.toInt(), tunnels.split(", "))
            }
            .toList()
    }

    class Cave(
        valves: List<Valve>
    ) {
        private val valveById = valves.associateBy { it.id }

        private val maxOpened = valves.count { it.flowRate > 0 }

        private data class Params(
            val valveId: String,
            val opened: Set<String>,
            val timeLeft: Int
        )

        fun maxPressureOnlyMe(): Int = maxPressureAtParams(
            Params(
                valveId = "AA",
                opened = emptySet(),
                timeLeft = 30
            ),
            mutableMapOf()
        )

        private fun maxPressureAtParams(params: Params, cache: MutableMap<Params, Int>): Int {
            cache[params]?.let { return it }
            val (valveId, opened, timeLeft) = params
            if (timeLeft <= 1) return 0 // not enough time to open this valve
            val valve = valveById[valveId]!!
            val minutesOfPressureRelease = timeLeft - 1

            fun pressureIfOpening(): Int {
                val totalPressure = valve.flowRate * minutesOfPressureRelease
                return totalPressure + maxPressureAtParams(
                    Params(
                        valveId = valveId,
                        opened = opened.plus(valveId),
                        timeLeft = timeLeft - 1
                    ),
                    cache = cache
                )
            }

            fun pressureIfSkipping(): Int {
                return valve.tunnels.maxOf { neighbor ->
                    maxPressureAtParams(
                        Params(
                            valveId = neighbor,
                            opened = opened,
                            timeLeft = timeLeft - 1
                        ),
                        cache = cache
                    )
                }
            }

            val pressure = if (opened.contains(valveId))
                pressureIfSkipping()
            else if (valve.flowRate == 0)
                pressureIfSkipping()
            else
                Integer.max(pressureIfOpening(), pressureIfSkipping())
            cache[params] = pressure
            return pressure
        }

        private data class ElephantParams(
            val valveIds: Set<String>,
            val opened: Set<String>,
            val timeLeft: Int
        )

        sealed class Turn {
            class Opened(val valveId: String, val pressure: Int) : Turn()
            class Skipped(val nextValves: List<String>) : Turn()
        }

        fun maxPressureMeAndElephant(): Int = maxPressureAtElephantParams(
            ElephantParams(
                valveIds = setOf("AA"),
                opened = emptySet(),
                timeLeft = 26
            ),
            mutableMapOf()
        )

        private fun maxPressureAtElephantParams(params: ElephantParams, cache: MutableMap<ElephantParams, Int>): Int {
            cache[params]?.let { return it }
            if (params.opened.size == maxOpened) {
                return 0 // can't do more
            }
            val (valveIds, opened, timeLeft) = params
            if (timeLeft <= 1) return 0 // not enough time to open this valve
            val minutesOfPressureRelease = timeLeft - 1

            val ids = valveIds.toList()
            val aid = ids[0]
            val aValve = valveById[aid]!!
            val aTurn: Turn = if (opened.contains(aid) || aValve.flowRate == 0) {
                Turn.Skipped(aValve.tunnels)
            } else {
                Turn.Opened(aid, minutesOfPressureRelease * aValve.flowRate)
            }
            val bTurn: Turn = if (ids.size == 2) {
                val bid = ids[1]
                val bValve = valveById[bid]!!
                if (opened.contains(bid) || bValve.flowRate == 0) {
                    Turn.Skipped(bValve.tunnels)
                } else {
                    Turn.Opened(bid, minutesOfPressureRelease * bValve.flowRate)
                }
            } else {
                Turn.Skipped(aValve.tunnels)
            }

            val pressure = when (aTurn) {
                is Turn.Opened -> when (bTurn) {
                    is Turn.Opened -> aTurn.pressure + bTurn.pressure + maxPressureAtElephantParams(
                        ElephantParams(valveIds, opened.plus(aTurn.valveId).plus(bTurn.valveId), timeLeft - 1), cache
                    )

                    is Turn.Skipped -> aTurn.pressure + bTurn.nextValves.maxOf { bValve ->
                        maxPressureAtElephantParams(
                            ElephantParams(
                                setOf(aTurn.valveId, bValve),
                                opened.plus(aTurn.valveId),
                                timeLeft - 1
                            ), cache
                        )
                    }
                }

                is Turn.Skipped -> when (bTurn) {
                    is Turn.Opened -> bTurn.pressure + aTurn.nextValves.maxOf { aValve ->
                        maxPressureAtElephantParams(
                            ElephantParams(
                                setOf(bTurn.valveId, aValve),
                                opened.plus(bTurn.valveId),
                                timeLeft - 1
                            ), cache
                        )
                    }

                    is Turn.Skipped -> {
                        val uniqueChoices = aTurn.nextValves.flatMap { a ->
                            bTurn.nextValves.map { b -> setOf(a, b) }
                        }.toSet()
                        uniqueChoices.maxOf { newValveIds ->
                            maxPressureAtElephantParams(
                                ElephantParams(
                                    newValveIds,
                                    opened,
                                    timeLeft - 1
                                ), cache
                            )
                        }
                    }
                }
            }
            cache[params] = pressure
            return pressure
        }
    }

    data class Valve(
        val id: String,
        val flowRate: Int,
        val tunnels: List<String>
    )
}
