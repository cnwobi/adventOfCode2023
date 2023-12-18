package main.aoc2023.aoc2023.day17

import aoc2016.day1.Direction
import main.aoc2023.println
import main.aoc2023.readInput
import java.util.PriorityQueue
import kotlin.math.min

data class NodeHeatLoss(val heatLoss: Int, val node: Node)
data class Node(val coord: Pair<Int, Int>, val direction: Direction, val step: Int) {
    fun inGrid(grid: List<List<Int>>): Boolean {
        val (row, col) = coord
        return row in grid.indices && col in grid.first().indices
    }

    private fun move(direction: Direction): Node {
        val nextStep = if (this.direction == direction) this.step + 1 else 1

        val newCoord = when (direction) {
            Direction.NORTH -> coord.copy(first = coord.first + 1)
            Direction.SOUTH -> coord.copy(first = coord.first - 1)
            Direction.EAST -> coord.copy(second = coord.second + 1)
            Direction.WEST -> coord.copy(second = coord.second - 1)
        }
        return this.copy(coord = newCoord, direction = direction, step = nextStep)
    }

    private fun nextDirections(): List<Direction> {
        return when (direction) {
            Direction.NORTH -> listOf(Direction.NORTH, Direction.EAST, Direction.WEST)
            Direction.SOUTH -> listOf(Direction.SOUTH, Direction.EAST, Direction.WEST)
            Direction.EAST -> listOf(Direction.EAST, Direction.NORTH, Direction.SOUTH)
            Direction.WEST -> listOf(Direction.WEST, Direction.NORTH, Direction.SOUTH)
        }
    }

   fun nextNodes(nextHeatLoss:Int,minStep: Int): List<NodeHeatLoss> {
      return nextDirections().mapNotNull {
           if (it == direction || step >= minStep)
               NodeHeatLoss(nextHeatLoss, move(direction = it))
           else null
       }
   }
}

fun priorityQueueWithSeeds(): PriorityQueue<NodeHeatLoss> {
    val priorityQueue = PriorityQueue<NodeHeatLoss>(compareBy { it.heatLoss })
    val right = Node(Pair(0, 1), Direction.EAST, 1)
    val down = Node(Pair(1, 0), Direction.SOUTH, 1)
    priorityQueue.addAll(listOf(NodeHeatLoss(0, right), NodeHeatLoss(0, down)))
    return priorityQueue
}

fun minHeatLoss(grid: List<List<Int>>, minStep: Int, maxStep: Int): Int {
    val priorityQueue = priorityQueueWithSeeds()
    val seen = mutableSetOf<Node>()
    val end = Pair(grid.size - 1, grid.first().size - 1)
    var minHeatLoss = Int.MAX_VALUE

    while (priorityQueue.isNotEmpty()) {
        val (prevHeatLoss, curr) = priorityQueue.poll()
        if (!curr.inGrid(grid) || curr.step > maxStep || curr in seen) continue
        seen.add(curr)
        val heatLoss = prevHeatLoss + grid[curr.coord.first][curr.coord.second]
        if (curr.coord == end && curr.step >= minStep) {
            minHeatLoss = min(minHeatLoss, heatLoss)
        }
        priorityQueue.addAll(curr.nextNodes(heatLoss, minStep))
    }

    return minHeatLoss

}

fun main() {
    fun toGrid(input: List<String>) = input
        .map { line ->
            line.split("")
                .filter { it.isNotEmpty() }
                .map { it.trim().toInt() }
        }

    fun part1(input: List<String>) = minHeatLoss(toGrid(input), minStep = 0, maxStep = 3)
    fun part2(input: List<String>) = minHeatLoss(toGrid(input), minStep = 4, maxStep = 10)

    val input = readInput("aoc2023/day17/day17")
    part1(input).println()
    part2(input).println()
}
