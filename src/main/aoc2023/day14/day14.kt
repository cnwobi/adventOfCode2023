package aoc2023.day14

import main.aoc2023.println
import main.aoc2023.readInput


fun transpose(grid: List<List<Char>>): List<List<Char>> {
    return  grid[0].indices.map {col ->
        grid.indices.map { row  -> grid[row][col]}.reversed()
    }

}



fun main() {
    fun rollNorth(
        oldMap: List<List<Char>>,
    ): List<List<Char>> {
        val newMap = oldMap.map { it.map { '.' }.toMutableList()  }.toMutableList()
        for (i in oldMap.indices) {
            for (j in oldMap[0].indices) {
                if (oldMap[i][j] == '#') {
                    newMap[i][j] = '#'
                    continue
                }
                if (oldMap[i][j] == 'O') {
                    var iprime = i
                    while (iprime >= 0) {
                        if (newMap[iprime][j] == '#' || newMap[iprime][j] == 'O') {
                            newMap[iprime + 1][j] = 'O'
                            break
                        }
                        if (iprime == 0) newMap[iprime][j] = 'O'
                        iprime--
                    }
                }

            }
        }
        return newMap
    }


    fun cycle(curr:List<List<Char>>): List<List<Char>> =
        transpose(rollNorth(transpose(rollNorth(transpose(rollNorth(transpose(rollNorth(curr))))))))


    fun part1(input: List<String>): Int {
        val oldMap = input.map {
            it.map { it }
        }
        val newMap = rollNorth(oldMap)


        return  newMap.mapIndexed{idx,row ->
            row.count { it == 'O' } * (row.size - idx)
        }.sum()
    }


    fun part2(input: List<String>): Int {
        val stateToCycleSeen = mutableMapOf<List<List<Char>>,Int>()
        val cycleToGrid = mutableMapOf<Int,List<List<Char>>>()
        val oldMap = input.map { row -> row.map { it } }
        var curr = oldMap
        val maxCycles =1000000000
        for (i in 1 .. maxCycles) {
            curr = cycle(curr)

            if (curr in stateToCycleSeen) {
                val lastSeen = stateToCycleSeen[curr]!!
                val period = i - lastSeen
                val remainingSteps = (maxCycles  - lastSeen) % period
                curr = cycleToGrid[remainingSteps + lastSeen]!!
                break
            }
            stateToCycleSeen[curr] = i
            cycleToGrid[i] = curr
        }

        return curr.mapIndexed{idx,row -> row.count { it == 'O' } * (row.size - idx) }.sum()
    }

    val input = readInput("aoc2023/day14/day14")
    part1(input).println()
    part2(input).println()
}
