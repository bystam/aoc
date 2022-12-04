
class Day4: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day4())
    }

    override fun task1(input: Input): Any {
        return input.capture("(\\d+)-(\\d+),(\\d+)-(\\d+)")
            .map { (f1, t1, f2, t2) ->
                (f1.toInt()..t1.toInt()) to (f2.toInt()..t2.toInt())
            }
            .count { (span1, span2) ->
                span1.contains(span2.first) && span1.contains(span2.last) ||
                        span2.contains(span1.first) && span2.contains(span1.last)
            }
    }

    override fun task2(input: Input): Any {
        return input.capture("(\\d+)-(\\d+),(\\d+)-(\\d+)")
            .map { (f1, t1, f2, t2) ->
                (f1.toInt()..t1.toInt()) to (f2.toInt()..t2.toInt())
            }
            .count { (span1, span2) ->
                span1.contains(span2.first) || span1.contains(span2.last) ||
                        span2.contains(span1.first) || span2.contains(span1.last)
            }
    }
}
