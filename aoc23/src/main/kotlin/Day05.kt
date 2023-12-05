
class Day05: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day05())
    }

    override fun task1(input: Input): Any {
        val iter = input.lines.iterator()
        val seeds = iter.next().removePrefix("seeds: ").split(" ").map { it.toLong() }
        iter.next() // empty line
        val mappings = mutableListOf<Mapping>()
        while (iter.hasNext()) {
            mappings += Mapping.from(iter)
        }

        val locations = seeds.map { seed ->
            mappings.fold(seed) { acc, next -> next.translate(acc) }
        }

        return locations.min()
    }

    override fun task2(input: Input): Any {
        val iter = input.lines.iterator()
        val seedRanges = SeedRange.from(iter.next())
        iter.next() // empty line

        val mappings = mutableListOf<Mapping>()
        while (iter.hasNext()) {
            mappings += Mapping.from(iter)
        }

        var min = Long.MAX_VALUE
        for (range in seedRanges) {
            var ranges: List<LongRange> = listOf(range.range)
            for (mapping in mappings) {
                ranges = ranges.flatMap { mapping.translate(it) }
            }
            val minHere = ranges.minOf { it.first }
            min = minOf(min, minHere)
        }

        return min
    }

    data class Mapping(
        val identifier: String,
        val translations: List<Translation>
    ) {

        fun translate(value: Long): Long {
            return translations.firstNotNullOfOrNull { it.translateOrNull(value) } ?: value
        }

        fun translate(range: LongRange): List<LongRange> {
            val translatedRanges = translations
                .mapNotNull { it.translate(range) }
                .sortedBy { it.first.first }

            val result = mutableListOf<LongRange>()

            var current = range.first
            for ((from, to) in translatedRanges) {
                if (current < from.first) {
                    result += (current..<from.first)
                }
                result += to
                current = from.last + 1
            }
            if (current < range.last) {
                result += (current..range.last)
            }
            return result
        }

        companion object {
            fun from(lines: Iterator<String>): Mapping {
                val headline = lines.next()
                assert(headline.endsWith(" map:"))
                val translations = mutableListOf<Translation>()
                var line = lines.next()
                while (line.isNotEmpty()) {
                    val (dst, src, len) = line.split(" ").map { it.toLong() }
                    translations += Translation(src, dst, len)
                    if (!lines.hasNext()) {
                        break
                    }
                    line = lines.next()
                }
                return Mapping(
                    identifier = headline.removeSuffix(" map:"),
                    translations = translations.sortedBy { it.sourceStart }
                )
            }
        }
    }

    data class Translation(
        val sourceStart: Long,
        val destinationStart: Long,
        val length: Long,
    ) {
        private val offset = destinationStart - sourceStart
        private val translationRange: LongRange = (sourceStart..<(sourceStart + length))

        fun translateOrNull(src: Long): Long? {
            if (src in translationRange) {
                return src + offset
            }
            return null
        }

        fun translate(range: LongRange): Pair<LongRange, LongRange>? {
            val start = maxOf(range.first, sourceStart)
            val end = minOf(range.last, sourceStart + length - 1)
            if (end < start) return null
            return LongRange(start, end) to LongRange(
                start = start + offset,
                endInclusive = end + offset,
            )
        }
    }

    data class SeedRange(
        val range: LongRange
    ) {
        companion object {
            fun from(line: String): List<SeedRange> {
                val longs = line.removePrefix("seeds: ").split(" ").map { it.toLong() }
                return longs.chunked(2).map { (from, len) ->
                    SeedRange(from..<(from + len))
                }
            }
        }
    }

}
