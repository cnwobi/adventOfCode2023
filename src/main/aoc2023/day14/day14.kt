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
                        if (iprime == 0) {
                            newMap[iprime][j] = 'O'
                        }
                        iprime--


                    }
                }

            }
        }
        return newMap
    }

    fun process_col(col:Int,grid: List<List<Char>>): Int {
        var row = 0
        var ans = 0
        while (row < grid.size) {
            while (row < grid.size && grid[row][col] == '#')
                row +=1
            var count = 0
            val start = row
            while (row < grid.size && grid[row][col] != '#') {
                if (grid[row][col] == '0') {
                    count += 1
                }
                row += 1
            }

            for (i in start until start + count ) {
                ans += row - i
            }
        }
        return ans

    }

    fun cycle(curr:List<List<Char>>): List<List<Char>> {
        return transpose(rollNorth(transpose(rollNorth(transpose(rollNorth(transpose(rollNorth(curr))))))))
    }

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
        val oldMap = input.map {
            it.map { it }
        }
        var curr = oldMap
        for (i in 1 .. 1000000000) {
            curr = cycle(curr)
        }


        return curr.mapIndexed{idx,row ->
            row.count { it == 'O' } * (row.size - idx)
        }.sum()
    }

    val input = readInput("aoc2023/day14/day14")
    part2(input).println()
}
