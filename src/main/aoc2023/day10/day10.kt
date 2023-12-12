package aoc2023.day10

import main.aoc2023.println
import main.aoc2023.readInput
import java.util.*
import kotlin.math.min

data class Point(val coord: Pair<Int, Int>, val steps: Int = 0)

fun main() {
    fun neighbours(
        grid: List<List<String>>,
        x: Int,
        y: Int
    ): List<String?> {
        val elementEast = grid.getOrNull(x)?.getOrNull(y + 1)
        val elementWest = grid.getOrNull(x)?.getOrNull(y - 1)
        val elementNorth = grid.getOrNull(x - 1)?.getOrNull(y)
        val elementSouth = grid.getOrNull(x + 1)?.getOrNull(y)
        return listOf(elementEast, elementWest, elementNorth, elementSouth)
    }

    fun replaceS(grid: List<List<String>>): Pair<MutableList<MutableList<String>>, Point> {
        val (x, y) = grid.flatMapIndexed { x, row -> List(row.size) { y -> Pair(x, y) } }
            .first { (x, y) -> grid[x][y] == "S" }
        val (elementEast, elementWest, elementNorth, elementSouth) = neighbours(grid, x, y)
        val result: String = when {
            (elementWest in setOf("-", "F", "L") && elementEast in setOf("-", "7", "J")) -> "-"
            (elementSouth in setOf("|", "L", "J") && elementEast in setOf("-", "J", "7")) -> "F"
            (elementSouth in setOf("|", "L", "J") && elementNorth in setOf("7", "|", "F")) -> "|"
            (elementNorth in setOf("7", "|", "F") && elementWest in setOf("-", "F", "L")) -> "J"
            (elementSouth in setOf("|", "L", "J") && elementWest in setOf("-", "F", "L")) -> "7"
            (elementNorth in setOf("7", "|", "F") && elementEast in setOf("-", "J", "7")) -> "L"
            else -> throw IllegalStateException()
        }
        val mutableGrid = grid.map { it.toMutableList() }.toMutableList()
        mutableGrid[x][y] = result
        return Pair(mutableGrid, Point(Pair(x, y), 0))
    }

    fun grid(input: List<String>): Pair<List<List<String>>, Point> {
        return replaceS(input.map { it.split("").filter { it.isNotBlank() } })
    }


    fun findLoop(input: List<String>): MutableMap<Pair<Int, Int>, Int> {
        val seen = mutableMapOf<Pair<Int, Int>, Int>()
        val queue: Queue<Point> = LinkedList()
        val (grid, startElement) = grid(input)

        fun process(coord: Pair<Int, Int>, nextStep: Int) {
            if (coord in seen) seen[coord] = min(nextStep, seen[coord] ?: Int.MAX_VALUE)
            else queue.add(Point(coord, nextStep))
        }

        queue.add(startElement)
        while (!queue.isEmpty()) {
            val element = queue.poll()
            seen[element.coord] = min(element.steps, seen[element.coord] ?: Int.MAX_VALUE)

            val (x, y) = element.coord
            val shape = grid[x][y]
            val nextStep = element.steps + 1

            val eastCoord = Pair(x, y + 1)
            val westCoord = Pair(x, y - 1)
            val northCoord = Pair(x - 1, y)
            val southCoord = Pair(x + 1, y)

            val (elementEast, elementWest, elementNorth, elementSouth) = neighbours(grid, x, y)

            // can go east
            if (shape in setOf("S", "-", "F", "L") && elementEast in setOf("J", "7", "-")) {
                process(eastCoord, nextStep)
            }
            if (shape in setOf("S", "-", "7", "J") && elementWest in setOf("-", "F", "L")) {
                process(westCoord, nextStep)
            }

            if (shape in setOf("S", "|", "L", "J") && elementNorth in setOf("|", "7", "F")) {
                process(northCoord, nextStep)
            }

            if (shape in setOf("S", "|", "7", "F") && elementSouth in setOf("|", "L", "J")) {
                process(southCoord,nextStep)
            }
        }
        return seen
    }

    fun part1(input: List<String>): Int {
        return findLoop(input).values.max()
    }

    fun intersects(
        point: Pair<Int, Int>,
        grid: List<List<String>>,
        seen: Set<Pair<Int, Int>>
    ): Int {
        val (r, c) = point
        return (c + 1 until grid[0].size)
            .mapNotNull { col -> if (Pair(r, col) in seen) Pair(r, col) else null }
            .count { (_, col) -> grid[r][col] in setOf("J", "L", "|") }
    }


    fun part2(input: List<String>): Int {
        val (grid, _) = grid(input)
        val loop = findLoop(input).keys
        return grid.indices.flatMap { grid[0].indices.map { col -> Pair(it, col) } }
            .filter { it !in loop }.count { intersects(it, grid, loop) % 2 == 1 }
    }

    val input = readInput("aoc2023/day10/day10")
    part1(input).println()
    part2(input).println()
}

