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

data class Range(val name: String, val destinationStart: Long, val start: Long, val range: Long) {
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
    val rangeMinHeap = PriorityQueue<Range>(compareBy { it.start })
    rangeMinHeap.addAll(ranges)
    val stateEndIsBeforeAnyRangeStart = rangedState.end < rangeMinHeap.peek().start

    if (stateEndIsBeforeAnyRangeStart) {
        return setOf(rangedState.copy(name = rangedState.nextState!!))
    }

    val states = mutableSetOf<RangedState>()

    var currentState = rangedState

    while (rangeMinHeap.isNotEmpty() && currentState.start < currentState.end) {
        val currentStateNotInRange = currentState.start > rangeMinHeap.peek().end
        if (currentStateNotInRange) {
            rangeMinHeap.poll()
            continue
        }

        if (currentState.start < rangeMinHeap.peek().start) {
            val end = min(currentState.end, rangeMinHeap.peek().start)
            val newRange = end - currentState.start
            val foundState = rangedState.copy(name = currentState.nextState!!, start = currentState.start, range = newRange)
            states.add(foundState)
            currentState = rangedState.copy(name = currentState.name, start = end, range = currentState.end - end + 1)
            continue
        }

        val range = rangeMinHeap.poll()
        val start = max(rangedState.start, currentState.start)
        val end = min(currentState.end, range.end) + 1
        val destinationStart = range.toDestinationStart(start)
        val newRange = end - start + 1
        val foundState = rangedState.copy(name = currentState.nextState!!, start = destinationStart!!, range = newRange)
        states.add(foundState)
        currentState = currentState.copy(start = end, range = currentState.end - end)

    }

    if (currentState.start < currentState.end) {
        states.add(currentState.copy(name = currentState.nextState!!))
    }
    return states
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
        queue.addAll(rangedSeeds)

        val endStates = mutableListOf<RangedState>()
        while (queue.isNotEmpty()) {
            val currState = queue.poll()
            if (currState.endState) {
                endStates.add(currState)
                continue
            }
            queue.addAll(rangedStateToNextStates(currState, ranges[currState.name]!!))
        }
        return "part 2: ${endStates.minOf { it.start }}"
    }

    val input = readInput("day5/day05")
    part1(input).println()
    part2(input).println()

}
