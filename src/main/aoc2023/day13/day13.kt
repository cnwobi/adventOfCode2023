package aoc2023.day13

import main.aoc2023.println
import main.aoc2023.readInput


fun toGrids(input: List<String>): List<List<String>> {
    return input.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
        if (line.isBlank()) acc.add(mutableListOf()) else acc.last().add(line)
        acc
    }
}

fun checkIsMirror(index: Int, grid: List<String>): Boolean {
    var up = index - 1
    var down = index + 2
    while (up in grid.indices && down in grid.indices) {
        if (grid[up] != grid[down]) return false
        up--
        down++
    }
    return true
}

fun findMirrorLine(grid: List<String>): Int? {
    for (idx in 0 until grid.size - 1) {
         if (grid[idx] == grid[idx+1] && checkIsMirror(idx,grid)) return idx + 1
    }
    return null
}

fun calculateMirrorLines(grid: List<String>):Int {
    return findMirrorLine(grid)?.let { it * 100 } ?: findMirrorLine(transpose(grid)) ?: 0
}

fun transpose(grid: List<String>): List<String> {
   return (0 until grid[0].length).map { col ->
        grid.indices.map { row -> grid[row][col] }.joinToString(separator = "")
    }
}


fun main() {
    fun part1(input: List<String>): Int {
        val grids = toGrids(input)
        return grids.sumOf { calculateMirrorLines(it) }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("aoc2023/day13/day13")
    part1(input).println()
    part2(input).println()
}
