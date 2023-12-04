package day3

import println
import readInput


data class Index(val row:Int, val col: Int)
data class PartNumber(val start:Index, val value:Int)
data class PartNumberStr(val start:Index, val value:String)
fun main() {
    val indexToNumber = mutableMapOf<Index, PartNumber>()

    fun populateReverseIndex(row:Int,col:Int,numStr:String) {
        val coordinateStart = Index(row,col)
        val actualNum = PartNumber(coordinateStart,numStr.toInt())

        for (idx in numStr.indices) {
            indexToNumber[Index(row,col+idx) ] = actualNum
        }
    }

    fun processLine(row: Int, line:String) {
        var start = 0
        var inDigit = false

        for (index in line.indices) {
            if (line[index].isDigit() && !inDigit) {
                start = index
                inDigit = true
            }
            if (!line[index].isDigit() && inDigit) {
                inDigit = false
                val numStr = line.substring(start, index)
                numStr.println()
                populateReverseIndex(row,start,numStr)
            }
        }
//        indexToNumber.println()

    }

    fun part1(input: List<String>): Int {
        input.forEachIndexed { row, line -> processLine(row,line) }
        val partNumbers = mutableSetOf<PartNumber>()

        val delta = listOf(-1,0,1)
        for (i in delta) {
            for (j in delta) {
                if(!(i == 0 && j == 0)) {
                    Pair(i,j).println()
                }

            }
        }

        input.forEachIndexed{
            r, line ->
            line.forEachIndexed{
                c, ch ->
                if(!ch.isDigit() && ch != '.') {
                    for (i in delta) {
                        for (j in delta) {
                            val neighbour = Index(r+i,c+j)
                            indexToNumber[neighbour]?.let { partNumbers.add(it) }
                        }
                    }

                }
            }
        }

        return partNumbers.sumOf { p -> p.value }
    }

    fun part2(input: List<String>): Int {


        return input.size
    }



    val input = readInput("day3/Day03")
    part1(input).println()
//    part2(input).println()
//    processLine(0,".........640....................724.........63*844...........*...234.690.48....*.....+........612..............*.......483.............$....")
}
