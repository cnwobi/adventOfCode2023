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

fun findSumOfMirrorLines(grid: List<String>):Set<Int> {
    val horizontalLines = mutableListOf<Int>()
    for(idx in (0 until grid.size - 1)) {
        if (checkIfMirrored(idx, grid,0)) horizontalLines.add(idx+1)

    }
    return horizontalLines.toSet()
}

fun findSumOfMirrorLines2(grid: List<String>):Set<Int> {
    val horizontalLines = mutableListOf<Int>()
    for(idx in (0 until grid.size - 1)) {
        if (checkIfMirrored(idx, grid,1)) horizontalLines.add(idx+1)
    }
    return horizontalLines.toSet()
}



fun gridTranspose(grid: List<String>):List<String> {
   return  grid[0].indices.map {col ->
        grid.indices.map { row  -> grid[row][col]}.joinToString(separator = "")
    }

}

fun findDiff(line: String, line1: String,diff:Int): Boolean {
    val c = line.zip(line1).mapIndexed { idx, v -> Triple(idx, v.first, v.second) }
        .filter { it.second != it.third }.map { it.first }
    return c.size <= diff
}
private fun checkIfMirrored(idx: Int, grid: List<String>,diff: Int): Boolean {
    var up = idx
    var down = idx + 1
    var isMirror = true
    while (up >= 0 && down < grid.size) {
        isMirror = findDiff(grid[up],grid[down],diff)
        if (!isMirror) break
        up--
        down++
    }
    return isMirror
}

fun main() {
    fun part1(input: List<String>): Int {
        return  grids(input).sumOf {
            findSumOfMirrorLines(it).sum() *100 + findSumOfMirrorLines(
                gridTranspose(
                    it
                )
            ).sum()
        }
    }

    fun part2(input: List<String>): Int {

        return  grids(input).sumOf {
            (findSumOfMirrorLines2(it) - findSumOfMirrorLines(it)).sum() *100 +
                    (findSumOfMirrorLines2(gridTranspose(it)) - findSumOfMirrorLines(gridTranspose(it)).sum()
            ).sum()
        }
    }

    val input = readInput("aoc2023/day13/day13")
    part1(input).println()
    part2(input).println()
}
