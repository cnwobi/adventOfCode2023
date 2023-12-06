package day5

import main.println
import main.readInput
import java.util.*
import kotlin.math.max
import kotlin.math.min


fun toStates(input: List<String>): Sequence<State> {
    return Regex("\\d+").findAll(input[0].split(":")[1].trim()).map { State(value = it.value.toLong()) }
}

fun toRangedStates(input: List<String>): Sequence<RangedState> {
    return Regex("\\d+")
            .findAll(input[0].split(":")[1].trim())
            .map { it.value }
            .chunked(2)
            .map { RangedState(start = it.first().toLong(), range = it.last().toLong()) }
}

fun part1ParseInput(input: List<String>): Pair<Sequence<State>, Map<String, List<Range>>> {
    val restInput = input.drop(1).filter { it.isNotBlank() }
    return Pair(toStates(input), linesToRanges(restInput))
}

fun part2ParseInput(input: List<String>): Pair<Sequence<RangedState>, Map<String, List<Range>>> {
    val restInput = input.drop(1).filter { it.isNotBlank() }
    return Pair(toRangedStates(input), linesToRanges(restInput))
}

val stateNameToNextStateName = mapOf(
        "seed-to-soil" to "soil-to-fertilizer",
        "soil-to-fertilizer" to "fertilizer-to-water",
        "fertilizer-to-water" to "water-to-light",
        "water-to-light" to "light-to-temperature",
        "light-to-temperature" to "temperature-to-humidity",
        "temperature-to-humidity" to "humidity-to-location")

data class Range(val name: String = "", val destinationStart: Long, val start: Long, val range: Long) {
    val end = start + range - 1
    fun toDestinationStart(source: Long): Long? {
        if (source in start until start + range) {
            return destinationStart + (source - start)
        }
        return null
    }
}

data class State(val name: String = "seed-to-soil", val value: Long) {
    val nextState: String? = stateNameToNextStateName[name]
}

data class RangedState(val name: String = "seed-to-soil", val start: Long, val range: Long) {
    val end = start + range - 1
    val nextState: String? = stateNameToNextStateName[name]
    val endState = !stateNameToNextStateName.containsKey(name)

    fun findIntersection(range: Range): Triple<RangedState?, RangedState?, RangedState?> {
        if (end < range.start) {
            return Triple(this.copy(name = nextState!!), null, null)
        }

        if (start > range.end) {
            return Triple(null, null, this)
        }

        val firstStart = min(start, range.start)
        val firstEnd = min(end, range.start) - 1
        var range1: RangedState? = null
        var range2: RangedState? = null
        var range3: RangedState? = null

        if (firstStart <= firstEnd) {
            range1 = RangedState(name = nextState!!, start = firstStart, range = firstEnd - firstStart + 1)
        }


        val secondStart = max(start, range.start)
        val secondEnd = min(end, range.end)

        if (secondStart <= secondEnd) {
            val newRange = secondEnd - secondStart + 1
            range2 = RangedState(name = nextState!!, start = range.toDestinationStart(secondStart)!!, range = newRange)
        }


        val thirdStart = min(end, range.end) + 1
        val thirdEnd = max(end, range.end)

        if (thirdEnd in thirdStart..end) {
            range3 = RangedState(name = name, start = thirdStart, range = thirdEnd - thirdStart + 1)
        }

        return Triple(range1, range2, range3)
    }
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

fun rangedStateToNextStates(rangedState: RangedState, ranges: List<Range>): Set<RangedState> {
    val sortedRanges = ranges.sortedBy { it.start }


    val states = mutableSetOf<RangedState?>()
    var currentState: RangedState? = rangedState

    for (range in sortedRanges) {
        if (currentState == null) {
            break
        }
        if (currentState.end < range.start) {
            states.add(currentState.copy(name = currentState.nextState!!))
            break
        }
        val (start, mid, end) = currentState.findIntersection(range)
        states.add(start)
        states.add(mid)
        currentState = end
    }

    if(currentState != null) {
        states.add(currentState.copy(name = currentState.nextState!!))
    }

    return states.filterNotNull().toSet()
}

fun stateToStateTransition(seed: State, rangesByStateName: Map<String, List<Range>>): State {
    fun nextState(state: State): State {
        var curr = state
        val nextValue = rangesByStateName[curr.name]!!.firstNotNullOfOrNull { it.toDestinationStart(curr.value) }
                ?: curr.value
        val nextState = curr.nextState ?: curr.name
        curr = curr.copy(name = nextState, value = nextValue)
        return curr
    }

    var curr = seed
    while (curr.nextState != null) {
        curr = nextState(curr)
    }
    curr = nextState(curr)
    return curr
}

fun main() {

    fun part1(input: List<String>): String {
        val (seeds, ranges) = part1ParseInput(input)
        return "part 1: ${seeds.map { stateToStateTransition(it, ranges) }.minOf { it.value }}"
    }

    fun part2(input: List<String>): String {
        val queue: Queue<RangedState> = LinkedList()
        val (rangedSeeds, ranges) = part2ParseInput(input)
        queue.add(rangedSeeds.toList()[0])

        val endStates = mutableListOf<RangedState>()
        while (queue.isNotEmpty()) {
            val currState = queue.poll()
            if (currState.endState) {
                endStates.add(currState)
                continue
            }
            val nextStates = rangedStateToNextStates(currState, ranges[currState.name]!!)
            queue.addAll(nextStates)
        }
        return "part 2: ${endStates.minOf { it.start }}"
    }

    val input = readInput("day5/Sample")
//    part1(input).println()
    part2(input).println()

}
