package day13

import day13.Package.Companion.convert
import day13.Package.Items
import day13.Package.Value
import readInput

private fun parse(input: List<String>): List<Pair<Package, Package>> {
    val result = mutableListOf<Pair<Package, Package>>()
    var index = 0
    while (index < input.size) {
        val first = parsePackage(input[index++]).second
        val second = parsePackage(input[index++]).second

        result.add(Pair(first, second))
        index++
    }
    return result
}

private fun parsePackage(input: String, startIndex: Int = 1): Pair<Int, Package> {
    val result = mutableListOf<Package>()
    var index = startIndex

    while (index < input.length) {
        val item = input[index++]
        if (item == '[') {
            val temp = parsePackage(input, index)
            index = temp.first
            result.add(temp.second)
        } else if (item == ']') {
            return Pair(index + 1, Items(result))
        } else if (item.isDigit()) {
            result.add(Value(item.digitToInt()))
        }
    }

    return Pair(index, Items(result))
}

private fun verify(firstInput: Package, secondInput: Package): Int {
    if (firstInput == secondInput) return 0
    var first = firstInput
    var second = secondInput

    if (firstInput is Value && secondInput is Value) {
        if (firstInput.value == secondInput.value) return 0
        return if (firstInput.value < secondInput.value) 1 else -1
    }else if (firstInput is Value) {
        first = Items(listOf(firstInput))
    } else if(second is Value) {
        second = Items(listOf(secondInput))
    }

    require(first is Items && second is Items)

    var index = 0
    while (index < first.items.size.coerceAtLeast(second.items.size)) {
        if (index == first.items.size) return 1
        if (index == second.items.size) return -1

        val temp = verify(first.items[index], second.items[index])
        if (temp != 0) return temp
        index++
    }
    return 0
}

private fun handle(input: List<String>): Int {
    val packages = parse(input)
    var result = 0

    for ((index, item) in packages.withIndex()) {
        val temp = verify(item.first, item.second)
        if (temp >= 0) {
            println("Result is $temp for index $index Should add it?: ${temp >= 0}")
            result += index + 1
        }
    }
    return result
}

private fun part1(input: List<String>): Int {
    return handle(input)
}

private fun part2(input: List<String>): Int {
    return 0
}

fun main() {
    val input = readInput("day13/test-input")
    println("Part 1 is ${part1(input)}")
    println(part2(input))
}

private sealed interface Package {
    data class Value(val value: Int) : Package
    data class Items(val items: List<Package>) : Package

    companion object {
        fun Value.convert() = Items(listOf(this))
    }
}