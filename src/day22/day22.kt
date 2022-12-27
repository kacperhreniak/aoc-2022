package day22

import readInput

private fun parseBoard(input: List<String>): Array<CharArray> {
    val maxSize = input.maxBy { it.length }.length
    val board = Array(input.size - 2) { CharArray(maxSize) { '|' } }

    input.forEachIndexed { rowIndex, line ->
        for ((colIndex, item) in line.withIndex()) {
            if (item == '.' || item == '#') board[rowIndex][colIndex] = item
        }
    }

    return board
}

private fun parseMovement(input: List<String>): List<Move> {
    fun Direction.rotateRight(): Direction = when (this) {
        Direction.LEFT  -> Direction.UP
        Direction.RIGHT -> Direction.DOWN
        Direction.UP    -> Direction.RIGHT
        Direction.DOWN  -> Direction.LEFT
    }

    fun Direction.rotateLeft(): Direction = when (this) {
        Direction.LEFT  -> Direction.DOWN
        Direction.DOWN  -> Direction.RIGHT
        Direction.RIGHT -> Direction.UP
        Direction.UP    -> Direction.LEFT
    }

    val movements = mutableListOf<Move>()

    var startIndex = 0
    val line = input.last()
    var direction = Direction.RIGHT

    for ((index, item) in line.withIndex()) {
        if (item == 'R' || item == 'L') {
            movements.add(
                Move(steps = line.substring(startIndex, index).toInt(), direction)
            )
            direction = if (item == 'R') direction.rotateRight() else direction.rotateLeft()
            startIndex = index + 1
        }
    }
    movements.add(Move(steps = line.substring(startIndex).toInt(), direction))
    return movements
}

private fun startPoint(input: List<String>): Pair<Int, Int> {
    for ((index, item) in input[0].withIndex()) {
        if (item == '.') {
            return Pair(0, index)
        }
    }
    throw IllegalStateException()
}

private fun calculateResult(row: Int, col: Int, direction: Direction): Int {
    return 1000 * (row + 1) + 4 * (col + 1) + direction.ordinal
}

private fun solutionPartOne(
    startPoint: Pair<Int, Int>,
    board: Array<CharArray>,
    strategies: List<Move>
): Int {

    fun handleStrategy(point: Pair<Int, Int>, move: Move): Pair<Int, Int> {
        fun getItem(point: Pair<Int, Int>): Char = board[point.first][point.second]

        fun newInEdge(point: Pair<Int, Int>): Pair<Int, Int> = if (point.first < 0) {
            point.copy(first = board.size - 1)
        } else if (point.first >= board.size) {
            point.copy(first = 0)
        } else if (point.second < 0) {
            point.copy(second = board[0].size - 1)
        } else if (point.second >= board[0].size) {
            point.copy(second = 0)
        } else point

        fun Pair<Int, Int>.next(direction: Direction): Pair<Int, Int> {
            val result = when (direction) {
                Direction.LEFT  -> this.copy(second = this.second - 1)
                Direction.RIGHT -> this.copy(second = this.second + 1)
                Direction.UP    -> this.copy(first = this.first - 1)
                Direction.DOWN  -> this.copy(first = this.first + 1)
            }
            return newInEdge(result)
        }

        fun findNewStartPosition(startPoint: Pair<Int, Int>): Pair<Int, Int> {
            var next = startPoint
            while (getItem(next) == '|') {
                next = next.next(move.direction)
            }
            return next
        }

        var counter = move.steps
        var next = point

        while (counter > 0) {
            val previous = next
            next = next.next(move.direction)

            if (getItem(next) == '|') {
                next = findNewStartPosition(next)
            }

            if (getItem(next) == '#') {
                next = previous
                break
            }
            counter--
        }
        return next
    }

    var point = startPoint
    for (strategy in strategies) {
        point = handleStrategy(point, strategy)
    }

    return calculateResult(point.first, point.second, strategies.last().direction)
}


private fun part1(input: List<String>): Int {
    val board = parseBoard(input)
    val startPoint = startPoint(input)
    val strategy = parseMovement(input)
    return solutionPartOne(startPoint, board, strategy)
}

private fun part2(input: List<String>): Int {
    return 0
}

data class Move(
    val steps: Int,
    val direction: Direction
)

enum class Direction {
    RIGHT,
    DOWN,
    LEFT,
    UP
}

fun main() {
    val input = readInput("day22/test-input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}