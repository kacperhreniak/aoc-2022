package day21

import readInput

private fun parse(input: List<String>): Map<String, Operation> {
    return input.associate {
        val parts = it.split(": ")
        val key = parts[0]
        val operation = if (parts[1].contains(" + ")) {
            val items = parts[1].split(" + ")
            Operation.Add(items[0], items[1])
        } else if (parts[1].contains(" - ")) {
            val items = parts[1].split(" - ")
            Operation.Subtract(items[0], items[1])
        } else if (parts[1].contains(" / ")) {
            val items = parts[1].split(" / ")
            Operation.Divide(items[0], items[1])
        } else if (parts[1].contains(" * ")) {
            val items = parts[1].split(" * ")
            Operation.Multiply(items[0], items[1])
        } else Operation.Yell(parts[1].toLong())
        key to operation
    }
}

private fun findNumber(
    data: Map<String, Operation>,
    key: String
): Long {
    return when (val operation = data[key]!!) {
        is Operation.Yell     -> operation.item
        is Operation.Subtract -> {
            findNumber(data, operation.first) - findNumber(data, operation.second)
        }
        is Operation.Add      -> {
            findNumber(data, operation.first) + findNumber(data, operation.second)
        }
        is Operation.Divide   -> {
            findNumber(data, operation.first) / findNumber(data, operation.second)
        }
        is Operation.Multiply -> {
            findNumber(data, operation.first) * findNumber(data, operation.second)
        }
    }
}

private const val START_KEY = "root"
private fun part1(input: List<String>): Long {
    val data = parse(input)
    return findNumber(data, START_KEY)
}

private fun findPath(key: String, point: String, data: Map<String, Operation>, path: List<String>): List<String>? {
    if (key == point) return path + key

    val temp = data[point]
    if (temp !is Operation.MathOperation) return null

    val newpPath = path + point
    return findPath(key, temp.first, data, newpPath)
        ?: findPath(key, temp.second, data, newpPath)
}

private const val KEY_TO_HANDLE = "humn"
private fun part2(input: List<String>): Long {
    fun findNewValue(operation: Operation.MathOperation, isFirst: Boolean, result: Long, second: Long) = when (operation) {
        is Operation.Add      -> result - second
        is Operation.Multiply -> result / second
        is Operation.Subtract -> if (isFirst) result + second else second - result
        is Operation.Divide   -> if (isFirst) result * second else result / second
    }

    val data = parse(input).toMutableMap()
    val rootOperation = data[START_KEY] as Operation.MathOperation

    val path = findPath(KEY_TO_HANDLE, rootOperation.first, data, listOf()).orEmpty()
    var value = findNumber(data, rootOperation.second)

    for ((index, key) in path.withIndex()) {
        if (key == KEY_TO_HANDLE) return value
        val operation = data[key] as Operation.MathOperation

        val isFirst = operation.first == path[index + 1]

        val second = if (isFirst) {
            findNumber(data, operation.second)
        } else findNumber(data, operation.first)
        value = findNewValue(operation, isFirst, value, second)
    }

    return (data[KEY_TO_HANDLE] as Operation.Yell).item
}

sealed interface Operation {
    data class Yell(val item: Long) : Operation

    sealed interface MathOperation : Operation {
        val first: String
        val second: String
    }

    data class Add(override val first: String, override val second: String) : MathOperation
    data class Subtract(override val first: String, override val second: String) : MathOperation
    data class Multiply(override val first: String, override val second: String) : MathOperation
    data class Divide(override val first: String, override val second: String) : MathOperation
}

fun main() {
    val input = readInput("day21/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}