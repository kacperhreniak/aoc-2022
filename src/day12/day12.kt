package day12

import readInput

private fun parse(input: List<String>): Array<CharArray> {
    val result = Array(input.size) { CharArray(input[0].length) }

    for ((row, line) in input.withIndex()) {
        for ((col, item) in line.withIndex()) {
            result[row][col] = item
        }
    }
    return result
}

private fun helperBFS(
    grid: Array<CharArray>,
    inputs: Set<Char>
): Int {
    val queue = ArrayDeque<Input>()

    val visited = Array<BooleanArray>(grid.size) { BooleanArray(grid[0].size) }
    for (row in 0 until grid.size) {
        for (col in 0 until grid[0].size) {
            val item = grid[row][col]
            if (inputs.contains(item)) queue.addFirst(Input(row, col, 0))
        }
    }
    var min = Int.MAX_VALUE
    while (queue.isNotEmpty()) {
        val item = queue.removeLast()
        if (visited[item.row][item.col]) continue
        val currentChar = grid[item.row][item.col]
        if (currentChar == 'E') {
            return item.counter
        }
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
            val value = (if (nextItem == 'E') 'z' else nextItem) - if (currentChar == 'S') 'a' else currentChar
            if (value <= 1) {
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
    return min
}

private data class Input(
    val row: Int,
    val col: Int,
    val counter: Int
)

private fun part1(input: List<String>): Int {
    val grid = parse(input)
    return helperBFS(grid, setOf('S'))
}

private fun part2(input: List<String>): Int {
    val grid = parse(input)
    return helperBFS(grid, setOf('S', 'a'))
}

fun main() {
    val input = readInput("day12/input")
    println(part1(input))
    println(part2(input))
}
