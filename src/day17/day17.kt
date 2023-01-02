package day17

import readInput

private const val WIDTH = 7
private const val COL_INDEX = 2
private const val HEIGHT_OFFSET = 4

private enum class Rock(val field: Array<Pair<Int, Int>>) {
    HORIZONTAL_LINE(field = arrayOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3))),
    CROSS(field = arrayOf(Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(1, 2), Pair(2, 1))),
    L_SHAPE(field = arrayOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 2), Pair(2, 2))),
    VERTICAL_LINE(field = arrayOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0))),
    SQUARE(field = arrayOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1)));

    fun next(): Rock = values()[(ordinal + 1) % values().size]
}

private fun Pair<Int, Int>.fall() = copy(first = first - 1)
private fun Pair<Int, Int>.applyJet(direction: Char) = when (direction) {
    '>'  -> copy(second = second + 1)
    '<'  -> copy(second = second - 1)
    else -> throw IllegalArgumentException()
}

private fun game(
    jets: String,
    startRock: Rock = Rock.HORIZONTAL_LINE,
    limit: Int,
): Int {
    var jetIndex = 0
    val cave = HashSet<Pair<Int, Int>>()
    var rock = startRock

    fun startPosition(): Pair<Int, Int> {
        val row = cave.maxOfOrNull { it.first } ?: 0
        return Pair(row + HEIGHT_OFFSET, COL_INDEX)
    }

    fun canMove(current: Pair<Int, Int>, rock: Rock, direction: Char): Boolean {
        val items = rock.field.map {
            Pair(
                first = it.first + current.first,
                second = it.second + current.second
            )
        }

        return when (direction) {
            '<'  -> items.minOf { it.second } > 0 && items.none { cave.contains(it.copy(second = it.second - 1)) }
            '>'  -> items.maxOf { it.second } < WIDTH - 1 && items.none { cave.contains(it.copy(second = it.second + 1)) }
            else -> throw IllegalArgumentException()
        }
    }

    fun canFall(current: Pair<Int, Int>, rock: Rock): Boolean {
        return current.first > 1 && rock.field.map {
            Pair(
                first = current.first + it.first - 1,
                second = current.second + it.second
            )
        }.none { cave.contains(it) }
    }

    fun singleRound(): Pair<Int, Int> {
        var position = startPosition()

        while (true) {
            val jet = jets[jetIndex % jets.length]
            if (canMove(position, rock, jet)) {
                position = position.applyJet(jet)
            }
            jetIndex++

            if (canFall(position, rock).not()) {
                return position
            }

            position = position.fall()
        }
    }

    fun Pair<Int, Int>.updateCave(rock: Rock) {
        rock.field.map { copy(first = first + it.first, second = second + it.second) }
            .forEach { cave.add(it) }
    }

    repeat(limit) {
        singleRound().updateCave(rock)
        rock = rock.next()
    }
    return cave.maxOf { it.first }
}

private fun part1(input: List<String>): Int {
    val rocks = 2022
    return game(
        jets = input[0],
        limit = rocks
    )
}

private fun part2(input: List<String>): Long {
    val limit = 1000000000000L
    val startPoint = 2024
    val cycleCount = 1705
    val cycleHeight = 2582

    val allCycleHeight = ((limit - startPoint) / cycleCount) * cycleHeight
    val newRocksLimit = startPoint + (limit - startPoint) % cycleCount
    return allCycleHeight + game(jets = input[0], limit = newRocksLimit.toInt())
}

fun main() {
    val input = readInput("day17/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

/* Magic numbers
    How I found a these magic numbers?
    I decided to store difference between heights in adjacent columns with information about current rock type and jet index, for an instance, `SQUARE_1919_022-500`
    to find out startPoint for cycle. I took first key and re-run code to find all indexes when cycles begin.
    For my input, the cycle starts for rock: `2024`, cycle is created by `1705` incoming rocks, and each cycle increases height by `2582`.

    Part of the code used to find cycles\
    fun createKey(): String {
        val temp = IntArray(WIDTH)
        repeat(WIDTH) { col ->
            temp[col] = cave.filter { it.second == col }.maxOfOrNull { it.first } ?: 0
        }

        var result = "${rock}_${jetIndex % jets.length}_"
        for (index in 1 until WIDTH) {
            result += temp[index] - temp[index - 1]
        }

        return result
    }

    val cache = hashSetOf<String>()
    repeat(limit) {
        singleRound().updateCave(rock)

        val key = createKey()
        if (cache.contains(key) && key == "SQUARE_1919_022-500") {
            println("$rock, $it, $key, ${cave.maxOf { it.first }}")
        } else {
            cache.add(key)
        }
        rock = rock.next()
    }
 */