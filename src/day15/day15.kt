package day15

import readInput
import kotlin.math.abs

private fun parse(input: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    return input.map { it.split(" ", ",", "=", ":") }
        .map {
            val startPoint = Pair(it[3].toInt(), it[6].toInt())
            val beaconPoint = Pair(it[13].toInt(), it[16].toInt())
            Pair(startPoint, beaconPoint)
        }
}

private fun solution(
    input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>,
    rowIndex: Int
): Pair<HashSet<Int>, HashSet<Int>> {
    val columns = HashSet<Int>()
    val itemsInRow = HashSet<Int>()

    for (item in input) {
        if (item.first.second == rowIndex) {
            itemsInRow.add(item.first.first)
        } else if (item.second.second == rowIndex) {
            itemsInRow.add(item.second.first)
        }

        val distance = abs(item.first.first - item.second.first) + abs(item.first.second - item.second.second)
        val sourceRowIndex = item.first.second
        val sourceColIndex = item.first.first
        val moves = distance - abs(rowIndex - sourceRowIndex)
        if (moves < 0) continue

        val startIndex = sourceColIndex - moves
        val endIndex = sourceColIndex + moves

        for (index in startIndex..endIndex) {
            columns.add(index)
        }
    }
    return Pair(columns, itemsInRow)
}

private fun part1(input: List<String>): Int {
    val parsedInput = parse(input)
    val result = solution(parsedInput, 2_000_000)
    return result.first.size - result.second.size
}

private fun solution2(
    input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>,
    range: IntRange
): Long {
    var row = 0
    var col = 0

    while (row <= range.last) {
        var found = false
        for (item in input) {
            val distance = abs(item.first.first - item.second.first) + abs(item.first.second - item.second.second)
            val sourceRowIndex = item.first.second
            val sourceColIndex = item.first.first
            val moves = distance - abs(row - sourceRowIndex)
            if (moves < 0) continue
            if (sourceColIndex - moves <= col && sourceColIndex + moves >= col) {
                col = sourceColIndex + moves + 1

                if (col >= range.last + 1) {
                    col = 0
                    row++
                }
                found = true
                continue
            }
        }
        if (found.not()) return col.toLong() * MULTIPLIER + row
    }
    return Long.MIN_VALUE
}

const val MAX_RANGE = 4_000_000
const val MULTIPLIER = 4_000_000

private fun part2(input: List<String>, scope: IntRange): Long {
    val parsedInput = parse(input)
    return solution2(parsedInput, scope)
}

fun main() {
    val input = readInput("day15/input")
    val scope = IntRange(0, MAX_RANGE)

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input, scope)}")
}
