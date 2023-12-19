package main.aoc2023.aoc2023.day19

import main.aoc2023.println
import main.aoc2023.readInput
import java.util.ArrayDeque

val allRange = 1L..4000L
val allCombinations = mapOf("x" to allRange, "m" to allRange, "a" to allRange, "s" to allRange)

data class RangedPart(val part: Map<String, LongRange> = allCombinations, val workflow: String) {
    fun uniqueCombinations(): Long =
        part.values.fold(1L) { acc, range -> acc * (range.last - range.first + 1) }
    fun isInTerminalWorkflow() = workflow == "A" || workflow == "R"
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

data class Rule(val condition: Condition? = null, val nextWorkflow: String)
data class Part(val ratings: Map<String, Int>,val workflow:String = "in") {
    fun isInTerminalWorkflow() = workflow == "A" || workflow == "R"
    fun isAccepted() =  workflow == "A"

    fun ratingsSum() = ratings.values.sum()

}

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
            Rule(nextWorkflow = rs)
        }
    }

    return Pair(workflowName, rules)
}

fun toTerminalWorkflow(workflowToRules: Map<String, List<Rule>>, part: Part): Part {
    val queue = ArrayDeque<Part>()
    queue.add(part)
    while (queue.isNotEmpty()) {
        val currPart = queue.poll()
        if (currPart.isInTerminalWorkflow()) return currPart!!
        val rules = workflowToRules[currPart.workflow]!!
        for (rule in rules) {
            if (rule.condition == null || rule.condition.isMet(part.ratings)) {
                queue.add(part.copy(workflow = rule.nextWorkflow))
                break
            }
        }
    }
    throw IllegalStateException("must terminate")
}


fun possibleTerminalStates(
    workflow: Map<String, List<Rule>>,
): MutableList<RangedPart> {
    val queue = ArrayDeque(listOf(RangedPart(workflow = "in")))

    val terminalStates = mutableListOf<RangedPart>()

    while (queue.isNotEmpty()) {
        var currentPart = queue.poll()
        if (currentPart.isInTerminalWorkflow()) {
            terminalStates.add(currentPart)
            continue
        }

        val rules = workflow[currentPart.workflow]!!
        for (rule in rules) {
            if (rule.condition == null) {
                queue.add(currentPart.copy(workflow = rule.nextWorkflow))
                break
            } else {
                val (accepted, rejected) = rule.condition.splitRange(currentPart.part)
                queue.add(currentPart.copy(part = accepted, workflow = rule.nextWorkflow))
                currentPart = currentPart.copy(part = rejected)
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

    val workflowToRules = rulesStr.associate(::workflowRulesPair)
    val parts = partsStr.map { Part(toRatings(it)) }
    return workflowToRules to parts

}

fun main() {
    fun part1(input: List<String>): Int {
        val (workflowToRules, parts) = parseInput(input)
        return parts.map { toTerminalWorkflow(workflowToRules,it) }.filter { it.isAccepted() }.sumOf { it.ratingsSum() }
    }

    fun part2(input: List<String>): Long {
        val (graph, _) = parseInput(input)
        return possibleTerminalStates(graph)
            .filter { it.isAccepted }
            .sumOf { it.uniqueCombinations() }
    }

    val input = readInput("aoc2023/day19/day19")
    part1(input).println()
    part2(input).println()
}
