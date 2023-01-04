package day22

import readInput

private fun Direction.rotateRight(): Direction = when (this) {
    Direction.LEFT  -> Direction.UP
    Direction.RIGHT -> Direction.DOWN
    Direction.UP    -> Direction.RIGHT
    Direction.DOWN  -> Direction.LEFT
}

private fun Direction.rotateLeft(): Direction = when (this) {
    Direction.LEFT  -> Direction.DOWN
    Direction.DOWN  -> Direction.RIGHT
    Direction.RIGHT -> Direction.UP
    Direction.UP    -> Direction.LEFT
}

private fun Direction.makeRotation(char: Char): Direction = when (char) {
    'R'  -> rotateRight()
    'L'  -> rotateLeft()
    else -> throw IllegalArgumentException()
}

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

private fun parseMovement(input: List<String>): List<Action> {
    val movements = mutableListOf<Action>()

    var startIndex = 0
    val line = input.last()

    for ((index, item) in line.withIndex()) {
        if (item == 'R' || item == 'L') {
            movements.add(
                Action.Steps(value = line.substring(startIndex, index).toInt())
            )
            movements.add(Action.Rotate(item))
            startIndex = index + 1
        }
    }
    movements.add(Action.Steps(value = line.substring(startIndex).toInt()))
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

private fun calculateResult(position: Pair<Int, Int>, direction: Direction): Int {
    return 1000 * (position.first + 1) + 4 * (position.second + 1) + direction.ordinal
}

private fun Pair<Int, Int>.getItem(board: Array<CharArray>): Char = board[first][second]

fun strategyPartOne(
    state: State,
    board: Array<CharArray>,
    move: Action.Steps
): State {
    fun newInEdge(point: Pair<Int, Int>): Pair<Int, Int> {
        return if (point.first < 0) {
            point.copy(first = board.size - 1)
        } else if (point.first >= board.size) {
            point.copy(first = 0)
        } else if (point.second < 0) {
            point.copy(second = board[0].size - 1)
        } else if (point.second >= board[0].size) {
            point.copy(second = 0)
        } else point
    }

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
        while (next.getItem(board) == '|') {
            next = next.next(state.direction)
        }
        return next
    }

    var next = state.position
    var counter = move.value
    while (counter > 0) {
        val previous = next
        next = next.next(state.direction)

        if (next.getItem(board) == '|') {
            next = findNewStartPosition(next)
        }

        if (next.getItem(board) == '#') {
            next = previous
            break
        }
        counter--
    }
    return state.copy(
        position = next
    )
}

private fun solution(
    startPoint: Pair<Int, Int>,
    strategies: List<Action>,
    handleStrategy: (state: State, strategy: Action.Steps) -> State
): Int {
    var state = State(
        position = startPoint,
        direction = Direction.RIGHT
    )
    for (strategy in strategies) {
        state = when (strategy) {
            is Action.Steps  -> handleStrategy(state, strategy)
            is Action.Rotate -> state.copy(
                direction = state.direction.makeRotation(char = strategy.char)
            )
        }
        println(state)
    }

    return calculateResult(state.position, state.direction)
}

private fun part1(input: List<String>): Int {
    val startPoint = startPoint(input)
    val board = parseBoard(input)
    val strategies = parseMovement(input)

    val handleStrategy = { state: State, move: Action.Steps ->
        strategyPartOne(state, board, move)
    }
    return solution(startPoint, strategies, handleStrategy)
}

private fun strategyPartTwo(
    state: State,
    board: Array<CharArray>,
    move: Action.Steps
): State {
    var direction = state.direction
    fun newInEdge(point: Pair<Int, Int>): Pair<Int, Int> {
        val bigRow = point.first / 50
        val bigCol = point.second / 50

        val smallRow = point.first - bigRow * 50
        val smallCol = point.second - bigCol * 50

        var next = point

        if (bigRow == 0 && bigCol == 0) {
            if (direction == Direction.LEFT) {
                direction = Direction.RIGHT
                next = Pair(3 * 50 - 1 - smallRow, 0)

                println("1: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else throw IllegalStateException("Point 0,0 shouldn't be here with this direction: $direction")
        } else if (bigCol == 1 && point.first < 0) {
            if (direction == Direction.UP) {
                direction = Direction.RIGHT
                next = Pair(3 * 50 + smallCol, 0)
                println("2: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else throw IllegalStateException("Point 0,0 shouldn't be here with this direction: $direction")
        } else if (bigCol == 2 && point.first < 0) {
            if (direction == Direction.UP) { // Potential place
                direction = Direction.UP
                next = Pair(4 * 50 - 1, smallCol)
                println("3: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else throw IllegalStateException("Point 0,0 shouldn't be here with this direction: $direction")
        } else if (bigRow == 0 && bigCol == 3) { // TODO CHANGE
            if (direction == Direction.RIGHT) {
                direction = Direction.LEFT
                next = Pair(3 * 50 - 1 - smallRow, 2 * 50 - 1)
                println("4: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else throw IllegalStateException("Point 0,0 shouldn't be here with this direction: $direction")
        } else if (bigRow == 1 && bigCol == 2) {
            if (direction == Direction.DOWN) {
                direction = Direction.LEFT
                next = Pair(1 * 50 + smallCol, 2 * 50 - 1)
                println("5: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else if (direction == Direction.RIGHT) {
                direction = Direction.UP
                next = Pair(1 * 50 - 1, 2 * 50 + smallRow)
                println("6: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else throw IllegalStateException("Point 0,0 shouldn't be here with this direction: $direction")
        } else if (bigRow == 1 && bigCol == 0) {
            if (direction == Direction.LEFT) {
                direction = Direction.DOWN
                next = Pair(2 * 50, smallRow)
                println("7: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else if (direction == Direction.UP) {
                direction = Direction.RIGHT
                next = Pair(1 * 50 + smallCol, 1 * 50)
                println("8: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else throw IllegalStateException("Point 0,0 shouldn't be here with this direction: $direction")
        } else if (bigRow == 2 && point.second < 0) {
            if (direction == Direction.LEFT) {
                direction = Direction.RIGHT
                next = Pair(1 * 50 - 1 - smallRow, 1 * 50)
                println("9: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else throw IllegalStateException("Point 0,0 shouldn't be here with this direction: $direction")
        } else if (bigRow == 2 && bigCol == 2) {
            if (direction == Direction.RIGHT) {
                direction = Direction.LEFT
                next = Pair(1 * 50 - 1 - smallRow, 3 * 50 - 1)
                println("10: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else throw IllegalStateException("Point 0,0 shouldn't be here with this direction: $direction")
        } else if (bigRow == 3 && bigCol == 1) {
            if (direction == Direction.DOWN) {
                direction = Direction.LEFT
                next = Pair(3 * 50 + smallCol, 1 * 50 - 1)
                println("11: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else if (direction == Direction.RIGHT) {
                direction = Direction.UP
                next = Pair(3 * 50 - 1, 1 * 50 + smallRow)
                println("12: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else throw IllegalStateException("Point 0,0 shouldn't be here with this direction: $direction")
        } else if (bigRow == 4 && bigCol == 0) { // TODO CHANGE
            if (direction == Direction.DOWN) {
                direction = Direction.DOWN
                next = Pair(0, 2 * 50 + smallCol)
                println("13: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else throw IllegalStateException("Point 0,0 shouldn't be here with this direction: $direction")
        } else if (bigRow == 3 && point.second < 0) {
            if (direction == Direction.LEFT) {
                direction = Direction.DOWN
                next = Pair(0, 1 * 50 + smallRow)
                println("14: $point; $bigRow $bigCol $smallRow $smallCol Next $next")
            } else throw IllegalStateException("Point 0,0 shouldn't be here with this direction: $direction")
        }

        return next
    }

    fun Pair<Int, Int>.next(direction: Direction): Pair<Int, Int> {
        val result = when (direction) {
            Direction.LEFT  -> this.copy(second = this.second - 1)
            Direction.RIGHT -> this.copy(second = this.second + 1)
            Direction.UP    -> this.copy(first = this.first - 1)
            Direction.DOWN  -> this.copy(first = this.first + 1)
        }
        return newInEdge(result)
    }

    var counter = move.value
    var next = state.position
    while (counter > 0) {
        val previous = next
        val previousDirection = direction
        next = next.next(direction)

        if (next.getItem(board) == '#') {
            next = previous
            direction = previousDirection
            break
        }
        counter--
    }
    return State(
        position = next,
        direction = direction
    )
}

private fun part2(input: List<String>): Int {
    val startPoint = startPoint(input)
    val board = parseBoard(input)
    val strategies = parseMovement(input)

    val handleStrategy = { state: State, move: Action.Steps ->
        strategyPartTwo(state, board, move)
    }
    return solution(startPoint, strategies, handleStrategy)
}

sealed interface Action {
    data class Steps(val value: Int) : Action
    data class Rotate(val char: Char) : Action
}

enum class Direction {
    RIGHT,
    DOWN,
    LEFT,
    UP
}

data class State(
    val position: Pair<Int, Int>,
    val direction: Direction,
)

fun main() {
    val input = readInput("day22/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}