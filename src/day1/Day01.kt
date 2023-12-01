package day1

import parseDigit
import println
import readInput
import kotlin.text.StringBuilder

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { it ->
            val firstIdx = it.asIterable().indexOfFirst { it.isDigit() }
            val lastIdx = it.asIterable().indexOfLast { it.isDigit() }
            StringBuilder().append(it[firstIdx]).append(it[lastIdx]).toString().toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val digits = listOf(
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "one",
                "two",
                "three",
                "four",
                "five",
                "six",
                "seven",
                "eight",
                "nine"
        )
        return input.sumOf {
            val first = it.findAnyOf(digits)?.second?.parseDigit()
            val second = it.findLastAnyOf(digits)?.second?.parseDigit()
            "$first$second".toInt()
        }
    }


    val input = readInput("day1/Day01")
    part1(input).println()
    part2(input).println()

}
