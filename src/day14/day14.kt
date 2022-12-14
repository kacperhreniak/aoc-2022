package day14

import readInput

private fun parse(input: List<String>): Pair<Array<CharArray>, IntRange> {
    var left = Int.MAX_VALUE
    var right = Int.MIN_VALUE
    var top = Int.MIN_VALUE

    for (line in input) {
        val parts = line.split(" -> ")
        for (item in parts) {
            val coordinates = item.split(",")
            left = left.coerceAtMost(coordinates[0].toInt())
            right = right.coerceAtLeast(coordinates[0].toInt())
            top = top.coerceAtLeast(coordinates[1].toInt())
        }
    }
    val startPoint = Pair(0, 500)
    val result = Array(top + 1) { CharArray(1000) { '.' } }
    result[startPoint.first][startPoint.second] = '+'

    for (line in input) {
        val parts = line.split(" -> ")
        for (index in 0 until parts.size - 1) {
            val start = parts[index].split(",")
            val end = parts[index + 1].split(",")


            if (start[0] == end[0]) {
                val startRow = start[1].coerceAtMost(end[1]).toInt()
                val endRow = start[1].coerceAtLeast(end[1]).toInt()
                for (index in startRow..endRow) {
                    result[index][start[0].toInt()] = '#'
                }
            } else if (start[1] == end[1]) {
                val startCol = start[0].coerceAtMost(end[0]).toInt()
                val endCol = start[0].coerceAtLeast(end[0]).toInt()
                for (index in startCol..endCol) {
                    result[start[1].toInt()][index] = '#'
                }
            } else throw IllegalStateException()
        }
    }

    return Pair(result, IntRange(left, right))
}

private fun play(grid: Array<CharArray>, startPoint: Pair<Int, Int>, range: IntRange): Int {
    var condition = true
    var counter = 0
    while (condition) {
        val temp = helper(startPoint.first, startPoint.second, grid, range)
        if (temp.first) counter++
        condition = temp.first && temp.second
    }
    return counter
}

private fun helper(row: Int, col: Int, grid: Array<CharArray>, range: IntRange): Pair<Boolean, Boolean> {
    if (col < range.first || col > range.last) return Pair(false, false)
    if (row >= grid.size) return Pair(false, true)
    if (grid[row][col] != '.' && grid[row][col] != '+') return Pair(false, true)

    val moves = listOf(
        { helper(row + 1, col, grid, range) },
        { helper(row + 1, col - 1, grid, range) },
        { helper(row + 1, col + 1, grid, range) }
    )

    moves.forEach {
        val temp = it.invoke()
        if (temp.second.not()) return Pair(false, false)
        if (temp.first) return Pair(true, true)
    }
    grid[row][col] = 'O'
    return Pair(true, true)
}

private fun part1(input: List<String>): Int {
    val parsedInput = parse(input)
    return play(parsedInput.first, Pair(0, 500), parsedInput.second)
}

private fun part2(input: List<String>): Int {
    val parsedInput = parse(input)
    val normalizedInput = normalizedInput(parsedInput.first)
    return play(normalizedInput, Pair(0, 500), IntRange(0, 1000))
}

private fun normalizedInput(grid: Array<CharArray>): Array<CharArray> {
    val newGrid = Array(grid.size + 2) { CharArray(grid[0].size) { '.' } }

    for ((rowIndex, row) in grid.withIndex()) {
        for ((index, col) in row.withIndex()) {
            newGrid[rowIndex][index] = col
        }
    }

    for (index in 0 until newGrid[0].size) {
        newGrid.last()[index] = '#'
    }

    return newGrid
}

fun main() {
    val input = readInput("day14/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
