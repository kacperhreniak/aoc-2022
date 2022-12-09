package day9

import readInput
import kotlin.math.abs

private fun part1(input: List<String>): Int {
    fun createKey(point: Pair<Int, Int>): String = "${point.first},${point.second}"
    val visited: MutableSet<String> = hashSetOf()
    var headPoint = Pair(0, 0)
    var tailPoint = Pair(0, 0)

    var counter = 0
    for (move in input) {
        val parts = move.split(" ")
        val direction = parts[0]
        val steps = parts[1].toInt()

        for (index in 0 until steps) {
            if (visited.add(createKey(tailPoint))) {
                counter++
            }

            headPoint = with(headPoint) {
                when (direction) {
                    "R"  -> Pair(first, second + 1)
                    "L"  -> Pair(first, second - 1)
                    "D"  -> Pair(first - 1, second)
                    "U"  -> Pair(first + 1, second)
                    else -> throw NullPointerException()
                }
            }

            tailPoint = if (headPoint.first == tailPoint.first) {
                val temp = (tailPoint.second - headPoint.second) / 2
                Pair(tailPoint.first, tailPoint.second - temp)
            } else if (headPoint.second == tailPoint.second) {
                val temp = (tailPoint.first - headPoint.first) / 2
                Pair(tailPoint.first - temp, tailPoint.second)
            } else if (abs(headPoint.second - tailPoint.second) >= 2) {
                val temp = (tailPoint.second - headPoint.second) / 2
                Pair(headPoint.first, tailPoint.second - temp)
            } else if (abs(headPoint.first - tailPoint.first) >= 2) {
                val temp = (tailPoint.first - headPoint.first) / 2
                Pair(tailPoint.first - temp, headPoint.second)
            } else tailPoint

            if (visited.add(createKey(tailPoint))) {
                counter++
            }
        }
    }

    return counter
}

private fun part2(input: List<String>): Int {
    fun createKey(point: Pair<Int, Int>): String = "${point.first},${point.second}"
    val visited: MutableSet<String> = hashSetOf()
    val points: Array<Pair<Int, Int>> = Array(10) { Pair(0, 0) }

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
            for (index in 1..9) {
                val tempHeadPoint = points[index - 1]
                val tempTailPoint = points[index]

                if (abs(tempTailPoint.second - tempHeadPoint.second) <= 1 && abs(tempHeadPoint.first - tempTailPoint.first) <= 1) {
                    continue
                }

                points[index] = if (tempHeadPoint.first == tempTailPoint.first) {
                    val temp = (tempTailPoint.second - tempHeadPoint.second) / 2
                    Pair(tempTailPoint.first, tempTailPoint.second - temp)
                } else if (tempHeadPoint.second == tempTailPoint.second) {
                    val temp = (tempTailPoint.first - tempHeadPoint.first) / 2
                    Pair(tempTailPoint.first - temp, tempTailPoint.second)
                } else if (tempHeadPoint.first > tempTailPoint.first && tempHeadPoint.second < tempTailPoint.second) {
                    // top lef
                    Pair(tempTailPoint.first + 1 , tempTailPoint.second - 1)
                } else if (tempHeadPoint.first > tempTailPoint.first && tempHeadPoint.second > tempTailPoint.second) {
                    // top right
                    Pair(tempTailPoint.first +1 , tempTailPoint.second + 1)
                } else if (tempHeadPoint.first < tempTailPoint.first && tempHeadPoint.second < tempTailPoint.second) {
                    // bottom left
                    Pair(tempTailPoint.first - 1 , tempTailPoint.second -1)
                } else if (tempHeadPoint.first < tempTailPoint.first && tempHeadPoint.second > tempTailPoint.second) {
                    // bottom right
                    Pair(tempTailPoint.first -1 , tempTailPoint.second + 1)
                } else points[index]
            }

            visited.add(createKey(points.last()))
        }
    }
    return visited.size
}

fun main() {
    val input = readInput("day9/input")
    println(part1(input))
    println(part2(input))
}
