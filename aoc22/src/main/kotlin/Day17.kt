import java.lang.StringBuilder

class Day17: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day17())
    }

    override val testInput: String = """
        >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
    """.trimIndent()

    override fun task1(input: Input): Any {
        val cave = Cave(input.text())

        while (true) {
            cave.step()
            if (cave.restingRocks == 2022) {
                break
            }
        }
        return cave.maxHeight
    }

    override fun task2(input: Input): Any {
        val cave = Cave(input.text())

        val start: Int
        val width: Int
        while (true) {
            cave.step()
            if (cave.restingRocks > 5000) {
                val w = cave.detectCycle()
                if (w != null) {
                    val s = cave.detectStartOfCycle(w)
                    start = s
                    width = w
                    break
                }
            }
        }
        val cycleHeights = cave.heightIncreases.subList(start, start + width)
        val preCycle = cave.heightIncreases.take(start).sum()
        val repeats = (1000000000000L - start) / width
        val tail = (1000000000000L - start) % width

        val repeatsTotal = repeats * cycleHeights.sum()
        val tailTotal = cycleHeights.take(tail.toInt()).sum()
        return preCycle + repeatsTotal + tailTotal
    }

    class Cave(
        val jetPattern: String
    ) {
        val occupied: MutableSet<Point2D> = mutableSetOf()
        var maxHeight: Int = 0
        var nextRockTypeOrdinal: Int = 0
        var restingRocks: Int = 0
        var currentRock: Rock? = null
        var nextEffect: Effect = Effect.Jet(0, jetPattern)
        var heightIncreases = mutableListOf<Int>()

        fun step() {
            var rock = currentRock
            if (rock == null) {
                val type = RockType.values()[nextRockTypeOrdinal]
                nextRockTypeOrdinal = (nextRockTypeOrdinal + 1) % RockType.values().size
                val verticalStart = maxHeight + 3
                val horizontalStart = 2
                rock = Rock(
                    position = Point2D(horizontalStart, verticalStart),
                    type = type
                )
                currentRock = rock
                return
            }

            when (val effect = nextEffect) {
                is Effect.Jet -> {
                    val jet = effect.jet
                    val direction = Vec2D(dx = if (jet == '<') -1 else 1)
                    currentRock = attemptMoveRock(rock, direction) ?: rock
                }
                is Effect.Fall -> {
                    val direction = Vec2D(dy = -1)
                    val newRock = attemptMoveRock(rock, direction)
                    currentRock = if (newRock == null) {
                        // rock is resting
                        occupied.addAll(rock.occupyingSpaces)
                        val currentHeight = maxHeight
                        maxHeight = Integer.max(maxHeight, rock.topY + 1)
                        heightIncreases.add(maxHeight - currentHeight)
                        restingRocks += 1
                        null
                    } else {
                        newRock
                    }
                }
            }

            nextEffect = nextEffect.next()
        }

        private fun attemptMoveRock(rock: Rock, direction: Vec2D): Rock? {
            val newRock = rock.copy(
                position = rock.position.offset(direction)
            )
            val spaces = newRock.occupyingSpaces
            val blocked = spaces.any {
                it.y < 0 || it.x < 0 || it.x > 6 || occupied.contains(it)
            }
            return newRock.takeUnless { blocked }
        }

        fun detectCycle(): Int? {
            val maxPossibleCycleWidth = heightIncreases.size / 2
            (maxPossibleCycleWidth downTo 20).forEach { width ->
                val possibleCycle = heightIncreases.takeLast(width)
                val valuesBefore = heightIncreases.takeLast(width * 2).take(width)
                if (possibleCycle == valuesBefore) {
                    return width
                }
            }
            return null
        }

        fun detectStartOfCycle(width: Int): Int {
            heightIncreases.indices.forEach { start ->
                val test = heightIncreases.subList(start, start + width)
                val next = heightIncreases.subList(start + width, start + width + width)
                if (test == next) return start
            }
            TODO("WTF")
        }
    }

    sealed interface Effect {

        fun next(): Effect

        data class Jet(val offset: Int, val pattern: String) : Effect {

            val jet: Char = pattern[offset]

            override fun next(): Effect = Fall(offset, pattern)
        }
        data class Fall(val offset: Int, val pattern: String) : Effect {
            override fun next(): Effect = Jet((offset + 1) % pattern.length, pattern)
        }
    }

    data class Rock(
        val position: Point2D,
        val type: RockType
    ) {
        val occupyingSpaces: List<Point2D> get() = type.spaces.map { position.offset(it) }
        val topY: Int get() = occupyingSpaces.maxOf { it.y }
    }

    enum class RockType(
        val spaces: List<Vec2D>
    ) {
        HLINE(
            (0..3).map { Vec2D(dx = it) }
        ),
        PLUS(
            listOf(
                Vec2D(dx = 1, dy = 2), // up
                Vec2D(dx = 0, dy = 1), // left
                Vec2D(dx = 1, dy = 1), // center
                Vec2D(dx = 2, dy = 1), // right
                Vec2D(dx = 1, dy = 0), // bottom
            )
        ),
        L(
            (0..2).map { Vec2D(dx = it, dy = 0) } + (1..2).map { Vec2D(dx = 2, dy = it) }
        ),
        VLINE(
            (0..3).map { Vec2D(dy = it) }
        ),
        SQUARE(
            (0..1).flatMap { dx -> (0..1).map { dy -> Vec2D(dx, dy) } }
        );
    }
}
