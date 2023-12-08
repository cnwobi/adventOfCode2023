package main.day8


import main.println
import main.readInput


fun main() {
    fun locationMap(input:List<String>):Pair<List<String>,Map<String,Pair<String,String>>> {
        val instructions = input[0].split("").filter { it.isNotBlank() }
        val map = input.drop(1).filter { it.isNotBlank() }.map { line ->
            val (key, left, right) =
                Regex("\\w+").findAll(line).map { it.value.trim() }.toList()
            Pair(key,Pair(left,right))
        }.associateBy { it.first }.mapValues { it.value.second }
        return Pair(instructions,map)
    }
    fun locationMap2(input:List<String>):Pair<List<String>,Map<String,Pair<String,String>>> {
       val (instructions,map) = locationMap(input)
        return Pair(instructions.reversed(),map)
    }

    fun part1(input: List<String>): Int {
        val (instructions,map) = locationMap(input)
        var instructionIdx = 0

        var currentKey = "AAA"
        val endLocation = "ZZZ"
        var count = 0

        while (currentKey != endLocation) {
            val currInstruction = instructions[instructionIdx]
            val (left,right) = map[currentKey]!!
            instructionIdx = (instructionIdx + 1) % instructions.size
            currentKey = if(currInstruction == "L") left else right
            count ++
        }
        return count
    }

    fun findCountToZ(location:String, map:Map<String, Pair<String, String>>,instructions:List<String>) {


    }

    fun part2(input: List<String>): Int {
        val (instructions,map) = locationMap(input)
        var locations = map.keys.filter { it.endsWith("A") }
        var instructionIdx = 0
        var count = 0



        while (!locations.all { it.endsWith("Z") }) {

            val currInstruction = instructions[instructionIdx]
            Pair(locations.joinToString(),currInstruction).println()
            instructionIdx = (instructionIdx + 1) % instructions.size
            locations = locations.map { currentKey ->
                val (left,right) = map[currentKey]!!
                if(currInstruction == "L") left else right
            }

            count ++
        }
        Pair(locations.joinToString(),instructions[instructionIdx]).println()
        return count
    }

    val testInput = readInput("day8/sample2")
    val testInput2 = readInput("day8/sample3")
    check(part1(testInput) == 6)
    check(part2(testInput2) == 6)

//    val input = readInput("day8/day08")
//    part1(input).println()
//    part2(input).println()
}
