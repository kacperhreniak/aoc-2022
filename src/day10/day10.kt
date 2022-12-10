package day10

import readInput

const val NEXT_CHECK = 40
const val LIT_PIXEL = '#'
const val DARK_PIXEL = '.'

private fun getParams(line: String): Pair<Int, Int> {
    val parts = line.split(" ")
    val instruction = parts[0]

    val cycles = when (instruction) {
        "noop" -> 1
        "addx" -> 2
        else   -> 0
    }

    val value = when (instruction) {
        "noop" -> 0
        "addx" -> parts[1].toInt()
        else   -> 0
    }
    return Pair(cycles, value)
}

private fun part1(input: List<String>): Int {
    var value = 1
    var cycle = 0
    var checkPoint = 20
    var result = 0
    for (line in input) {
        val params = getParams(line)

        for (step in 0 until params.first) {
            cycle++
            if (cycle == checkPoint) {
                result += cycle * value
                checkPoint += NEXT_CHECK
            }
        }

        value += params.second
    }
    return result
}

private fun part2(input: List<String>) {
    val result = mutableListOf<String>()
    var temp = ""
    var index = 1
    for (line in input) {
        val params = getParams(line)
        for (step in 0 until params.first) {
            temp += if (temp.length + 1 in index until index + 3) {
                LIT_PIXEL
            } else DARK_PIXEL

            if (temp.length == NEXT_CHECK) {
                result.add(temp)
                temp = ""
            }
        }
        index += params.second
    }

    for (line in result) {
        println(line)
    }
}

fun main() {
    val input = readInput("day10/input")
    println(part1(input))
    part2(input)
}
