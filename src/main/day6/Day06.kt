package day6

import main.println
import main.readInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt


fun main() {

    fun smallestNumber(time:Long,distance:Long) {
          var lo = 0L
          var high = time


          while (lo <= high) {
              val speed =  lo + (high - lo) / 2
              val distance1 = (time - speed) * speed
              if(distance1 < distance) {
                  lo = speed + 1
              } else {
                  high = speed - 1
              }

          }

        lo.println()
//        high.println()


    }
    fun part1(input: List<String>): Int {
        val numbersMatch = Regex("\\d+")
        val time = numbersMatch.findAll(input[0]).map { it.value.toInt() }
        val distance = numbersMatch.findAll(input[1]).map { it.value.toInt() }
        val timeBestDistance = time.zip(distance).toList()
        return  timeBestDistance.map { (time, distance) -> (0..time).map { speed -> (time - speed) * speed }.count { it > distance } }.reduce { acc, e -> acc * e }
    }

    fun part2(input: List<String>): Int {
        val numbersMatch = Regex("\\d+")
        val time = numbersMatch.findAll(input[0]).map { it.value }.toList().joinToString(separator = "").toLong()
        val distance = numbersMatch.findAll(input[1]).map { it.value}.toList().joinToString(separator = "").toLong()
        return (0..time).map { speed -> (time - speed) * speed }.count { it > distance }
    }

    val input = readInput("day6/day06")
    part1(input).println()
    part2(input).println()

}
