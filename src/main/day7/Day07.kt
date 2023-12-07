package main.day7

import main.println
import main.readInput
import java.lang.Exception
import java.lang.IllegalArgumentException

enum class Type(val value: Int) {
    FIVE_OF_KIND(7), FOUR_OF_KIND(6), FULL_HOUSE(5), THREE_OF_KIND(4), TWO_PAIR(3), ONE_PAIR(2), HIGH_CARD(1)
}

data class CamelCard(val cards: List<String>, val bid: Long = 0) {
    private val cardToValue: MutableMap<String, Int> = mutableMapOf("A" to 50,
            "K" to 40, "Q" to 30, "J" to 20, "T" to 10)
    private val cardToValueWildCard: MutableMap<String, Int> = mutableMapOf("A" to 50,
            "K" to 40, "Q" to 30, "T" to 10, "J" to 1)
    private val count = cards.groupingBy { it }.eachCount()
    private fun rank(): Type {
        val uniqueCards = count.keys.toList().size
        if (uniqueCards == 5) return Type.HIGH_CARD
        if (uniqueCards == 1) return Type.FIVE_OF_KIND
        if (uniqueCards == 2 && count.values.any { it == 4 }) return Type.FOUR_OF_KIND
        if (uniqueCards == 2 && count.values.any { it == 3 }) return Type.FULL_HOUSE
        if (uniqueCards == 3 && count.values.any { it == 3 }) return Type.THREE_OF_KIND
        if (count.values.count { it == 2 } == 2) return Type.TWO_PAIR
        if (count.values.count { it == 2 } == 1) return Type.ONE_PAIR
        throw IllegalArgumentException("Unknown card")
    }

    fun rankWithWildCard(): Type {
        if (!count.containsKey("J")) return rank()

        val wildcards = count["J"]!!
        if (wildcards == 5 || wildcards == 4) return Type.FIVE_OF_KIND

        val cardCount = count - "J"
        if (wildcards == 3 && cardCount.values.all { it == 2 }) return Type.FIVE_OF_KIND
        if (wildcards == 3 && cardCount.values.all { it == 1 }) return Type.FOUR_OF_KIND

        if (wildcards == 2 && cardCount.values.all { it == 1 }) return Type.THREE_OF_KIND
        if (wildcards == 2 && cardCount.values.any { it == 2 }) return Type.FOUR_OF_KIND
        if (wildcards == 2 && cardCount.values.all { it == 3 }) return Type.FIVE_OF_KIND

        if (wildcards == 1 && cardCount.values.all { it == 1 }) return Type.ONE_PAIR

        if (wildcards == 1 && cardCount.values.all { it == 2 }) return Type.FULL_HOUSE

        if (wildcards == 1 && cardCount.values.any { it == 2 }) return Type.THREE_OF_KIND

        if (wildcards == 1 && cardCount.values.any { it == 3 }) return Type.FOUR_OF_KIND
        if (wildcards == 1 && cardCount.values.all { it == 4 }) return Type.FIVE_OF_KIND

        throw IllegalArgumentException("Unknown J Configuration $cards")
    }

    fun compare(other: CamelCard): Int {
        if (this.rank().value == other.rank().value) return tieBreak(other, cardToValue)
        return if (this.rank().value > other.rank().value) 1 else -1
    }

    fun compareWithWildcard(other: CamelCard): Int {
        if (this.rankWithWildCard().value == other.rankWithWildCard().value) return tieBreak(other, cardToValueWildCard)
        return if (this.rankWithWildCard().value > other.rankWithWildCard().value) 1 else -1
    }

    private fun value(idx: Int, cardToValue: Map<String, Int>) = cardToValue[cards[idx]]
            ?: cards[idx].toInt()

    private fun tieBreak(other: CamelCard, cardToValue: Map<String, Int>): Int {
        for (idx in other.cards.indices) {
            if (value(idx, cardToValue) == other.value(idx, cardToValue)) continue
            return if (value(idx, cardToValue) > other.value(idx, cardToValue)) 1 else -1
        }
        throw Exception("Impossible state")
    }
}

fun camelCards(input: List<String>) = input.map { line ->
    val (hands, bid) = line.split(Regex("\\s+"))
    CamelCard(hands.trim().split("").map { it.trim() }.filter { it.isNotBlank() }, bid.toLong())
}

fun scoreCards(input: List<String>, comparator: Comparator<CamelCard>) =
        camelCards(input).sortedWith(comparator).mapIndexed { idx, card -> (idx + 1) * card.bid }.sum()

fun main() {
    fun part1(input: List<String>) = scoreCards(input) { o1, o2 -> o1.compare(o2) }
    fun part2(input: List<String>) = scoreCards(input) { o1, o2 -> o1.compareWithWildcard(o2) }
    val input = readInput("day7/day07_buck")
    part1(input).println()
    part2(input).println()
}
