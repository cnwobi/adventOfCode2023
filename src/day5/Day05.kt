package day5

import println
import readInput
import kotlin.math.max
import kotlin.math.min


data class Range(val name: String, val destinationStart: Long, val sourceStart: Long, val range: Long) {
    val sourceEnd = sourceStart + range - 1
    val destinationEnd = destinationStart + range - 1
    fun mapToDestination(source: Long): Long? {
        if (source in this.sourceStart until sourceStart + range) {
            return destinationStart + (source - sourceStart)
        }
        return null
    }

}

val transistions = mapOf("seed-to-soil" to "soil-to-fertilizer", "soil-to-fertilizer" to "fertilizer-to-water", "fertilizer-to-water" to "water-to-light", "water-to-light" to "light-to-temperature", "light-to-temperature" to "temperature-to-humidity", "temperature-to-humidity" to "humidity-to-location")

data class State(val name: String = "seed-to-soil", val value: Long) {
    val nextState: String? = transistions[name]
}

data class State2(val name: String = "seed-to-soil", val start: Long, val range: Long) {
    val nextState: String? = transistions[name]
}

fun linesToRanges(lines: List<String>): Map<String, List<Range>> {
    fun lineToRange(name: String, line: String): Range {
        val regex = Regex("\\d+")
        val (destination, source, range) = regex.findAll(line).map { it.value }.toList()
        return Range(name, destination.toLong(), source.toLong(), range.toLong())
    }

    var name = ""
    val ranges = mutableListOf<Range>()

    for (line in lines) {
        if (line.contains("map")) {
            name = line.dropLast(4).trim()
            continue
        }
        ranges.add(lineToRange(name, line))
    }

    return ranges.groupBy { it.name }
}

private fun nextState(ranges: Map<String, List<Range>>, curr: State): State {
    var curr1 = curr
    val nextValue = ranges[curr1.name]!!.firstNotNullOfOrNull { it.mapToDestination(curr1.value) }
            ?: curr1.value
    val nextState = curr1.nextState ?: curr1.name
    curr1 = curr1.copy(name = nextState, value = nextValue)
    return curr1
}
fun transistions(seed: State, ranges: Map<String, List<Range>>): State {
    var curr = seed
    while (curr.nextState != null) {
        curr = nextState(ranges, curr)
    }
    curr = nextState(ranges,curr)
    return curr
}



fun main() {

    fun part1(input: List<String>): Long {
        val restInput = input.drop(1).filter { it.isNotBlank() }
        val ranges: Map<String, List<Range>> = linesToRanges(restInput)
        val seeds = Regex("\\d+").findAll(input[0].split(":")[1].trim()).map { State(value = it.value.toLong()) }
        return seeds.map { transistions(it,ranges) }.minOf { it.value }
    }

    fun part2(input: List<String>): Long {
        val restInput = input.drop(1).filter { it.isNotBlank() }
        val ranges: Map<String, List<Range>> = linesToRanges(restInput)
        val seeds = Regex("\\d+").findAll(input[0].split(":")[1].trim()).map { it.value }.chunked(2).first()

        return  (seeds.first().toLong() until seeds.first().toLong() + seeds.last().toLong()).map { State(value = it) }.map { transistions(it,ranges) }.minOf { it.value }
    }


//    val input = readInput("day5/day05")
////    part1(input).println()
//    part2(input).println()




}
