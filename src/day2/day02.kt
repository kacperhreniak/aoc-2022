private fun itemPoint(item: Char): Int = when {
    item == 'X' || item == 'A' -> 0 // rock
    item == 'Y' || item == 'B' -> 1 // paper
    item == 'Z' || item == 'C' -> 2 // scisorr
    else                       -> 0
}

private fun part1(input: List<String>): Int {
    fun winning(first: Char, second: Char): Int {
        fun position(item: Char): Int = when {
            item == 'X' || item == 'A' -> 0 // rock
            item == 'Z' || item == 'C' -> 1 // scisorr
            item == 'Y' || item == 'B' -> 2 // paper
            else                       -> throw NullPointerException("")
        }

        val positionFirst = position(first)
        val positionSecond = position(second)

        return if ((positionSecond + 1) % 3 == positionFirst) {
            6
        } else if(positionSecond == positionFirst) {
            3
        } else 0
    }

    var points = 0
    for (line in input) {
        val temp = winning(line[0], line[2]) + itemPoint(line[2]) + 1
        points += temp
    }

    return points
}

private fun part2(input: List<String>): Int {
    fun pointForResult(input: Char): Int = when (input) {
        'X' -> 0
        'Y' -> 3
        'Z' -> 6
        else -> throw NullPointerException()
    }

    fun game(result: Int, oponent: Int): Int {
        if(result == 3) return oponent
        if(result == 0) return (oponent + 2) % 3
        if(result == 6) return (oponent + 1) % 3
        throw NullPointerException()
    }

    var points = 0

    for (line in input) {
        val first = itemPoint(line[0])
        val result = pointForResult(line[2])

        val temp = game(result, first) + 1 + result
        points += temp
    }

    return points
}

fun main() {
    val input = readInput("day2/input")
    println(part1(input))
    println(part2(input))
}
