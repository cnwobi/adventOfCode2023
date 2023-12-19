package main.aoc2023.aoc2023.day18

import main.aoc2023.println
import main.aoc2023.readInput
import kotlin.math.pow
import kotlin.math.roundToLong

fun main() {
    val hexMap = mapOf("a" to 10L, "b" to 11, "c" to 12, "d" to 13, "e" to 14, "f" to 15)

    fun toDir(code: Char): String {
//        0 means R, 1 means D, 2 means L, and 3 means U
        return when (code) {
            '0' -> "R"
            '1' -> "D"
            '2' -> "L"
            '3' -> "U"
            else -> throw IllegalStateException()
        }
    }

    fun hexToLong(hex: String): Long {
        return hex.reversed().mapIndexed { index, c ->
            (hexMap[c.toString()] ?: c.toString().toLong()) * 16.0.pow(index) }
            .sum().roundToLong()
    }

    fun toInstructionHex(line: String): Pair<String, Long> {
        val (_,codedInstruction) = line.split(Regex("#"))
        val hexDirCode = codedInstruction.dropLast(1)
        val dir = toDir(hexDirCode.last())
        return Pair(dir, hexToLong(hexDirCode.dropLast(1)))
    }

    fun toInstruction(line: String): Pair<String, Long> {
        val (dir, steps, _) = line.split(Regex("\\s+"))
        return Pair(dir, steps.toLong())
    }

    fun toDelta(dir: String, step: Long, last: Pair<Long, Long>): Pair<Long, Long> {
        return when (dir.trim()) {
            "R" -> last.copy(second = last.second + step)
            "L" -> last.copy(second = last.second - step)
            "U" -> last.copy(first = last.first - step)
            "D" -> last.copy(first = last.first + step)
            else -> throw IllegalStateException("Unknown dir")
        }
    }

    fun buildPolygon(
        shapeSoFar: List<Pair<Long, Long>>,
        instruction: Pair<String, Long>
    ): List<Pair<Long, Long>> {
        val (dir, steps) = instruction
        val last = shapeSoFar.lastOrNull() ?: Pair(0L, 0L)
        return shapeSoFar + toDelta(dir, steps, last)
    }

    fun shoeLacePicksTheorem(input:List<String>,op:(String) -> Pair<String,Long>): Long {
        val perimeter = input.sumOf { op(it).second }
        val polygonPoints = input.map { op(it) }
            .fold(listOf<Pair<Long, Long>>()) { acc, instruction ->
                buildPolygon(acc, instruction)
            }
        val closedPolygon =  (polygonPoints + polygonPoints.first())
        val area = closedPolygon.zipWithNext().sumOf { (p1, p2) ->
            val (x1, y1) = p1
            val (x2, y2) = p2
            (y1 + y2) * (x2 - x1)
        }

        return (area + perimeter + 2).div(2)
    }

    fun part1(input: List<String>): Long {
        return shoeLacePicksTheorem(input,::toInstruction)
    }

    fun part2(input: List<String>): Long {
        return shoeLacePicksTheorem(input,::toInstructionHex)
    }

    /*
#######
#.....#
###...#
..#...#
..#...#
###.###
#...#..
##..###
.#....#
.######
*/
    val input = readInput("aoc2023/day18/day18")
    part1(input).println()
    part2(input).println()
}
