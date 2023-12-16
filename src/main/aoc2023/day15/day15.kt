package aoc2023.day15

import main.aoc2023.println
import main.aoc2023.readInput


data class Lens(val label: String, val focalLength: Int)

data class Box(var lenses: MutableList<Lens> = mutableListOf()) {
    fun add(lens: Lens) {
        val index: Int = lenses.indexOfFirst { it.label == lens.label }
        if (index == -1) lenses.add(lens) else lenses[index] = lens
    }

    fun remove(label: String) {
        lenses = lenses.filter { it.label != label }.toMutableList()
    }
}

fun lensHash(s: String) = s.fold(0) { acc: Int, c: Char -> ((acc + c.code) * 17) % 256 }

fun processStep(
    step: String, acc: MutableMap<Int, Box>
): MutableMap<Int, Box> {
    if (step.contains("=")) {
        val (label, value) = step.split("=")
        acc.getOrPut(lensHash(label)) { Box() }.add(Lens(label, value.toInt()))
        return acc
    }

    val label = step.split("-").first()
    acc[lensHash(label)]?.remove(label)
    return acc
}

fun splitSteps(it: String) = it.trim().split(",").map { it.trim() }


fun main() {
    fun part1(input: List<String>) = input.flatMap { splitSteps(it) }.sumOf { lensHash(it) }

    fun part2(input: List<String>) = input.flatMap { splitSteps(it) }
        .fold(mutableMapOf<Int, Box>()) { acc, step -> processStep(step, acc) }
        .map { (number, boxes) ->
            boxes.lenses.mapIndexed { idx, c -> (number + 1) * (idx + 1) * c.focalLength }.sum()
        }.sum()

    val input = readInput("aoc2023/day15/day15")
    part1(input).println()
    part2(input).println()
}
