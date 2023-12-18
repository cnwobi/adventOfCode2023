package main.aoc2023.aoc2023.day18

import main.aoc2023.println
import main.aoc2023.readInput
import kotlin.math.abs

fun main() {

    fun toInstruction(line: String): Pair<String, Int> {
        val (dir, steps, _) = line.split(Regex("\\s+"))
        return Pair(dir, steps.toInt())
    }

    fun groupWalls(
        walls: MutableList<MutableList<Pair<Int, Int>>>,
        element: Pair<Int, Int>
    ): MutableList<MutableList<Pair<Int, Int>>> {
        val last = walls.lastOrNull()?.lastOrNull()
        if (last == null || abs(element.second - last.second) > 1) {
            walls.add(mutableListOf())
        }
        walls.last().add(element)
        return walls
    }

    fun toDelta(dir: String, step: Int, last: Pair<Int, Int>): Pair<Int, Int> {
        return when (dir.trim()) {
            "R" -> last.copy(second = last.second + step)
            "L" -> last.copy(second = last.second - step)
            "U" -> last.copy(first = last.first - step)
            "D" -> last.copy(first = last.first + step)
            else -> throw IllegalStateException("Unknown dir")
        }
    }

    fun drawInstruction(
        shapeSoFar: List<Pair<Int, Int>>,
        instruction: Pair<String, Int>
    ): List<Pair<Int, Int>> {
        val (dir, steps) = instruction
        val last = shapeSoFar.lastOrNull() ?: Pair(0, 0)
        return shapeSoFar + (1..steps).map { step -> toDelta(dir, step, last) }
    }

    fun part1(input: List<String>): Int {
     val c =   input.map { toInstruction(it) }
            .fold(listOf<Pair<Int, Int>>()) { acc, instruction ->
                drawInstruction(acc, instruction)
            }.groupBy { it.first }.mapValues { (k, v) ->
                v.sortedBy { it.second }
                    .fold(mutableListOf<MutableList<Pair<Int, Int>>>()) { acc, element ->
                        groupWalls(
                            acc,
                            element
                        )
                    }


            }
                .mapValues { (k,v) ->
                    val minY = v.first().first().second
               val walls =  v.map {
                    it.first().second - minY .. it.last().second - minY }


               val countOfWalls = walls.sumOf { it.toList().size }

                    var seenWalls = 0
                 val inside =   walls.zipWithNext().map { (wall1,wall2) ->
                        seenWalls += 1
                        (wall1.last+ 1 until wall2.first).map {
                            if(seenWalls % 2 == 1) 1 else 0
                        }.sum()
                    }.sum()

                    "walls $countOfWalls".println()
                    "inside $inside".println()
                    "\n".println()

                    inside.println()
               countOfWalls + inside

                }.values.sum().println()
//                .forEach { it.println() }



        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
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
