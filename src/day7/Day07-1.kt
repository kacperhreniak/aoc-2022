package day7

import readInput

private fun handle(input: List<String>, handler: (value: Int) -> Unit) {
    val stack = ArrayDeque<Int>()
    stack.addFirst(0)

    for (line in input) {
        if (line.startsWith("$ cd ..")) {
            val temp = stack.removeFirst()
            handler(temp)
            val newItem = (stack.removeFirstOrNull() ?: 0) + temp
            stack.addFirst(newItem)
        } else if (line.startsWith("$ cd /")) {
            popStack(stack, handler)
        } else if (line.startsWith("$ cd")) {
            stack.addFirst(0)
        } else if (line.startsWith("$ ls").not() && line.startsWith("dir").not()) {
            val temp = stack.removeFirstOrNull() ?: continue
            val size = temp + line.split(" ")[0].toInt()
            stack.addFirst(size)
        }
    }
    popStack(stack, handler)
}

private fun popStack(stack: ArrayDeque<Int>, handler: (value: Int) -> Unit) {
    while (stack.size > 0) {
        val temp = stack.removeFirstOrNull() ?: break
        handler(temp)
        if (stack.size != 0) {
            val newItem = stack.removeFirst() + temp
            stack.addFirst(newItem)
        }
    }
}

private fun part1(input: List<String>): Int {
    var result = 0
    handle(input) {
        if (it <= 100_000) result += it
    }
    return result
}

const val MAX_SIZE = 70_000_000
const val REQUIRED = 30_000_000
private fun part2(input: List<String>): Int {
    var total = 0
    for (line in input) {
        if (line.startsWith("$").not() && line.startsWith("dir").not()) {
            total += line.split(" ")[0].toInt()
        }
    }
    val freeSpace = MAX_SIZE - total
    val requiredSpace = REQUIRED - freeSpace
    var min = Int.MAX_VALUE

    handle(input) {
        if (it in requiredSpace until min) min = it
    }
    return min
}

fun main() {
    val input = readInput("day7/input")
    println(part1(input))
    println(part2(input))
}
