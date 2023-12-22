package main.aoc2023.aoc2023.day20

import main.aoc2023.println
import main.aoc2023.readInput
import java.util.*


enum class Value {
    HIGH, LOW;

    override fun toString(): String {
        return if (this == LOW) "low" else "high"
    }
}

enum class ModuleType {
    BROADCAST, CONJ, FLIP_FLOP;

    companion object {
        fun toType(char: Char? = null): ModuleType {
            if (char == null) return BROADCAST
            return if (char == '&') CONJ else FLIP_FLOP
        }
    }
}

data class Signal(val value: Value, val src: String, val dest: String)

data class Module(
    val input: MutableMap<String, Value> = mutableMapOf(),
    var outputs: List<String>? = null,
    val name: String,
    var value: Value? = null,
    var type: ModuleType? = null
) {

    private fun flipLevel(value: Value?) =
        if (value == Value.LOW || value == null) Value.HIGH else Value.LOW

    fun process(signal: Signal): List<Signal> {
        if (type == ModuleType.BROADCAST) {
            return outputs!!.map { Signal(value = signal.value, src = "broadcast", dest = it) }
        }

        if (type == ModuleType.FLIP_FLOP && signal.value == Value.LOW) {
            value = flipLevel(value)
            val signals: List<Signal> = outputs!!.map {
                Signal(src = name, value = value!!, dest = it)
            }
            return signals
        }

        if (type == ModuleType.CONJ) {
            input[signal.src] = signal.value
            val isAllHigh = input.values.all { it == Value.HIGH }
            val toSend = if (isAllHigh) Value.LOW else Value.HIGH
            return outputs!!.map { Signal(src = name, value = toSend, dest = it) }
        }

        return emptyList()
    }
}

fun buildGraph(
    input: List<String>,
): Map<String, Module> {
    val graph = mutableMapOf<String, Module>()
    val b = "broadcaster"
    input.forEach { line ->

        val (moduleNameStr, outputStr) = line.split(Regex("\\s+->\\s+"))
        val outputs = outputStr.split(",").map { it.trim() }


        if (moduleNameStr.trim() == b) {
            val m = Module(name = moduleNameStr.trim(), outputs = outputs, value = Value.LOW)
            m.type = ModuleType.BROADCAST
            graph[b] = m
            outputs.forEach { outputName ->
                val outPutModule = graph.getOrPut(outputName) {
                    Module(
                        name = outputName,
                        value = Value.LOW
                    )
                }
                outPutModule.input[moduleNameStr] = Value.LOW
            }

        } else {
            val type = moduleNameStr.first()
            val moduleName = moduleNameStr.drop(1)
            val module = graph.getOrPut(moduleName) {
                Module(name = moduleName)
            }

            module.type = ModuleType.toType(type)
            module.outputs = outputs

            outputs.forEach { outputName ->
                val outPutModule = graph.getOrPut(outputName) {
                    Module(name = outputName)
                }
                outPutModule.input[moduleName] = Value.LOW
            }
        }
    }
    return graph
}

fun main() {
    fun buttonPress(graph: Map<String, Module>): List<Signal> {
        val signals = mutableListOf<Signal>()
        val queue =
            ArrayDeque(listOf(Signal(src = "button", value = Value.LOW, dest = "broadcaster")))

        while (queue.isNotEmpty()) {
            val curr = queue.poll()

            signals.add(curr)
            queue.addAll(graph[curr.dest]!!.process(curr))
        }
        return signals
    }


    fun part1(input: List<String>): Long {
        val graph = buildGraph(input)
        return (1..1000)
            .flatMap { buttonPress(graph) }
            .groupingBy { it.value }
            .eachCount()
            .values
            .fold(1L) { acc, l -> acc * l }
    }

       fun part2(input: List<String>): Long {
        val graph = buildGraph(input)

        var isKsOn = false

        var count = 0


        while (true) {
            count += 1
            val signals = buttonPress(graph)

           val low =  signals.any{it == Signal(value = Value.HIGH, src = "pm", dest = "dt" )}
            if(low) {
//                count.println()
                break
            }
        }
        return 0
    }

    val input = readInput("aoc2023/day20/day20")
    part1(input).println()
    part2(input).println()
}
