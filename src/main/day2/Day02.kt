package main.day2


import main.println
import main.readInput

data class Cube(val count: Int, val color: String)
data class Game(val id: Int, val cubes: List<Cube>) {
    private val colorByCountInBag = mapOf("red" to 12, "green" to 13, "blue" to 14)
    fun isValid():Boolean = cubes.all { (count, color) -> count <= colorByCountInBag[color]!! }
    private fun maxPerColor(): Map<String, Int> = cubes.groupBy { it.color }.mapValues { (_, v) -> v.maxOf { it.count } }
    fun powerOfCubes():Int = maxPerColor().values.reduce{acc,maxCount -> acc * maxCount}
}

fun lineToGame(line: String): Game {
    val gameMatch = Regex("Game (\\d+):.+")
    val gameNumber = gameMatch.find(line)!!.groupValues[1].toInt()
    val match = Regex("(\\d+) (\\w+)")
    val rolls = match.findAll(line).map { Cube(it.groupValues[1].toInt(), it.groupValues[2]) }.toList()
    return Game(gameNumber, rolls)
}

fun main() {
    fun part1(input: List<String>): Int = input.map { lineToGame(it) }.filter { it.isValid() }.sumOf { it.id }
    fun part2(input: List<String>): Int = input.map { lineToGame(it) }.sumOf { it.powerOfCubes() }
    val input = readInput("day2/Day02")
    part1(input).println()
    part2(input).println()
}
