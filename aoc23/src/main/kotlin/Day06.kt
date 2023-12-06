
class Day06: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day06())
    }

    override fun task1(input: Input): Any {
        val (timeString, distanceString) = input.lines.toList()
        val times = timeString.removePrefix("Time:").splitIgnoreEmpty(" ").map { it.toLong() }
        val distances = distanceString.removePrefix("Distance:").splitIgnoreEmpty(" ").map { it.toLong() }
        val races = times.zip(distances).map { (time, distance) -> Race(time, distance) }

        return races.map { it.determineMargin() }.product()
    }

    override fun task2(input: Input): Any {
        val (timeString, distanceString) = input.lines.toList()
        val time = timeString.removePrefix("Time:").replace(" ", "").toLong()
        val distance = distanceString.removePrefix("Distance:").replace(" ", "").toLong()
        val race = Race(time, distance)

        return race.determineMargin()
    }

    data class Race(
        val time: Long,
        val record: Long
    ) {
        fun determineMargin(): Long {
            return (1..<time).count {
                distancePressing(it) > record
            }.toLong()
        }

        private fun distancePressing(pressTime: Long): Long = pressTime * (time - pressTime)
    }
}
