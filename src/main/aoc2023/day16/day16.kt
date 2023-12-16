package main.aoc2023.aoc2023.day16

import aoc2016.day1.Direction
import main.aoc2023.println
import main.aoc2023.readInput
import java.util.*
import kotlin.system.measureTimeMillis

data class Beam(private val from: Pair<Int, Int>, val direction: Direction) {
    private var curr: Pair<Int, Int> = from
    private fun delta() = when (direction) {
        Direction.NORTH -> Pair(-1, 0)
        Direction.SOUTH -> Pair(1, 0)
        Direction.EAST -> Pair(0, 1)
        Direction.WEST -> Pair(0, -1)
    }

    fun next() {
        val (dx, dy) = delta()
        this.curr = curr.copy(first = curr.first + dx, second = curr.second + dy)
    }

    private fun shouldContinueTraveling(tile: Char) =
        ((direction == Direction.NORTH || direction == Direction.SOUTH) && tile == '|') || ((direction == Direction.WEST || direction == Direction.EAST) && tile == '-') || tile == '.'


    private fun nextBeams(mirror: Char): List<Beam> {
        val east = Beam(curr.copy(second = curr.second + 1), Direction.EAST)
        val west = Beam(curr.copy(second = curr.second - 1), Direction.WEST)
        val north = Beam(curr.copy(first = curr.first - 1), Direction.NORTH)
        val south = Beam(curr.copy(first = curr.first + 1), Direction.SOUTH)

        return when (direction) {
            Direction.NORTH -> when (mirror) {
                '/' -> listOf(east)
                '\\' -> listOf(west)
                '-' -> listOf(west, east)
                else -> error("Direction $direction, mirror $mirror")
            }

            Direction.SOUTH -> when (mirror) {
                '/' -> listOf(west)
                '\\' -> listOf(east)
                '-' -> listOf(west, east)
                else -> error("Direction $direction, mirror $mirror")
            }

            Direction.EAST -> when (mirror) {
                '/' -> listOf(north)
                '\\' -> listOf(south)
                '|' -> listOf(north, south)
                else -> error("Direction $direction, mirror $mirror")
            }

            Direction.WEST -> when (mirror) {
                '/' -> listOf(south)
                '\\' -> listOf(north)
                '|' -> listOf(north, south)
                else -> error("Direction $direction, mirror $mirror")
            }
        }
    }

    private fun isInbounds(contraption: List<List<Char>>): Boolean {
        return curr.first in contraption.indices && curr.second in contraption.first().indices
    }

    fun travel(
        contraption: List<List<Char>>, tiles: MutableList<MutableList<Char>>
    ): List<Beam> {
        while (isInbounds(contraption)) {
            val loc = contraption[curr.first][curr.second]
            tiles[curr.first][curr.second] = '#'
            if (shouldContinueTraveling(loc)) {
                next()
                continue
            }
            return nextBeams(loc)
        }
        return listOf()
    }
}


fun main() {
    fun energizedTiles(
        contraption: MutableList<MutableList<Char>>,
        tiles: MutableList<MutableList<Char>>,
        beam: Beam = Beam(from = Pair(0, 0), direction = Direction.EAST)
    ): Int {
        val queue = ArrayDeque<Beam>()
        queue.add(beam)
        val seen = mutableSetOf<Beam>()
        while (!queue.isEmpty()) {
            val curr = queue.poll()
            if (curr !in seen) queue.addAll(curr.travel(contraption, tiles))
            seen.add(curr)
        }
        return tiles.flatMap { it.map { it } }.count { it == '#' }
    }

    fun part1(input: List<String>): Int {
        val contraption = input.map { it.map { it }.toMutableList() }.toMutableList()
        val tiles = contraption.map { it.map { '.' }.toMutableList() }.toMutableList()

        return energizedTiles(contraption, tiles)
    }




    fun part2(input: List<String>): Int {
        val contraption = input.map { it.map { it }.toMutableList() }.toMutableList()
        fun tiles() = contraption.map { it.map { '.' }.toMutableList() }.toMutableList()
        val northwards = contraption.first().indices.map {
            Beam(Pair(contraption.size - 1, it), Direction.NORTH)
        }
        val southwards = contraption.first().indices.map { Beam(Pair(0, it), Direction.SOUTH) }
        val eastwards = contraption.indices.map { Beam(Pair(it, 0), Direction.EAST) }
        val westwards =
            contraption.indices.map { Beam(Pair(it, contraption.first().size - 1), Direction.WEST) }
        val allStartBeams = northwards + southwards + eastwards + westwards
        return allStartBeams.maxOf { energizedTiles(contraption, tiles(), it) }
    }

    val input = readInput("aoc2023/day16/day16")
    val executionTime = measureTimeMillis {
        part1(input)
    }

    val executionTime2 = measureTimeMillis {
        part2(input)
    }

    // m3 * n3
    // m * n
    // 2m + 2n
    // m + n (m * n)
    // m2 + 2mn + n2
    // m2 + n2


    part1(input).println()
    part2(input).println()
    "Part 1 time : $executionTime ms".println()
    "Part 2 time : $executionTime2 ms".println()
}
