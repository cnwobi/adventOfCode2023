package main.day4

import main.println
import main.readInput
import java.util.LinkedList
import java.util.Queue
import kotlin.math.pow


data class Card(val id: Int, val winningNumbers: Set<Int>, val numbers: Set<Int>) {
    fun countOfWinningNums(): Int = winningNumbers.intersect(numbers).size
    fun nextWinningCards(cardById: Map<Int, Card>): List<Card> = (id + 1..id + countOfWinningNums()).mapNotNull { cardById[it] }
}

fun lineToCard(line: String): Card {
    val cardMatch = Regex("Card\\s+(\\d+):.+")
    val cardNumber = cardMatch.find(line)!!.groupValues[1].toInt()
    val (winningNums, nums) = line.split(":")[1]
            .trim().split("|")
            .map { it.trim().split(Regex("\\s+")).map { n -> n.toInt() }.toSet() }
    return Card(cardNumber, winningNums, nums)
}


fun main() {
    fun part1(input: List<String>): Int {
        return input.map { lineToCard(it) }
                .map { it.countOfWinningNums() }
                .filter { it > 0 }
                .sumOf { count -> 2.toDouble().pow(count - 1).toInt() }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { lineToCard(it) }
        val cardById = cards.associateBy { it.id }
        val queue: Queue<Card> = LinkedList()
        val processedCards = mutableListOf<Card>()
        queue.addAll(cards)

        while (queue.isNotEmpty()) {
            val currCard = queue.poll()
            queue.addAll(currCard.nextWinningCards(cardById = cardById))
            processedCards.add(currCard)
        }

        return processedCards.size
    }

    val input = readInput("day4/day04")
    part1(input).println()
    part2(input).println()
}
