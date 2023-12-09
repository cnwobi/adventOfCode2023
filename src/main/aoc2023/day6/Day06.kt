package main.aoc2023.day6

import main.aoc2023.println
import main.aoc2023.readInput


fun main() {

    fun part1(input: List<String>): Int {
        val numbersMatch = Regex("\\d+")
        val time = numbersMatch.findAll(input[0]).map { it.value.toInt() }
        val distance = numbersMatch.findAll(input[1]).map { it.value.toInt() }
        val timeBestDistance = time.zip(distance).toList()
        return  timeBestDistance.map { (time, distance) -> (0..time).map { speed -> (time - speed) * speed }.count { it > distance } }.reduce { acc, e -> acc * e }
    }

    fun part2(input: List<String>): Int {
        val numbersMatch = Regex("\\d+")
        val time = numbersMatch.findAll(input[0]).map { it.value }.toList().joinToString(separator = "").toLong()
        val distance = numbersMatch.findAll(input[1]).map { it.value}.toList().joinToString(separator = "").toLong()
        return (0..time).map { speed -> (time - speed) * speed }.count { it > distance }
    }

    val input = readInput("aoc2023/day6/day06")
    part1(input).println()
    part2(input).println()

}
