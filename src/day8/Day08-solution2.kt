package day8

import readInput

private fun part1(input: List<String>): Int {
    val grid: Array<IntArray> = Array(input.size) { IntArray(input[0].length) }
    for (row in input.indices) {
        for (col in 0 until input[row].length) {
            grid[row][col] = input[row][col].digitToInt()
        }
    }

    val highestInRow: Array<IntArray> = Array(grid.size) { IntArray(grid[0].size) { Int.MIN_VALUE } }
    val highestInCol: Array<IntArray> = Array(grid.size) { IntArray(grid[0].size) { Int.MIN_VALUE } }
    val added: MutableSet<String> = hashSetOf()

    for (row in grid.indices) {
        highestInRow[row][0] = Int.MIN_VALUE
        highestInRow[row][1] = grid[row][0]
        for (col in 2 until grid[0].size) {
            highestInRow[row][col] = highestInRow[row][col - 1].coerceAtLeast(grid[row][col - 1])
        }
    }
    for (col in grid[0].indices) {
        highestInCol[0][col] = Int.MIN_VALUE
        highestInCol[1][col] = grid[0][col]
        for (row in 2 until grid.size) {
            highestInCol[row][col] = highestInCol[row - 1][col].coerceAtLeast(grid[row - 1][col])
        }
    }
    for (row in grid.size - 2 downTo 1) {
        var rightMax = grid[row][grid[0].size - 1]
        for (col in grid[0].size - 2 downTo 1) {
            val currentItem = grid[row][col]
            if (currentItem > rightMax || highestInRow[row][col] < currentItem) {
                added.add("R$row-C$col")
            }
            rightMax = rightMax.coerceAtLeast(currentItem)
        }
    }

    for (col in grid[0].size - 2 downTo 1) {
        var bottomMax = grid[grid.size - 1][col]
        for (row in grid.size - 2 downTo 1) {
            val currentItem = grid[row][col]

            if (currentItem > bottomMax || highestInCol[row][col] < currentItem) {
                added.add("R$row-C$col")
            }
            bottomMax = bottomMax.coerceAtLeast(currentItem)
        }
    }

    return added.size + (2 * grid.size + 2 * (grid.size - 2))
}

fun main() {
    val input = readInput("day8/input")
    println(part1(input))
}
