package main.aoc2023.aoc2023.day19

import main.aoc2023.println
import main.aoc2023.readInput
import java.util.ArrayDeque

val allRange = 1L..4000L
val allCombinations = mapOf("x" to allRange, "m" to allRange, "a" to allRange, "s" to allRange)

data class RangedPart(val part: Map<String, LongRange> = allCombinations, val workflow: String) {
    fun uniqueCombinations(): Long =
        part.values.fold(1L) { acc, range -> acc * (range.last - range.first + 1) }
    val isAccepted: Boolean
        get() = workflow == "A"
}

data class Condition(val category: String, val operand: String, val threshold: Int) {
    fun isMet(part: Map<String, Int>): Boolean {
        val categoryValue = part[category]!!
        return if (operand == ">") categoryValue > threshold else categoryValue < threshold
    }

    fun splitRange(part: Map<String, LongRange>): Pair<Map<String, LongRange>, Map<String, LongRange>> {
        val curr = part[category]!!
        val rejectedRange: LongRange
        val acceptedRange: LongRange
        if (operand == ">") {
            acceptedRange = threshold + 1..curr.max()
            rejectedRange = curr.min()..threshold
        } else {
            acceptedRange = curr.min() until threshold
            rejectedRange = threshold..curr.max()
        }
        return Pair(part + (category to acceptedRange), part + (category to rejectedRange))
    }
}

data class Rule(val condition: Condition? = null, val nextState: String)
data class Part(val ratings: Map<String, Int>)

fun workflowRulesPair(workflowStr: String): Pair<String, List<Rule>> {
    val regex = Regex("(\\w+)\\{(.+)}")
    val matchResult = regex.find(workflowStr)
    val workflowName = matchResult!!.groupValues[1]

    val unParsedRules = matchResult.groupValues[2]

    val rules = unParsedRules.split(",").map { rs ->
        if (rs.contains(":")) {
            val r = Regex("([xmas])([><])(\\d+):(\\w+)")
            val match = r.find(rs)
            val cat = match!!.groupValues[1]
            val operand = match.groupValues[2]
            val threshold = match.groupValues[3].toInt()
            val nextState = match.groupValues[4]
            Rule(Condition(cat, operand, threshold), nextState)
        } else {
            Rule(nextState = rs)
        }
    }

    return Pair(workflowName, rules)
}

fun terminalState(workflow: Map<String, List<Rule>>, part: Part): String {
    val queue = ArrayDeque<String>()
    queue.add("in")

    while (queue.isNotEmpty()) {
        val state = queue.poll()
        if (state !in workflow) return state!!
        val rules = workflow[state]!!
        for (rule in rules) {
            if (rule.condition == null || rule.condition.isMet(part.ratings)) {
                queue.add(rule.nextState)
                break
            }
        }
    }
    throw IllegalStateException("must terminate")
}


fun getTerminatedRangedParts(
    workflow: Map<String, List<Rule>>,
): MutableList<RangedPart> {
    val queue = ArrayDeque(listOf(RangedPart(workflow = "in")))

    val terminalStates = mutableListOf<RangedPart>()

    while (queue.isNotEmpty()) {
        var psa = queue.poll()
        if (psa.workflow !in workflow) {
            terminalStates.add(psa)
            continue
        }

        val rules = workflow[psa.workflow]!!
        for (rule in rules) {
            if (rule.condition == null) {
                queue.add(psa.copy(workflow = rule.nextState))
                break
            } else {
                val (accepted, rejected) = rule.condition.splitRange(psa.part)
                queue.add(psa.copy(part = accepted, workflow = rule.nextState))
                psa = psa.copy(part = rejected)
            }
        }
    }
    return terminalStates
}

fun toRatings(partString: String): Map<String, Int> {
    return partString.drop(1)
        .dropLast(1)
        .split(",")
        .map { it.split("=") }
        .associate { (category, ratingStr) -> category to ratingStr.toInt() }
}


fun parseInput(input: List<String>): Pair<Map<String, List<Rule>>, List<Part>> {
    val (rulesStr, partsStr) = input.fold(mutableListOf<MutableList<String>>(mutableListOf())) { acc, s ->
        if (s.isBlank()) acc.add(mutableListOf()) else acc.last().add(s)
        acc
    }.map { it.toList() }

    val workflowGraph = rulesStr.associate(::workflowRulesPair)
    val parts = partsStr.map { Part(toRatings(it)) }
    return workflowGraph to parts

}

fun main() {
    fun part1(input: List<String>): Int {
        val (graph, parts) = parseInput(input)
        return parts.filter { terminalState(graph, it) == "A" }.sumOf { it.ratings.values.sum() }
    }


    fun part2(input: List<String>): Long {
        val (graph, _) = parseInput(input)
        return getTerminatedRangedParts(graph)
            .filter { it.isAccepted }
            .sumOf { it.uniqueCombinations() }
    }

    val input = readInput("aoc2023/day19/day19")
    part1(input).println()
    part2(input).println()
}
