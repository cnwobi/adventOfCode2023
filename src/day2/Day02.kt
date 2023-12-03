package day2


import println
import readInput
import kotlin.math.max

data class Game(val number: Int, val rolls: List<List<Pair<Int, String>>>)

fun main() {
    val cubes = mapOf("red" to 12, "green" to 13, "blue" to 14)
    fun lineToGame(line: String): Game {
        val tokens = line.split(":")
        val gameNumber = tokens[0].split(" ")[1].trim().toInt()
        val rolls = tokens[1].split(";").map {
            it.split(",").map { t ->
                val pair = t.trim().split(" ")
                Pair(pair[0].trim().toInt(), pair[1].trim())
            }
        }
       return Game(gameNumber, rolls)
    }

    fun isValidRoll(rolls: List<Pair<Int, String>>) = rolls.map { roll -> roll.first <= cubes[roll.second]!! }

    fun part1(input: List<String>): Int {
        return input.map { lineToGame(it) }.filter { game ->
            game.rolls.flatMap { isValidRoll(it) }.fold(true) { b, acc -> acc && b }
        }.sumOf { game -> game.number }
    }

    fun part2(input: List<String>): Int {
       return input.map { lineToGame(it) }.sumOf {
            it.rolls.flatten().foldRight(mutableMapOf<String,Int>()) { (number, color), acc ->
                acc[color] = max(number, acc.getOrDefault(color,0))
                acc
            }.values.reduce { acc, i -> acc * i }
        }
    }


    val input = readInput("day2/Day02")
    part1(input).println()
    part2(input).println()

}
