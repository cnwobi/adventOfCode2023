package aoc2023.day09

import main.aoc2023.println
import main.aoc2023.readInput

fun main() {
    fun lineToNums(line: String) = line.split(Regex("\\s+")).map { it.trim().toLong() }
    fun nextInSequence(seq: List<Long>): Long {
        if (seq.all { it == 0L }) return 0
        val nextSeq = seq.drop(1).zip(seq.dropLast(1)).map { it.first - it.second }
        return nextInSequence(nextSeq) + seq.last()
    }

    fun part1(input: List<String>) = input.map { lineToNums(it) }.sumOf { nextInSequence(it) }
    fun part2(input: List<String>) = input.map { lineToNums(it) }.sumOf { nextInSequence(it.reversed()) }

    val input = readInput("aoc2023/day09/day09")
    part1(input).println()
    part2(input).println()
}
