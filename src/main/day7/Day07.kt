package main.day7

import main.println
import main.readInput
import java.lang.Exception
import java.lang.IllegalArgumentException

enum class HandType(val value: Int) {
    FIVE_OF_KIND(7),
    FOUR_OF_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1)
}

data class CamelCard(val hands: List<String>, val bid: Long= 0) {
    private val rankByHand: MutableMap<String, Int> = mutableMapOf("A" to 50,
            "K" to 40, "Q" to 30, "J" to 20, "T" to 10)
    private val rankByHand2: MutableMap<String, Int> = mutableMapOf("A" to 50,
            "K" to 40, "Q" to 30,  "T" to 10,"J" to 1)
    private val count = hands.groupBy { it }.mapValues { it.value.size }
    private fun rank1(): HandType {
        val keys = count.keys.toList()
        if (keys.size == 5) return HandType.HIGH_CARD
        if (count.keys.size == 1) return HandType.FIVE_OF_KIND// 5 of a kind
        if (keys.size == 2 && count.values.any { it == 4 }) return HandType.FOUR_OF_KIND // 4 of a kind
        if (keys.size == 2 && count.values.any { it == 3 }) return HandType.FULL_HOUSE // full house
        if (keys.size == 3 && count.values.any { it == 3 }) return HandType.THREE_OF_KIND // three of a kind T -> 3 , 9 -> 1, 8 -> 1
        if (count.values.count { it == 2 } == 2) return HandType.TWO_PAIR// two pair       2 -> 2 3 -> 2 4 -> 1
        if (count.values.count { it == 2 } == 1) return HandType.ONE_PAIR
        throw IllegalArgumentException("Unknown card")
    }

     fun rank2(): HandType {
        if (!count.containsKey("J")) {
            return rank1()
        }
        val jCount = count["J"]!!
        if (jCount == 5 || jCount == 4) return HandType.FIVE_OF_KIND

        val noJCount = count - "J"
        if (jCount == 3 && noJCount.values.all { it == 2 }) return HandType.FIVE_OF_KIND
        if (jCount == 3 && noJCount.values.all { it == 1 }) return HandType.FOUR_OF_KIND

        if (jCount == 2 && noJCount.values.all { it == 1 }) return HandType.THREE_OF_KIND
        if (jCount == 2 && noJCount.values.any { it == 2 }) return HandType.FOUR_OF_KIND
        if (jCount == 2 && noJCount.values.all { it == 3 }) return HandType.FIVE_OF_KIND

        if (jCount == 1 && noJCount.values.all { it == 1 }) return HandType.ONE_PAIR

        if (jCount == 1 && noJCount.values.all { it == 2 }) return HandType.FULL_HOUSE

        if (jCount == 1 && noJCount.values.any { it == 2 }) return HandType.THREE_OF_KIND

        if (jCount == 1 && noJCount.values.any { it == 3 }) return HandType.FOUR_OF_KIND
        if (jCount == 1 && noJCount.values.all { it == 4 }) return HandType.FIVE_OF_KIND

        throw IllegalArgumentException("Unknown J Configuration $hands")
    }

    fun compare(other: CamelCard): Int {
        if (this.rank1().value > other.rank1().value) {
            return 1
        }
        if (this.rank1().value < other.rank1().value) {
            return -1
        }
        return tieBreak(other)
    }

    fun compare2(other: CamelCard): Int {
        if (this.rank2().value > other.rank2().value) {
            return 1
        }
        if (this.rank2().value < other.rank2().value) {
            return -1
        }
        return tieBreak2(other)
    }



    private fun handWorthByIdx(idx: Int): Int {
        val h = hands[idx]
        return rankByHand[h] ?: h.toInt()
    }

    private fun handWorthByIdx2(idx: Int): Int {
        val h = hands[idx]
        return rankByHand2[h] ?: h.toInt()
    }

    private fun tieBreak(other: CamelCard): Int {
        for (idx in other.hands.indices) {
            val thisHand = handWorthByIdx(idx)
            val otherHand = other.handWorthByIdx(idx)
            if (thisHand == otherHand) continue
            if (thisHand > otherHand) return 1
            return -1
        }
        throw Exception("Impossible state")
    }

    private fun tieBreak2(other: CamelCard): Int {
        for (idx in other.hands.indices) {
            val thisHand = handWorthByIdx2(idx)
            val otherHand = other.handWorthByIdx2(idx)
            if (thisHand == otherHand) continue
            if (thisHand > otherHand) return 1
            return -1
        }
        throw Exception("Impossible state")
    }
}

fun main() {
    fun camelCards(input: List<String>) = input.map { line ->
        val (hands, bid) = line.split(Regex("\\s+"))
        CamelCard(hands.trim().split("").map { it.trim() }.filter { it.isNotBlank() }, bid.toLong())
    }

    fun part1(input: List<String>): Long {
        val cards = camelCards(input)
        val cardComparator = Comparator<CamelCard> { o1, o2 -> o1.compare(o2) }
        return cards.sortedWith(cardComparator).mapIndexed { idx, card -> (idx + 1) * card.bid }.sum()
    }

    fun part2(input: List<String>): Long  {
        val cards = camelCards(input)
        val cardComparator = Comparator<CamelCard> { o1, o2 -> o1.compare2(o2) }
        return cards.sortedWith(cardComparator).mapIndexed { idx, card -> (idx + 1) * card.bid }.sum()
    }

    val input = readInput("day7/day07_buck")
    part1(input).println()
    part2(input).println()
}
