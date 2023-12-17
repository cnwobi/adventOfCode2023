package aoc2023.day13

import main.aoc2023.println
import main.aoc2023.readInput

fun toGrids(input: List<String>): List<List<String>> =
    input.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
        if (line.isBlank()) acc.add(mutableListOf()) else acc.last().add(line)
        acc
    }

fun checkIsMirror(index: Int, grid: List<String>, allowedDiff: Int = 0): Boolean {
    var up = index
    var down = index + 1
    while (up in grid.indices && down in grid.indices) {
        if (diff(grid[up--], grid[down++]) > allowedDiff) return false
    }
    return true
}

fun findMirrorLine(grid: List<String>) =
    (0 until grid.size - 1).firstOrNull { checkIsMirror(it, grid) }?.let { it + 1 }

fun diff(line: String, line1: String) = line.zip(line1).count { (ch1, ch2) -> ch1 != ch2 }

fun findMirrorLine2(grid: List<String>): Int? {
    val oldMirrorLine = findMirrorLine(grid)
    return (0 until grid.size - 1)
        .filter { checkIsMirror(it, grid, 1) }
        .map { it + 1 }
        .firstOrNull { it != oldMirrorLine }
}

fun calculateMirrorLines(grid: List<String>) =
    findMirrorLine(grid)?.let { it * 100 } ?: findMirrorLine(transpose(grid)) ?: 0

fun calculateMirrorLines2(grid: List<String>) =
    findMirrorLine2(grid)?.let { it * 100 } ?: findMirrorLine2(transpose(grid)) ?: 0

fun transpose(grid: List<String>) =
    (0 until grid[0].length).map { col ->
        grid.indices.map { row -> grid[row][col] }.joinToString(separator = "")
    }

fun main() {
    fun part1(input: List<String>) = toGrids(input).sumOf { calculateMirrorLines(it) }
    fun part2(input: List<String>) = toGrids(input).sumOf { calculateMirrorLines2(it) }
    val input = readInput("aoc2023/day13/day13")
    part1(input).println()
    part2(input).println()
}
