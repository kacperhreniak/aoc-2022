package day18

import readInput

private fun parseInput(input: List<String>): List<IntArray> =
    input.map { line ->
        val parts = line.split(",")
        val result = IntArray(3)
        repeat(3) { result[it] = parts[it].toInt().coerceAtLeast(result[it]) }
        result
    }

private fun getMaxes(input: List<IntArray>): IntArray {
    val result = IntArray(3)
    input.forEach { line ->
        for (index in line.indices) {
            result[index] = line[index].coerceAtLeast(result[index])
        }
    }
    return result
}

private fun Array<Array<BooleanArray>>.updateBoard(input: List<IntArray>) {
    for (line in input) {
        this[line[0]][line[1]][line[2]] = true
    }
}

fun neighbours(x: Int, y: Int, z: Int): List<IntArray> {
    val moves = listOf(
        intArrayOf(0, 0, 1),
        intArrayOf(0, 0, -1),
        intArrayOf(1, 0, 0),
        intArrayOf(-1, 0, 0),
        intArrayOf(0, -1, 0),
        intArrayOf(0, 1, 0),
    )

    return moves.map {
        it[0] += x
        it[1] += y
        it[2] += z
        it
    }
}

private fun calculatePartOne(board: Array<Array<BooleanArray>>): Int {
    fun isFreeSpace(x: Int, y: Int, z: Int): Boolean {
        if (x < 0 || y < 0 || z < 0 || x >= board[0].size || y >= board[0][0].size || z >= board.size) {
            return true
        }
        return board[z][x][y].not()
    }

    fun calculatePoint(x: Int, y: Int, z: Int): Int {
        return neighbours(x, y, z).count { isFreeSpace(it[1], it[2], it[0]) }
    }

    var result = 0
    for (z in board.indices) {
        for (x in board[z].indices) {
            for (y in board[z][x].indices) {
                if (board[z][x][y]) {
                    result += calculatePoint(x, y, z)
                }
            }
        }
    }
    return result
}

private fun part1(input: List<String>): Int {
    val parsedInput = parseInput(input)
    val sizes = getMaxes(parsedInput)
    val board = Array(sizes[0] + 1) { Array(sizes[1] + 1) { BooleanArray(sizes[2] + 1) } }
    board.updateBoard(parsedInput)
    return calculatePartOne(board)
}

private fun calculatePartTwo(board: Array<Array<BooleanArray>>): Int {
    val outside = hashSetOf<Triplet>()
    val queue = mutableListOf(Triplet(-1, -1, -1)).also {
        outside.add(it[0])
    }
    while (queue.isNotEmpty()) {
        val item = queue.removeLast()
        val neighbours = neighbours(item.first, item.second, item.third)
            .asSequence()
            .filter { it[0] in -1..board.size }
            .filter { it[1] in -1..board[0].size }
            .filter { it[2] in -1..board[0][0].size }
            .filter {
                it[0] == -1 || it[1] == -1 || it[2] == -1 ||
                        it[0] == board.size || it[1] == board[0].size || it[2] == board[0][0].size ||
                        board[it[0]][it[1]][it[2]].not()
            }

        neighbours.forEach {
            val item = Triplet(it[0], it[1], it[2])
            if (outside.contains(item).not()) {
                outside.add(item)
                queue.add(item)
            }
        }
    }

    var result = 0
    for (x in board.indices) {
        for (y in board[0].indices) {
            for (z in board[0][0].indices) {
                if (board[x][y][z]) {
                    result += neighbours(x, y, z).count { Triplet(it[0], it[1], it[2]) in outside }
                }
            }
        }
    }
    return result
}

private fun part2(input: List<String>): Int {
    val parsedInput = parseInput(input)
    val sizes = getMaxes(parsedInput)
    val board = Array(sizes[0] + 1) { Array(sizes[1] + 1) { BooleanArray(sizes[2] + 1) } }
    board.updateBoard(parsedInput)

    return calculatePartTwo(board)
}

fun main() {
    val input = readInput("day18/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

data class Triplet(
    val first: Int,
    val second: Int,
    val third: Int,
)