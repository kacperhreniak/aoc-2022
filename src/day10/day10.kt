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

        if ((cycle + params.first) % checkPoint < cycle) {
            result += checkPoint * value
            checkPoint += NEXT_CHECK
        }
        cycle += params.first
        value += params.second
    }
    return result
}

private fun part2(input: List<String>) {
    var temp = ""
    var index = 1
    for (line in input) {
        val params = getParams(line)
        for (step in 0 until params.first) {
            temp += if (temp.length + 1 in index until index + 3) {
                LIT_PIXEL
            } else DARK_PIXEL

            if (temp.length == NEXT_CHECK) {
                println(temp)
                temp = ""
            }
        }
        index += params.second
    }
}

fun main() {
    val input = readInput("day10/test-input")
    println(part1(input))
    part2(input)
}
