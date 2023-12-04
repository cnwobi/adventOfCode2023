package day3

import println
import readInput


data class Index(val row: Int, val col: Int)
data class PartNumber(val start: Index, val value: Int)
data class PartNumberStr(val start: Index, val value: String)

fun main() {
    val indexToNumber = mutableMapOf<Index, PartNumber>()
    val delta = listOf(-1, 0, 1)
    val neighbourIndexDelta = delta.flatMap { d -> delta.map { d2 -> Index(d, d2) } }.filter { it != Index(0, 0) }

    fun populateReverseIndex(row: Int, col: Int, numStr: String) {
        val coordinateStart = Index(row, col)
        val actualNum = PartNumber(coordinateStart, numStr.toInt())
        for (idx in numStr.indices) {
            indexToNumber[Index(row, col + idx)] = actualNum
        }
    }

    fun processLine(row: Int, line: String) {
        var start = 0
        var inDigit = false
        fun processNum(end: Int) {
            populateReverseIndex(row, start, line.substring(start, end))
        }
        for (index in line.indices) {
            if (line[index].isDigit() && !inDigit) {
                start = index
                inDigit = true
            }
            if (!line[index].isDigit() && inDigit) {
                inDigit = false
                processNum(index)
            }
        }
        if (inDigit) {
            processNum(line.length)
        }

    }

    fun part1(input: List<String>): Int {
        input.forEachIndexed { row, line -> processLine(row, line) }
        val partNumbers = mutableSetOf<PartNumber>()

        input.forEachIndexed { r, line ->
            line.forEachIndexed { c, ch ->
                if (!ch.isDigit() && ch != '.') {
                    neighbourIndexDelta
                            .map { it.copy(row = it.row + r, col = it.col + c) }
                            .forEach { indexToNumber[it]?.let { partNumbers.add(it) } }
                }
            }
        }

        return partNumbers.sumOf { p -> p.value }
    }

    fun getAdjacentNumbers(index: Index) =
            neighbourIndexDelta.map { n -> n.copy(row = index.row + n.row, col = index.col + n.col) }.mapNotNull { indexToNumber[it] }.toSet()


    fun part2(input: List<String>): Int {
        input.forEachIndexed { row, line -> processLine(row, line) }
        return input
                .flatMapIndexed { row, line -> line.mapIndexed { col, ch -> Pair(Index(row, col), ch) } }
                .asSequence()
                .filter { (_,ch) -> ch == '*' }
                .map { getAdjacentNumbers(it.first) }
                .filter { it.size == 2 }
                .map { adjacentNumbers -> adjacentNumbers.fold(1) { acc, partNumber -> acc * partNumber.value } }.sum()
    }


    val input = readInput("day3/Day03")
    part1(input).println()
    part2(input).println()
}
