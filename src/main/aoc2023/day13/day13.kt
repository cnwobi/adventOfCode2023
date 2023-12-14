package aoc2023.day13

import main.aoc2023.println
import main.aoc2023.readInput

fun grids(input:List<String>):List<List<String>> {
    return input.fold(mutableListOf(mutableListOf<String>())){
            acc, line ->
        if (line.isBlank()) acc.add(mutableListOf()) else acc.last().add(line)
        acc
    }
}

fun findSumOfMirrorLines(grid: List<String>):Int {
    val horizontalLines = mutableListOf<Int>()
    for(idx in (0 until grid.size - 1)) {
        if (findDiff(grid[idx],grid[idx+1]).isEmpty()) {
            if (checkIfMirrored(idx, grid)) horizontalLines.add(idx+1)
        }
    }
    return horizontalLines.sum()
}



fun gridTranspose(grid: List<String>):List<String> {
   return  grid[0].indices.map {col ->
        grid.indices.map { row  -> grid[row][col]}.joinToString(separator = "")
    }

}

fun findDiff(line: String, line1: String): List<Int> {
    val c = line.zip(line1).mapIndexed { idx, v -> Triple(idx, v.first, v.second) }
        .filter { it.second != it.third }.map { it.first }
    return c
}
private fun checkIfMirrored(idx: Int, grid: List<String>): Boolean {
    var up = idx - 1
    var down = idx + 2
    var isMirror = true
    while (up >= 0 && down < grid.size) {
        isMirror = grid[up] == grid[down]
        if (!isMirror) break
        up--
        down++
    }
    return isMirror
}

fun main() {
    fun part1(input: List<String>): Int {
        return  grids(input).sumOf {
            findSumOfMirrorLines(it)*100 + findSumOfMirrorLines(gridTranspose(it))
        }
    }

    fun part2(input: List<String>): Int {
        return  0
    }

    val input = readInput("aoc2023/day13/day13")
    part1(input).println()
    part2(input).println()
}
