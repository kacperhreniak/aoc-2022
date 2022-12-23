package day23

import readInput

private fun parse(input: List<String>): HashMap<Int, HashSet<Int>> {
    fun HashMap<Int, HashSet<Int>>.addItem(row: Int, col: Int) {
        val values = getOrDefault(row, hashSetOf())
        values.add(col)
        put(row, values)
    }

    val result = HashMap<Int, HashSet<Int>>()
    for ((rowIndex, row) in input.withIndex()) {
        for ((colIndex, item) in row.withIndex()) {
            if (item == '#') result.addItem(rowIndex, colIndex)
        }
    }

    return result
}

fun HashMap<Int, HashSet<Int>>.isNorthFree(row: Int, col: Int): Boolean {
    val north = get(row - 1).orEmpty()
    return north.isEmpty() ||
            (north.contains(col) || north.contains(col - 1) || north.contains(col + 1)).not()
}

fun HashMap<Int, HashSet<Int>>.isSouthFree(row: Int, col: Int): Boolean {
    val south = get(row + 1).orEmpty()
    return south.isEmpty() ||
            (south.contains(col) || south.contains(col - 1) || south.contains(col + 1)).not()
}

fun HashMap<Int, HashSet<Int>>.isWestFree(row: Int, col: Int): Boolean {
    return (get(row - 1).orEmpty().contains(col - 1) ||
            get(row).orEmpty().contains(col - 1) ||
            get(row + 1).orEmpty().contains(col - 1)).not()
}

fun HashMap<Int, HashSet<Int>>.isEastFree(row: Int, col: Int): Boolean {
    return (get(row - 1).orEmpty().contains(col + 1) ||
            get(row).orEmpty().contains(col + 1) ||
            get(row + 1).orEmpty().contains(col + 1)).not()
}

fun HashMap<Int, HashSet<Int>>.isNoOneAround(row: Int, col: Int): Boolean {
    return isNorthFree(row, col) && isSouthFree(row, col) && isWestFree(row, col) && isEastFree(row, col)
}

private const val MOVES_NUMBER = 4
fun HashMap<Int, HashSet<Int>>.findMove(round: Int, row: Int, col: Int): Pair<Int, Int>? {
    val moves = listOf(
        Pair({ this.isNorthFree(row, col) }, Pair(row - 1, col)),
        Pair({ this.isSouthFree(row, col) }, Pair(row + 1, col)),
        Pair({ this.isWestFree(row, col) }, Pair(row, col - 1)),
        Pair({ this.isEastFree(row, col) }, Pair(row, col + 1))
    )

    var counter = round % MOVES_NUMBER
    for (index in 0 until MOVES_NUMBER) {
        val potentialMove = moves[counter]
        if (potentialMove.first.invoke()) {
            return potentialMove.second
        }
        counter = (counter + 1) % MOVES_NUMBER
    }

    return null
}

private fun moves(elves: HashMap<Int, HashSet<Int>>, maxRounds: Int): Pair<Int, HashMap<Int, HashSet<Int>>> {
    var round = 0
    while (round < maxRounds) {
        val proposeMoves = HashMap<Pair<Int, Int>, MutableList<Pair<Int, Int>>>()
        for ((rowIndex, row) in elves) {
            for (colIndex in row) {
                if (elves.isNoOneAround(rowIndex, colIndex)) continue

                elves.findMove(round, rowIndex, colIndex)?.run {
                    val values = (proposeMoves[this] ?: mutableListOf()).apply {
                        add(Pair(rowIndex, colIndex))
                    }
                    proposeMoves.put(this, values)
                }
            }
        }

        var wasMove = false
        for ((key, value) in proposeMoves) {
            if (value.size == 1) {
                wasMove = true
                elves[value[0].first]?.remove(value[0].second)

                val values = elves.getOrDefault(key.first, hashSetOf())
                values.add(key.second)
                elves[key.first] = values
            }
        }

        round++
        if (!wasMove) return Pair(round, elves)
    }
    return Pair(round, elves)
}

private fun findMax(elves: HashMap<Int, HashSet<Int>>): Int {
    var top = Int.MAX_VALUE
    var bottom = Int.MIN_VALUE
    var left = Int.MAX_VALUE
    var right = Int.MIN_VALUE

    for ((key, values) in elves) {
        for (colIndex in values) {
            top = key.coerceAtMost(top)
            bottom = key.coerceAtLeast(bottom)
            left = colIndex.coerceAtMost(left)
            right = colIndex.coerceAtLeast(right)
        }
    }

    var result = (right - left + 1) * (bottom - top + 1)
    for ((_, value) in elves) {
        result -= value.size
    }

    return result
}

private const val MAX_MOVES_FIRST = 10
private fun part1(input: List<String>): Int {
    val elves = parse(input)
    val movesResult = moves(elves, MAX_MOVES_FIRST)
    return findMax(movesResult.second)
}

private const val MAX_MOVE_SECOND = 10000
private fun part2(input: List<String>): Int {
    val elves = parse(input)
    val movesResult = moves(elves, MAX_MOVE_SECOND)
    return movesResult.first
}

fun main() {
    val input = readInput("day23/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

/*

    for (index in top..bottom) {
        if (elves[index].orEmpty().isEmpty()) continue
        for (colIndex in left..right) {
            if (elves[index]!!.contains(colIndex)) {
                print("#")
            } else print(".")
        }
        println()
    }

    println()


 */