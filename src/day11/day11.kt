package day11

import readInput
import java.util.*

private fun parse(input: List<String>): List<Monkey> {

    fun parseItems(line: String): MutableList<Long> {
        val result = mutableListOf<Long>()
        val parts = line.split(":")
        val items = parts[1].split(",")
        for (part in items) {
            result.add(part.substring(1).toLong())
        }
        return result
    }

    fun operation(line: String): (item: Long) -> Long {
        val parts = line.split(": ")
        val items = parts[1].split("= ")[1].split(" ")


        return { value ->
            val secondItem = when (items[2]) {
                "old" -> value
                else -> items[2].toLong()
            }

            when (items[1]) {
                "*" -> value * secondItem
                "+" -> value + secondItem
                else -> throw NullPointerException()
            }
        }
    }

    fun divideBy(line: String): Int {
        val parts = line.split("by ")
        return parts[1].toInt()
    }

    fun action(line: String): Int {
        val parts = line.split("to monkey ")
        return parts[1].toInt()
    }

    val result = mutableListOf<Monkey>()
    var index = 0

    while (index < input.size) {
        if (input[index] == "") index++
        val id = result.size
        val items = parseItems(input[index + 1])
        val operation = operation(input[index + 2])
        val division = divideBy(input[index + 3])
        val ifTrue = action(input[index + 4])
        val ifFalse = action(input[index + 5])

        result.add(
            Monkey(
                id = id,
                items = items,
                operation = operation,
                divideBy = division,
                policy = Pair(ifTrue, ifFalse)
            )
        )
        index += 6
    }

    return result
}

private fun solution(input: List<String>, iterationCount: Int, isSecondPart: Boolean): Long {
    val monkeys = parse(input)
    val common = monkeys.map { it.divideBy }.reduce { a, b -> a * b }
    for (round in 0 until iterationCount) {
        for (monkey in monkeys) {
            for (item in monkey.items) {
                monkey.counter++
                val temp = monkey.operation(item)
                val newWorryLevel = if (isSecondPart) temp % common else temp / 3
                if (newWorryLevel % monkey.divideBy == 0L) {
                    monkeys[monkey.policy.first].items.add(newWorryLevel)
                } else {
                    monkeys[monkey.policy.second].items.add(newWorryLevel)
                }
            }

            monkey.items = mutableListOf()
        }
    }

    val queue = PriorityQueue<Monkey>(monkeys.size, compareByDescending { it.counter })
    for (item in monkeys) {
        queue.add(item)
    }

    val first = queue.poll().counter
    val second = queue.poll().counter

    return first.toLong() * second.toLong()
}

private fun part1(input: List<String>): Long {
    return solution(input, 20, false)
}

private fun part2(input: List<String>): Long {
    return solution(input, 10_000, true)
}

data class Monkey(
    var counter: Int = 0,
    val id: Int,
    var items: MutableList<Long>,
    val operation: (item: Long) -> Long,
    val divideBy: Int,
    val policy: Pair<Int, Int>
)

fun main() {
    val input = readInput("day11/input")
    println(part1(input))
    println(part2(input))
}
