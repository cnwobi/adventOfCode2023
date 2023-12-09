package main.day8


import main.lcm
import main.println
import main.readInput

fun main() {
    fun locationMap(input: List<String>): Pair<List<String>, Map<String, Pair<String, String>>> {
        val instructions = input[0].split("").filter { it.isNotBlank() }
        val map = input.drop(1).filter { it.isNotBlank() }.map { line ->
            val (key, left, right) =
                Regex("\\w+").findAll(line).map { it.value.trim() }.toList()
            Pair(key, Pair(left, right))
        }.associateBy { it.first }.mapValues { it.value.second }
        return Pair(instructions, map)
    }

    fun part1(input: List<String>): Int {
        val (instructions, map) = locationMap(input)
        var instructionIdx = 0

        var currentKey = "AAA"
        val endLocation = "ZZZ"
        var count = 0

        while (currentKey != endLocation) {
            val currInstruction = instructions[instructionIdx]
            val (left, right) = map[currentKey]!!
            instructionIdx = (instructionIdx + 1) % instructions.size
            currentKey = if (currInstruction == "L") left else right
            count++
        }
        return count
    }

    fun countToFirstZ(
        location: String,
        map: Map<String, Pair<String, String>>,
        instructions: List<String>
    ): Long {
        var instructionIdx = 0
        var currentKey = location
        var count = 0L
        while (!currentKey.endsWith("Z")) {
            val currInstruction = instructions[instructionIdx]
            val (left, right) = map[currentKey]!!
            instructionIdx = (instructionIdx + 1) % instructions.size
            currentKey = if (currInstruction == "L") left else right
            count++
        }
        return count
    }

    fun part2(input: List<String>): Long {
        val (instructions, map) = locationMap(input)
        val locations = map.keys.filter { it.endsWith("A") }
        map.filterKeys { it.endsWith("Z")  }.println()
        map.filterKeys { it.endsWith("A") }.println()
        val countToFirstZ = locations.map { countToFirstZ(it, map, instructions) }
        return countToFirstZ.fold(countToFirstZ.first()) { acc, i -> lcm(acc, i) }
    }

    val testInput = readInput("day8/sample2")
    check(part1(testInput) == 6)
    val input = readInput("day8/day08")
    part1(input).println()
    part2(input).println()
}


