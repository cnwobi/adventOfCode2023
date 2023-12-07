package main.day7

import main.println
import main.readInput
import java.lang.Exception
import java.lang.IllegalArgumentException

enum class Type(val value: Int) {
    FIVE_OF_KIND(7), FOUR_OF_KIND(6), FULL_HOUSE(5), THREE_OF_KIND(4), TWO_PAIR(3), ONE_PAIR(2), HIGH_CARD(1)
}

data class Hand(val cards: List<String>, val bid: Long = 0) {
    private val cardToValue: MutableMap<String, Int> = mutableMapOf("A" to 50, "K" to 40, "Q" to 30, "J" to 20, "T" to 10)
    private val cardToValueWildCard: MutableMap<String, Int> = mutableMapOf("A" to 50, "K" to 40, "Q" to 30, "T" to 10, "J" to 1)
    private val countByCard = cards.groupingBy { it }.eachCount()
    private val type = rank()
    private val typeWildcard = rankWithWildCard()

    private fun rank(): Type {
        val uniqueCards = countByCard.keys.size
        if (uniqueCards == 5) return Type.HIGH_CARD
        if (uniqueCards == 1) return Type.FIVE_OF_KIND
        if (uniqueCards == 2 && countByCard.values.any { it == 4 }) return Type.FOUR_OF_KIND
        if (uniqueCards == 2 && countByCard.values.any { it == 3 }) return Type.FULL_HOUSE
        if (uniqueCards == 3 && countByCard.values.any { it == 3 }) return Type.THREE_OF_KIND
        if (countByCard.values.count { it == 2 } == 2) return Type.TWO_PAIR
        if (countByCard.values.count { it == 2 } == 1) return Type.ONE_PAIR
        throw IllegalArgumentException("Unknown card")
    }

    fun rankWithWildCard(): Type {
        if (!countByCard.containsKey("J")) return rank()

        val wildcards = countByCard["J"]!!
        if (wildcards == 5 || wildcards == 4) return Type.FIVE_OF_KIND

        val cardCount = countByCard - "J"
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

    fun compare(other: Hand): Int = if (type == other.type) tieBreak(other, cardToValue) else type.value - other.type.value
    fun compareWithWildcard(other: Hand): Int = if (typeWildcard == other.typeWildcard) tieBreak(other, cardToValueWildCard) else typeWildcard.value - other.typeWildcard.value

    private fun value(idx: Int, cardToValue: Map<String, Int>) = cardToValue[cards[idx]]
            ?: cards[idx].toInt()

    private fun tieBreak(other: Hand, cardToValue: Map<String, Int>): Int {
        for (idx in other.cards.indices) {
            if (value(idx, cardToValue) == other.value(idx, cardToValue)) continue
            return value(idx, cardToValue) - other.value(idx, cardToValue)
        }
        throw Exception("Impossible state")
    }
}

fun toCards(input: List<String>) = input.map { line ->
    val (hands, bid) = line.split(Regex("\\s+"))
    Hand(hands.trim().split("").map { it.trim() }.filter { it.isNotBlank() }, bid.toLong())
}

fun scoreCards(input: List<String>, comparator: Comparator<Hand>) =
        toCards(input).sortedWith(comparator).mapIndexed { idx, card -> (idx + 1) * card.bid }.sum()

fun main() {
    fun part1(input: List<String>) = scoreCards(input) { o1, o2 -> o1.compare(o2) }
    fun part2(input: List<String>) = scoreCards(input) { o1, o2 -> o1.compareWithWildcard(o2) }
    val input = readInput("day7/day07_buck")
    part1(input).println()
    part2(input).println()
}
