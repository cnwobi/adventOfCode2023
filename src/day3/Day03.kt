package day3

import println
import readInput


data class Point(val row: Int, val col: Int)
data class PartNumber(val start: Point, val value: Int)

data class Element(val point: Point, val ch: Char) {
    val isAsterisk = ch == '*'
}

fun main() {
    val pointByPartNumber = mutableMapOf<Point, PartNumber>()
    val delta = listOf(-1, 0, 1)
    val neighbourIndexDelta = delta.flatMap { d -> delta.map { d2 -> Point(d, d2) } }.filter { it != Point(0, 0) }

    fun populateReverseIndex(row: Int, col: Int, numStr: String) {
        val coordinateStart = Point(row, col)
        val actualNum = PartNumber(coordinateStart, numStr.toInt())
        for (idx in numStr.indices) {
            pointByPartNumber[Point(row, col + idx)] = actualNum
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

    fun isSymbol(ch: Char) = !ch.isDigit() && ch != '.'
    fun neighbours(row: Int, col: Int) = neighbourIndexDelta.map { it.copy(row + it.row, col = col + it.col) }.mapNotNull { pointByPartNumber[it] }.toSet()

    fun lineToPartNumbers(row: Int, line: String) =
            line.mapIndexed { col, ch -> Pair(col, ch) }.filter { isSymbol(it.second) }.flatMap { neighbours(row, it.first) }


    fun part1(input: List<String>): Int {
        input.forEachIndexed { row, line -> processLine(row, line) }

        return input.flatMapIndexed { row, line -> lineToPartNumbers(row, line) }.sumOf { it.value }
    }


    fun part2(input: List<String>): Int {
        input.forEachIndexed { row, line -> processLine(row, line) }
        fun getAdjacentNumbers(index: Point) =
                neighbourIndexDelta.map { n -> n.copy(row = index.row + n.row, col = index.col + n.col) }
                        .mapNotNull { pointByPartNumber[it] }.toSet()

        fun lineToElements(row: Int, line: String) = line.mapIndexed { col, ch -> Element(Point(row, col), ch) }

        return input
                .flatMapIndexed { row, line -> lineToElements(row, line) }
                .asSequence()
                .filter { it.isAsterisk }
                .map { getAdjacentNumbers(it.point) }
                .filter { it.size == 2 }
                .map { adjacentNumbers -> adjacentNumbers.fold(1) { acc, partNumber -> acc * partNumber.value } }.sum()
    }


    val input = readInput("day3/Day03")
    part1(input).println()
    part2(input).println()
}
