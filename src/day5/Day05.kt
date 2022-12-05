package day5

import readInput

const val NEXT_ELEMENT_DISTANCE = 4
const val ITERATION_NUMBER_INDEX = 1
const val FROM_INDEX = 3
const val TO_INDEX = 5

private fun createInitialStacks(initialInput: List<String>): Array<ArrayDeque<Char>> {
    val max = initialInput.last().split(" ").last()
    val stacks = Array(max.toInt()) { ArrayDeque<Char>() }

    for (index in 0 until initialInput.size - 1) {
        val line = initialInput[index]

        var item = 1
        while (item < line.length) {
            if (line[item] != ' ') {
                stacks[item / NEXT_ELEMENT_DISTANCE].addLast(line[item])
            }
            item += NEXT_ELEMENT_DISTANCE
        }
    }

    return stacks
}

private fun part1(initialInput: List<String>, input: List<String>): String {
    val stacks = createInitialStacks(initialInput)
    for (line in input) {
        val parts = line.split(" ")

        for (index in 0 until parts[ITERATION_NUMBER_INDEX].toInt()) {
            val item = stacks[parts[FROM_INDEX].toInt() - 1].removeFirst()
            stacks[parts[TO_INDEX].toInt() - 1].addFirst(item)
        }
    }

    return stacks.fold("") { result, stack -> result + stack.firstOrNull()!! }
}

private fun part2(initialInput: List<String>, input: List<String>): String {
    val stacks = createInitialStacks(initialInput)

    for (line in input) {
        val parts = line.split(" ")
        val temp = ArrayDeque<Char>()
        for (index in 0 until parts[ITERATION_NUMBER_INDEX].toInt()) {
            val item = stacks[parts[FROM_INDEX].toInt() - 1].removeFirst()
            temp.addFirst(item)
        }

        while (temp.isNotEmpty()) {
            val item = temp.removeFirst()
            stacks[parts[TO_INDEX].toInt() - 1].addFirst(item)
        }
    }
    return stacks.fold("") { result, stack -> result + stack.firstOrNull()!! }
}

fun main() {
    val input = readInput("day5/input")
    val initialInput = readInput("day5/initial-input")
    println(part1(initialInput, input))
    println(part2(initialInput, input))
}
