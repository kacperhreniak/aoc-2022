package day12

import readInput
import kotlin.math.abs

private fun part1(input: List<String>): Int {
    val grid = parse(input)
    grid.second[grid.first.first][grid.first.second] = 'a'
    return helperBFS(grid.second, grid.first)
}

private fun parse(input: List<String>): Pair<Pair<Int, Int>, Array<CharArray>> {
    val result = Array(input.size) { CharArray(input[0].length) }

    var startPoint: Pair<Int, Int>? = null
    for ((row, line) in input.withIndex()) {
        for ((col, item) in line.withIndex()) {
            if (item == 'S') {
                startPoint = Pair(row, col)
            }
            result[row][col] = item
        }
    }
    return Pair(startPoint!!, result)
}

private fun helperBFS(
    grid: Array<CharArray>,
    startPoint: Pair<Int, Int>,
): Int {
    val queue = ArrayDeque<Input>()
    val input = Input(startPoint.first, startPoint.second, 0)
    queue.addFirst(input)

    val visited = Array<BooleanArray>(grid.size) { BooleanArray(grid[0].size) }
    while (queue.isNotEmpty()) {
        val item = queue.removeLast()
        if (visited[item.row][item.col]) continue
        val currentChar = grid[item.row][item.col]
        if (currentChar == 'E') return item.counter

        visited[item.row][item.col] = true

        val changes = listOf(
            intArrayOf(0, 1),
            intArrayOf(0, -1),
            intArrayOf(1, 0),
            intArrayOf(-1, 0)
        )

        for (change in changes) {
            val newRow = item.row + change[0]
            val newCol = item.col + change[1]
            if (newCol < 0 || newCol > grid[0].size - 1 || newRow < 0 || newRow > grid.size - 1) {
                continue
            }

            val nextItem = grid[newRow][newCol]
            val value = currentChar - (if (nextItem == 'E') 'z' else nextItem)

            if (value == 0 || abs(value) == 1) {
                queue.addFirst(
                    Input(
                        row = newRow,
                        col = newCol,
                        counter = item.counter + 1
                    )
                )
            }
        }
    }
    return Int.MIN_VALUE
}

private data class Input(
    val row: Int,
    val col: Int,
    val counter: Int
)

private fun part2(input: List<String>): Int {
    return 0
}

fun main() {
    val input = readInput("day12/input")
    println(part1(input))
    println(part2(input))
}
