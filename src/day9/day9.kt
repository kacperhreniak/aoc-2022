package day9

import readInput
import kotlin.math.abs

private fun helper(input: List<String>, points: Array<Pair<Int, Int>>): Int {
    fun createKey(point: Pair<Int, Int>): String = "${point.first},${point.second}"
    val visited: MutableSet<String> = hashSetOf<String>().apply {
        add(createKey(points.last()))
    }
    for (move in input) {
        val parts = move.split(" ")
        val direction = parts[0]
        val steps = parts[1].toInt()

        for (step in 0 until steps) {
            points[0] = with(points[0]) {
                when (direction) {
                    "R"  -> Pair(first, second + 1)
                    "L"  -> Pair(first, second - 1)
                    "D"  -> Pair(first - 1, second)
                    "U"  -> Pair(first + 1, second)
                    else -> throw NullPointerException()
                }
            }

            for (index in 1 until points.size) {
                val tempHeadPoint = points[index - 1]
                val tempTailPoint = points[index]

                val diffRow = tempHeadPoint.first - tempTailPoint.first
                val diffCol = tempHeadPoint.second - tempTailPoint.second
                if (abs(diffRow) <= 1 && abs(diffCol) <= 1) {
                    continue
                }

                val flowMove = Pair(diffRow.coerceIn(-1, 1), diffCol.coerceIn(-1, 1))
                points[index] = Pair(tempTailPoint.first + flowMove.first, tempTailPoint.second + flowMove.second)
            }
            visited.add(createKey(points.last()))
        }
    }

    return visited.size
}

private fun part1(input: List<String>): Int {
    val points: Array<Pair<Int, Int>> = Array(2) { Pair(0, 0) }
    return helper(input, points)
}

private fun part2(input: List<String>): Int {
    val points: Array<Pair<Int, Int>> = Array(10) { Pair(0, 0) }
    return helper(input, points)
}

fun main() {
    val input = readInput("day9/input")
    println(part1(input))
    println(part2(input))
}
