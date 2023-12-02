
class Day02: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day02())
    }

    override fun task1(input: Input): Any {
        val limit = Cubes(red = 12, green = 13, blue = 14)
        return input.lines
            .map { Game.from(it) }
            .filter { game -> game.rounds.all { it.possibleWithin(limit) } }
            .sumOf { it.id }
    }

    override fun task2(input: Input): Any {
        return input.lines
            .map { Game.from(it) }
            .sumOf { it.minimalCubes().power() }
    }

    data class Game(
        val id: Int,
        val rounds: List<Cubes>
    ) {

        fun minimalCubes(): Cubes {
            val red = rounds.maxOf { it.red }
            val green = rounds.maxOf { it.green }
            val blue = rounds.maxOf { it.blue }
            return Cubes(red, green, blue)
        }

        companion object {
            fun from(string: String): Game {
                val (prefix, content) = string.split(":")
                val id = prefix.removePrefix("Game ").toInt()
                val rounds = content.split(";").map { round ->
                    val cubes = round.split(",")
                    val red = cubes.countColor("red")
                    val green = cubes.countColor(" green")
                    val blue = cubes.countColor(" blue")
                    Cubes(red, green, blue)
                }
                return Game(id, rounds)
            }

            private fun List<String>.countColor(color: String): Int = this.find { it.endsWith(color) }
                ?.removeSuffix(color)?.trim()?.toInt() ?: 0
        }
    }

    data class Cubes(
        val red: Int,
        val green: Int,
        val blue: Int,
    ) {
        fun possibleWithin(limit: Cubes): Boolean = red <= limit.red && green <= limit.green && blue <= limit.blue

        fun power(): Int = red * green * blue
    }
}
