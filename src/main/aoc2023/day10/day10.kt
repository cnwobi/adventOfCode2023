package aoc2023.day10

import main.aoc2023.println
import main.aoc2023.readInput
import java.util.*
import kotlin.math.min


//| is a vertical pipe connecting north and south.
//- is a horizontal pipe connecting east and west.
//L is a 90-degree bend connecting north and east.
//J is a 90-degree bend connecting north and west.
//7 is a 90-degree bend connecting south and west.
//F is a 90-degree bend connecting south and east.

// | is a vertical pipe connecting south and north.
// - is a horizontal pipe connecting west and east.
// L is a 90-degree bend connecting south and west.
// J is a 90-degree bend connecting south and east.
// 7 is a 90-degree bend connecting north and east.
// F is a 90-degree bend connecting north and west.

//  S
//E
enum class Direction {
    NORTH, SOUTH, EAST, WEST
}


data class Point(val coord: Pair<Int, Int>, val shape: String, val steps: Int = 0) {
}

fun main() {


    fun findLoop(input: List<String>): MutableMap<Pair<Int, Int>, Int> {
        val seen = mutableMapOf<Pair<Int, Int>, Int>()
        val queue: Queue<Point> = LinkedList()
        val grid = input.map { it.split("").filter { it.isNotBlank() } }
        val startElement =
            grid.flatMapIndexed { x, row -> row.mapIndexed { y, ch -> Point(Pair(x, y), ch) } }
                .first { it.shape == "S" }


        queue.add(startElement)

        while (!queue.isEmpty()) {
            val element = queue.poll()
            seen[element.coord] = min(element.steps, seen[element.coord] ?: Int.MAX_VALUE)

            val (x, y) = element.coord
            val nextStep = element.steps + 1

            val elementEast = grid.getOrNull(x)?.getOrNull(y + 1)
            val eastCoord = Pair(x, y + 1)

            val elementWest = grid.getOrNull(x)?.getOrNull(y - 1)
            val westCoord = Pair(x, y - 1)

            val elementNorth = grid.getOrNull(x - 1)?.getOrNull(y)
            val northCoord = Pair(x - 1, y)
            val elementSouth = grid.getOrNull(x + 1)?.getOrNull(y)
            val southCoord = Pair(x + 1, y)

            // can go east
            if (elementEast != null && element.shape in setOf(
                    "S",
                    "-",
                    "F",
                    "L"
                ) && elementEast in setOf("J", "7", "-")
            ) {
                if (eastCoord in seen) {
                    seen[eastCoord] = min(nextStep, seen[eastCoord] ?: Int.MAX_VALUE)
                } else {
                    queue.add(Point(eastCoord, elementEast, nextStep))
                }
            }
            if (elementWest != null && element.shape in setOf(
                    "S",
                    "-",
                    "7",
                    "J"
                ) && elementWest in setOf("-", "F", "L")
            ) {
                if (westCoord in seen) {
                    seen[westCoord] = min(nextStep, seen[westCoord] ?: Int.MAX_VALUE)
                } else {
                    queue.add(Point(westCoord, elementWest, nextStep))
                }
            }

            if (elementNorth != null && element.shape in setOf(
                    "S",
                    "|",
                    "L",
                    "J"
                ) && elementNorth in setOf("|", "7", "F")
            ) {
                if (northCoord in seen) {
                    seen[northCoord] = min(nextStep, seen[northCoord] ?: Int.MAX_VALUE)
                } else {
                    queue.add(Point(northCoord, elementNorth, nextStep))
                }
            }

            if (elementSouth != null && element.shape in setOf(
                    "S",
                    "|",
                    "7",
                    "F"
                ) && elementSouth in setOf("|", "L", "J")
            ) {
                if (southCoord in seen) {
                    seen[southCoord] = min(nextStep, seen[southCoord] ?: Int.MAX_VALUE)
                } else {
                    queue.add(Point(southCoord, elementSouth, nextStep))
                }
            }
        }
        return seen
    }

    fun part1(input: List<String>): Int {


        return findLoop(input).values.max()
    }


//    fun part2(input: List<String>): Int {
//        val grid = input.map { it.split("").filter { it.isNotBlank() } }
//        val loop = findLoop(input)
//        val c = grid.flatMapIndexed { r, row ->
//            row.mapIndexed { c, item ->
//                if (item != ".") return -1
//                (c until grid[0].size).filter { Pair(r, it) in loop }.count()
//            }
//        }
//    }

    val input = readInput("aoc2023/day10/sample")
//    part1(input).println()
//    part2(input).println()
}

// 2,0
