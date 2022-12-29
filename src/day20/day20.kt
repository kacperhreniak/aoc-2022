package day20

import readInput

private fun parseInput(input: List<String>, multiplier: Long = 1L): List<Pair<Int, Long>> =
    input.mapIndexed { index, value -> Pair(index, multiplier * value.toLong()) }

private fun decrypt(data: List<Pair<Int, Long>>): List<Pair<Int, Long>> {
    val result = data.toMutableList()
    data.indices.forEach { iteration ->
        val index = result.indexOfFirst { it.first == iteration }
        val item = result[index]
        result.removeAt(index)
        val nextIndex = (index + item.second).mod(result.size)
        result.add(nextIndex, item)

    }
    return result
}

private fun solution(data: List<Pair<Int, Long>>): Long {
    val zero = data.indexOfFirst { it.second == 0L }
    return listOf(1000, 2000, 3000).sumOf { data[(zero + it) % data.size].second }
}

private fun part1(input: List<String>): Long {
    val data = parseInput(input)
    val mixed = decrypt(data)
    return solution(mixed)
}

private const val MULTIPLIER = 811589153
private fun part2(input: List<String>): Long {
    val data = parseInput(input, MULTIPLIER.toLong())
    var mixed = data
    repeat(10) { mixed = decrypt(mixed) }
    return solution(mixed)
}

fun main() {
    val input = readInput("day20/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
