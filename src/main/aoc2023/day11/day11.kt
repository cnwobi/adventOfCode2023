package aoc2023.day11

import main.aoc2023.println
import main.aoc2023.readInput
import kotlin.math.max
import kotlin.math.min

fun findAllBlankColsAndRows(input: List<String>): Pair<Set<Int>, Set<Int>> {
    fun isAllDots(axis:String) = axis.all { it == '.' }
    fun columnAllDots(col: Int) = input.indices.map { row -> input[row][col] }.all { it == '.' }

    val blankRows = input.mapIndexedNotNull { idx, line -> if(isAllDots(line)) idx else null}.toSet()
    val blankColumns = input[0].indices.mapNotNull { if (columnAllDots(it)) it else null }.toSet()
    return Pair(blankRows, blankColumns)
}

fun findAllGalaxies(input: List<String>): List<Pair<Int, Int>> {
    return input.indices.flatMap { r -> input[r].indices.map { Pair(r, it) } }.filter { (r, c) -> input[r][c] == '#' }
}

fun distanceBetweenGalaxies(
    galaxy: Pair<Int, Int>,
    galaxy2: Pair<Int, Int>,
    blankRows: Set<Int>,
    blankCols: Set<Int>,
    scale:Long = 2L
): Long {
    fun expansionsXaxis(blankAxis: Set<Int>, range: IntRange) = blankAxis.count { it in range }
    val (minX, maxX) = Pair(
        min(galaxy.first, galaxy2.first),
        max(galaxy.first, galaxy2.first)
    )
    val (minY, maxY) = Pair(
        min(galaxy.second, galaxy2.second),
        max(galaxy.second, galaxy2.second)
    )
    val blankXAxisInRange = expansionsXaxis(blankRows, minX..maxX)
    val blankYAxisInRange = expansionsXaxis(blankCols, minY..maxY)
    val distanceX = maxX - minX -  blankXAxisInRange + scale * blankXAxisInRange
    val distanceY = maxY - minY - blankYAxisInRange + scale * blankYAxisInRange
    return distanceY + distanceX
}


fun uniqueGalaxies(input: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    val allGalaxies = findAllGalaxies(input)
    val uniqueGalaxyPairs = (0 until allGalaxies.size - 1).flatMap { i ->
        (i + 1 until allGalaxies.size).map { j ->
            Pair(allGalaxies[i], allGalaxies[j])
        }
    }
    return uniqueGalaxyPairs
}

fun main() {
    fun part1(input: List<String>): Long {
        val (blankRows, blankCols) = findAllBlankColsAndRows(input)
        val uniqueGalaxyPairs = uniqueGalaxies(input)
        return uniqueGalaxyPairs.sumOf { (g, g2) -> distanceBetweenGalaxies(g, g2, blankRows, blankCols) }
    }
    fun part2(input: List<String>): Long {
        val (blankRows, blankCols) = findAllBlankColsAndRows(input)
        val uniqueGalaxyPairs = uniqueGalaxies(input)
        return uniqueGalaxyPairs.sumOf { (g, g2) -> distanceBetweenGalaxies(g, g2, blankRows, blankCols, scale = 1_000_000) }
    }
    val input = readInput("aoc2023/day11/day11")
    part1(input).println()
    part2(input).println()
}
