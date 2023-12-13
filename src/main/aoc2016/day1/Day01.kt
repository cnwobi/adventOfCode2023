package aoc2016.day1

import main.aoc2023.println
import main.aoc2023.readInput
import kotlin.math.abs

enum class Direction {
    NORTH, SOUTH, EAST, WEST
}


fun move(
    currLocation:  Triple<Int, Int, Direction>,
    instruction: Pair<Char, Int>
): Triple<Int, Int,Direction> {
    val (turn, steps) = instruction


   return when (currLocation.third) {
        Direction.NORTH -> if (turn == 'L') currLocation.copy(first = currLocation.first - steps, third = Direction.EAST) else currLocation.copy(
            first = currLocation.first + steps,third = Direction.WEST
        )

        Direction.SOUTH -> if (turn == 'L') currLocation.copy(first = currLocation.first + steps, third = Direction.WEST) else currLocation.copy(
            first = currLocation.first - steps, third = Direction.EAST
        )

        Direction.EAST -> if (turn == 'L') currLocation.copy(second = currLocation.second - steps, third = Direction.SOUTH) else currLocation.copy(
            second = currLocation.second + steps,third = Direction.NORTH
        )

        Direction.WEST -> if (turn == 'L') currLocation.copy(second = currLocation.second + steps,third = Direction.NORTH) else currLocation.copy(
            second = currLocation.second - steps,third = Direction.SOUTH
        )
    }

}

fun main() {
    fun part1(input: List<String>): Int {
        val start = Triple(0, 0, Direction.NORTH)
       val end =  input.first()
            .split(Regex(",\\s+"))
            .map { Pair(it.first(), it.drop(1).toInt()) }
            .fold(start) { currLocation, instruction -> move(currLocation,instruction) }
        return abs(end.first) + abs(end.second)
    }

    fun part2(input: List<String>): Int {
        val instructions =  input.first()
            .split(Regex(",\\s+"))
            .map { Pair(it.first(), it.drop(1).toInt()) }
        val seen = mutableSetOf(Pair(0,0))
        var firstSeenTwice:Pair<Int,Int>? = null
        var curr = Triple(0, 0, Direction.NORTH)

        for (instruction in instructions) {
            curr = move(curr,instruction)
            val loc = Pair(curr.first,curr.second)
            if ( loc in seen) {
                firstSeenTwice = loc
            }
            seen.add(loc)
        }

        return abs(firstSeenTwice!!.first) + abs(firstSeenTwice.second)
    }


    val input = readInput("aoc2016/day1/sample")
    part1(input).println()
    part2(input).println()
}
