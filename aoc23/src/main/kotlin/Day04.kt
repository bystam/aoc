import kotlin.math.pow

class Day04: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day04())
    }

    override fun task1(input: Input): Any {
        val cards = input.lines.map { ScratchCard.from(it) }
        return cards.sumOf {
            it.score()
        }
    }

    override fun task2(input: Input): Any {
        val collection = ScratchCardCollection(input.lines.map { ScratchCard.from(it) })
        for (id in collection.idSpan) {
            collection.playCard(id)
        }
        return collection.totalCards()
    }

    class ScratchCardCollection(
        list: Sequence<ScratchCard>
    ) {
        val cardById: Map<Int, ScratchCard> = list.associateBy { it.id }
        val counts: MutableList<Int> = cardById.keys.map { 1 }.toMutableList()
        val idSpan: IntRange = (cardById.keys.min().. cardById.keys.max())

        fun playCard(id: Int) {
            val card = cardById[id]!!
            val winnerCount = card.having.intersect(card.winning).size
            val currentCardCount = counts[card.id - 1]
            (1..winnerCount).forEach { offset ->
                counts[card.id - 1 + offset] += currentCardCount
            }
        }

        fun totalCards(): Int = counts.sum()
    }

    data class ScratchCard(
        val id: Int,
        val winning: Set<Int>,
        val having: Set<Int>
    ) {

        fun score(): Int {
            val winners = having.intersect(winning)
            if (winners.isEmpty()) return 0
            return 2.0.pow(winners.size - 1).toInt()
        }

        companion object {
            fun from(string: String): ScratchCard {
                val (prefix, suffix) = string.split(": ")
                val id = prefix.removePrefix("Card").trim().toInt()
                val (winning, having) = suffix.split("|")
                return ScratchCard(
                    id = id,
                    winning = winning.splitIgnoreEmpty(" ").map { it.toInt() }.toSet(),
                    having = having.splitIgnoreEmpty(" ").map { it.toInt() }.toSet(),
                )
            }
        }
    }
}
