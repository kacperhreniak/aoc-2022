package day8

import readInput

private var counter: Int = 0

private fun part1(input: List<String>): Int {
    val grid: Array<IntArray> = Array(input.size) { IntArray(input[0].length) }
    val added: Array<BooleanArray> = Array(input.size) { BooleanArray(input[0].length) }

    for (row in input.indices) {
        for (col in 0 until input[row].length) {
            grid[row][col] = input[row][col].digitToInt()
        }
    }
    counter = 2 * grid[0].size + 2 * (grid.size - 2)

    for (row in 1 until grid.size - 1) {
        helperHorizontalLeft(row, 1, grid, grid[row][0], added)
        helperHorizontalRight(row, grid[0].size - 2, grid, grid[row][grid[0].size - 1], added)
    }

    for (col in 1 until grid[0].size - 1) {
        helperVerticalUp(1, col, grid, grid[0][col], added)
        helperVerticalDown(grid.size - 2, col, grid, grid[grid.size - 1][col], added)
    }
    return counter
}

private fun helperHorizontalLeft(row: Int, col: Int, grid: Array<IntArray>, max: Int, added: Array<BooleanArray>) {
    checkPartOne(row, col, grid, max, added) {
        helperHorizontalLeft(row, col + 1, grid, it, added)
    }
}

private fun helperHorizontalRight(row: Int, col: Int, grid: Array<IntArray>, max: Int, added: Array<BooleanArray>) {
    checkPartOne(row, col, grid, max, added) {
        helperHorizontalRight(row, col - 1, grid, it, added)
    }
}

private fun helperVerticalDown(row: Int, col: Int, grid: Array<IntArray>, max: Int, added: Array<BooleanArray>) {
    checkPartOne(row, col, grid, max, added) {
        helperVerticalDown(row - 1, col, grid, it, added)
    }
}

private fun helperVerticalUp(row: Int, col: Int, grid: Array<IntArray>, max: Int, added: Array<BooleanArray>) {
    checkPartOne(row, col, grid, max, added) {
        helperVerticalUp(row + 1, col, grid, it, added)
    }
}

private fun checkPartOne(
    row: Int,
    col: Int,
    grid: Array<IntArray>,
    max: Int,
    added: Array<BooleanArray>,
    nextCall: (max: Int) -> Unit
) {
    if (row <= 0 || col <= 0 || row >= grid.size - 1 || col >= grid[0].size - 1) return

    val current = grid[row][col]
    if (current > max && !added[row][col]) {
        counter++
        added[row][col] = true
    }
    val nextMax = Math.max(max, current)
    nextCall(nextMax)
}

private fun part2(input: List<String>): Int {
    val grid: Array<IntArray> = Array(input.size) { IntArray(input[0].length) }

    for (row in input.indices) {
        for (col in 0 until input[row].length) {
            grid[row][col] = input[row][col].digitToInt()
        }
    }
    var max = 0
    for (row in 1 until grid.size - 1) {
        for (col in 1 until grid[0].size - 1) {
            max = max.coerceAtLeast(helperPartTwo(row, col, grid))
        }
    }
    return max
}

private fun helperPartTwo(row: Int, col: Int, grid: Array<IntArray>): Int {
    val current = grid[row][col]
    val left = helperLeft(row, col - 1, grid, current)
    val right = helperRight(row, col + 1, grid, current)
    val top = helperUp(row + 1, col, grid, current)
    val bottom = helperDown(row - 1, col, grid, current)

    return left * top * right * bottom
}

private fun helperLeft(row: Int, col: Int, grid: Array<IntArray>, max: Int): Int {
    if (row < 0 || col < 0 || row > grid.size - 1 || col > grid[0].size - 1) return 0
    if (grid[row][col] >= max) return 1

    return 1 + helperLeft(row, col - 1, grid, max)
}

private fun helperRight(row: Int, col: Int, grid: Array<IntArray>, max: Int): Int {
    if (row < 0 || col < 0 || row > grid.size - 1 || col > grid[0].size - 1) return 0
    if (grid[row][col] >= max) return 1

    return 1 + helperRight(row, col + 1, grid, max)
}

private fun helperDown(row: Int, col: Int, grid: Array<IntArray>, max: Int): Int {
    if (row < 0 || col < 0 || row > grid.size - 1 || col > grid[0].size - 1) return 0
    if (grid[row][col] >= max) return 1

    return 1 + helperDown(row - 1, col, grid, max)
}

private fun helperUp(row: Int, col: Int, grid: Array<IntArray>, max: Int): Int {
    if (row < 0 || col < 0 || row > grid.size - 1 || col > grid[0].size - 1) return 0
    if (grid[row][col] >= max) return 1

    return 1 + helperUp(row + 1, col, grid, max)
}

fun main() {
    val input = readInput("day8/input")
    println(part1(input))
    println(part2(input))
}
