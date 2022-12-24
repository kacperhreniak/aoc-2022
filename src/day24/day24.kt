package day24

import readInput

private fun parse(input: List<String>): Array<Array<Item>> {
    val board = Array(input.size) { Array<Item>(input[0].length) { Item.Free } }

    // first line
    for ((index, item) in input[0].withIndex()) {
        board[0][index] = if (item == '#') {
            Item.Wall
        } else if (item == '.') {
            Item.Entrance
        } else Item.Free
    }

    // last line
    for ((index, item) in input.last().withIndex()) {
        board[input.size - 1][index] = if (item == Item.Wall.element) {
            Item.Wall
        } else if (item == Item.Free.element) {
            Item.Exit
        } else Item.Free
    }

    for (index in 1 until input.size - 1) {
        val row = input[index]
        for ((colIndex, item) in row.withIndex()) {
            board[index][colIndex] = when (item) {
                Item.Free.element -> Item.Free
                Item.Wall.element -> Item.Wall
                else              -> Item.Blizzard(mutableListOf(item))
            }
        }
    }

    return board
}

private fun findEntrance(board: Array<Array<Item>>): Pair<Int, Int> {
    for ((index, item) in board[0].withIndex()) {
        if (item is Item.Entrance) return Pair(0, index)
    }

    throw IllegalStateException("There is no entrance")
}

private fun play(startPosition: Position, input: Array<Array<Item>>, destination: Item): Result {
    fun generateNextBoard(board: Array<Array<Item>>): Array<Array<Item>> {
        val changes = mutableListOf<IncomingBlizzard>()
        for (rowIndex in 1 until board.size - 1) {
            val row = board[rowIndex]
            for ((colIndex, item) in row.withIndex()) {
                if (item is Item.Blizzard) {
                    changes.addAll(nextBlizzardPosition(rowIndex, colIndex, board.size, board[0].size, item))
                }
            }
        }

        val newBoard = Array(board.size) { Array<Item>(board[0].size) { Item.Free } }
        newBoard[0] = board[0]
        newBoard[board.size - 1] = board[board.size - 1]

        for (change in changes) {
            val item = newBoard[change.row][change.col]

            if (item !is Item.Blizzard) {
                newBoard[change.row][change.col] = Item.Blizzard(mutableListOf(change.direction))
            } else {
                item.items.add(change.direction)
            }
        }

        for (row in newBoard) {
            row[0] = Item.Wall
            row[board[0].size - 1] = Item.Wall
        }
        return newBoard
    }

    fun findNewPositions(positions: Set<Position>, board: Array<Array<Item>>): Set<Position> {
        return positions.map {
            val newOptions = mutableListOf<Position>()
            if (it.row - 1 >= 0 && (board[it.row - 1][it.col] == Item.Free || board[it.row - 1][it.col] == destination)) {
                newOptions.add(Position(it.minutes + 1, it.row - 1, it.col))
            }
            if (it.row + 1 < board.size && (board[it.row + 1][it.col] == Item.Free || board[it.row + 1][it.col] == destination)) {
                newOptions.add(Position(it.minutes + 1, it.row + 1, it.col))
            }
            if (it.col - 1 >= 0 && board[it.row][it.col - 1] == Item.Free) {
                newOptions.add(Position(it.minutes + 1, it.row, it.col - 1))
            }
            if (it.col < board[0].size && board[it.row][it.col + 1] == Item.Free) {
                newOptions.add(Position(it.minutes + 1, it.row, it.col + 1))
            }

            if (board[it.row][it.col] !is Item.Blizzard) {
                newOptions.add(Position(it.minutes + 1, it.row, it.col))
            }
            newOptions
        }
            .flatten()
            .toSet()
    }

    var board = input
    var positions = setOf(startPosition)

    while (positions.isNotEmpty()) {
        val isExit = positions.firstOrNull { board[it.row][it.col] == destination }
        if (isExit != null) return Result(isExit, board)

        board = generateNextBoard(board)
        positions = findNewPositions(positions, board)
    }
    throw IllegalStateException("")
}

private fun nextBlizzardPosition(row: Int, col: Int, maxRow: Int, maxCol: Int, item: Item.Blizzard): List<IncomingBlizzard> {
    fun fixCol(col: Int): Int = when (col) {
        0          -> maxCol - 2
        maxCol - 1 -> 1
        else       -> col
    }

    fun fixRow(row: Int): Int = when (row) {
        0          -> maxRow - 2
        maxRow - 1 -> 1
        else       -> row
    }

    return item.items.map {
        when (it) {
            '<'  -> IncomingBlizzard(row = row, col = fixCol(col - 1), '<')
            '>'  -> IncomingBlizzard(row = row, col = fixCol(col + 1), '>')
            '^'  -> IncomingBlizzard(row = fixRow(row - 1), col = col, '^')
            'v'  -> IncomingBlizzard(row = fixRow(row + 1), col = col, 'v')
            else -> throw IllegalStateException("")
        }
    }
}

private fun part1(input: List<String>): Int {
    val board = parse(input)
    val entrance = findEntrance(board)
    val position = Position(0, entrance.first, entrance.second)
    return play(position, board, Item.Exit).position.minutes
}

private fun part2(input: List<String>): Int {
    val board = parse(input)
    val startPosition = findEntrance(board)
    val position = Position(0, startPosition.first, startPosition.second)

    val first = play(position, board, Item.Exit)
    val second = play(first.position, first.board, Item.Entrance)
    val third = play(second.position, second.board, Item.Exit)

    return third.position.minutes
}

data class Position(
    val minutes: Int,
    val row: Int,
    val col: Int,
)

data class IncomingBlizzard(
    val row: Int,
    val col: Int,
    val direction: Char
)

data class Result(
    val position: Position,
    val board: Array<Array<Item>>,
)

sealed interface Item {
    val element: Char

    object Entrance : Item {
        override val element: Char = 'E'
    }

    object Exit : Item {
        override val element: Char = 'X'
    }

    object Wall : Item {
        override val element: Char = '#'
    }

    object Free : Item {
        override val element: Char = '.'
    }

    data class Blizzard(
        val items: MutableList<Char>
    ) : Item {
        override val element: Char = '*'
    }
}

fun main() {
    val input = readInput("day24/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}