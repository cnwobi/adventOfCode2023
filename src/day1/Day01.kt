package day1

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { it ->
            val firstIdx = it.indexOfFirst { it.isDigit() }
            val lastIdx = it.indexOfLast { it.isDigit() }
            "${it[firstIdx]}${it[lastIdx]}".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val wordToDigitMap: MutableMap<String, String> = mutableMapOf(
                "one" to "1", "two" to "2", "three" to "3", "four" to "4", "five" to "5", "six" to "6", "seven" to "7", "eight" to "8", "nine" to "9"
        )
        wordToDigitMap.putAll((1..9).associate { it.toString() to it.toString() })

        return input.sumOf {line ->
            val numbers = line.indices.flatMap { i ->
                (i until line.length).mapNotNull { j -> wordToDigitMap.getOrElse(line.substring(i, j + 1)){null} }
            }
            "${numbers.first()}${numbers.last()}".toInt()
        }
    }


    val input = readInput("day1/Day01")
    part1(input).println()
    part2(input).println()

}
