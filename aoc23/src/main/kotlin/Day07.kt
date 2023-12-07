
class Day07: Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day07())
    }

    override val testInput: String? = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent()

    override fun task1(input: Input): Any {
        val hands = input.lines.map { Hand.from(it) }.toList()

        return hands.sortedWith(FirstComparator.reversed()).zip(1..hands.size).sumOf { (hand, rank) ->
            hand.bet * rank
        }
    }

    override fun task2(input: Input): Any {
        val hands = input.lines.map { Hand.from(it) }.toList()

        return hands.sortedWith(SecondComparator.reversed()).zip(1..hands.size).sumOf { (hand, rank) ->
            hand.bet * rank
        }
    }

    data class Hand(
        val cards: List<Card>,
        val bet: Int
    ) {

        companion object {


            fun from(line: String): Hand {
                val (cards, bet) = line.split(" ")
                return Hand(
                    cards = cards.map { Card(it) },
                    bet = bet.toInt()
                )
            }
        }
    }

    enum class HandType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD,
    }

    data class Card(
        val value: Char
    )

    object FirstComparator: Comparator<Hand> {

        private val priority: List<Char> = listOf(
            'A',
            'K',
            'Q',
            'J',
            'T',
            '9',
            '8',
            '7',
            '6',
            '5',
            '4',
            '3',
            '2',
        )

        override fun compare(a: Hand, b: Hand): Int {
            val aType = a.getType()
            val bType = b.getType()
            if (aType != bType)
                return aType.compareTo(bType)

            a.cards.zip(b.cards).forEach { (aCard, bCard) ->
                val aIndex = priority.indexOf(aCard.value)
                val bIndex = priority.indexOf(bCard.value)
                if (aIndex != bIndex)
                    return aIndex.compareTo(bIndex)
            }

            TODO("WTF")
        }

        private fun Hand.getType(): HandType {
            val sameCounts = getSameCounts()
            return when (sameCounts.size) {
                1 -> when (sameCounts.single()) {
                    5 -> HandType.FIVE_OF_A_KIND
                    4 -> HandType.FOUR_OF_A_KIND
                    3 -> HandType.THREE_OF_A_KIND
                    2 -> HandType.ONE_PAIR
                    else -> TODO(toString())
                }
                2 -> when {
                    sameCounts == listOf(3, 2) -> HandType.FULL_HOUSE
                    sameCounts == listOf(2, 2) -> HandType.TWO_PAIR
                    else -> TODO(toString())
                }
                else -> HandType.HIGH_CARD
            }
        }

        private fun Hand.getSameCounts(): List<Int> {
            val sames = mutableMapOf<Card, Int>()
            cards.forEach {
                sames[it] = (sames[it] ?: 0) + 1
            }
            return sames.values.filter { it > 1 }.sortedDescending()
        }
    }


    object SecondComparator: Comparator<Hand> {

        private val priority: List<Char> = listOf(
            'A',
            'K',
            'Q',
            'T',
            '9',
            '8',
            '7',
            '6',
            '5',
            '4',
            '3',
            '2',
            'J',
        )

        override fun compare(a: Hand, b: Hand): Int {
            val aType = a.getType()
            val bType = b.getType()
            if (aType != bType)
                return aType.compareTo(bType)

            a.cards.zip(b.cards).forEach { (aCard, bCard) ->
                val aIndex = priority.indexOf(aCard.value)
                val bIndex = priority.indexOf(bCard.value)
                if (aIndex != bIndex)
                    return aIndex.compareTo(bIndex)
            }

            TODO("WTF")
        }

        private fun Hand.getType(): HandType {
            val sameCounts = getSameCounts()
            return when (sameCounts.size) {
                1 -> when (sameCounts.single()) {
                    5 -> HandType.FIVE_OF_A_KIND
                    4 -> HandType.FOUR_OF_A_KIND
                    3 -> HandType.THREE_OF_A_KIND
                    2 -> HandType.ONE_PAIR
                    else -> TODO(toString())
                }
                2 -> when {
                    sameCounts == listOf(3, 2) -> HandType.FULL_HOUSE
                    sameCounts == listOf(2, 2) -> HandType.TWO_PAIR
                    else -> TODO(toString())
                }
                else -> HandType.HIGH_CARD
            }
        }

        private fun Hand.getSameCounts(): List<Int> {
            val sames = mutableMapOf<Card, Int>()
            cards.filter { it.value != 'J' }.forEach {
                sames[it] = (sames[it] ?: 0) + 1
            }
            val jokers = cards.count { it.value == 'J' }
            val bestCards = sames.values.sortedDescending().toMutableList()
            if (bestCards.isEmpty()) {
                bestCards.add(jokers)
            } else {
                bestCards[0] += jokers
            }
            return bestCards.filter { it > 1 }
        }
    }
}
