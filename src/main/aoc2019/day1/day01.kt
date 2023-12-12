package main.aoc2023.aoc2019.day1

import main.aoc2023.println
import main.aoc2023.readInput
import kotlin.math.max

fun main() {

    fun fuel(mass:Long):Long {
        var mMass = mass
        var total = 0L
        while (mMass > 0) {
           mMass =  (mMass / 3) -2
           total += max(0,mMass)
        }
        return total
    }

    fun part1(input: List<String>) = input.sumOf { it.trim().toLong() / 3 - 2 }
    fun part2(input: List<String>) = input.map { it.trim().toLong() }.sumOf { fuel(it) }

    val input = readInput("aoc2019/day1/day01")
    part1(input).println()
    part2(input).println()
    fuel(100756).println()
}

