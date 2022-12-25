package day25

import readInput
import kotlin.math.pow

private fun symbolToDigit(input: Char): Int = when (input) {
    '2'  -> 2
    '1'  -> 1
    '0'  -> 0
    '-'  -> -1
    '='  -> -2
    else -> throw IllegalArgumentException()
}

private const val SNAFU_SYMBOLS = "012=-"

private fun convertToDecimal(line: String): Long {
    var index = 0
    return line.foldRight(0) { input, acc ->
        acc + (5.toDouble().pow(index++) * symbolToDigit(input)).toLong()
    }
}

private fun sumAllInput(input: List<String>): Long =
    input.fold(0L) { acc: Long, line: String ->
        val converted = convertToDecimal(line)
        acc + converted
    }

private fun convertToSNUFF(number: Long): String {
    var result = ""
    var input = number
    while (input > 0L) {
        val index = (input % 5L).toInt()
        result = SNAFU_SYMBOLS[index] + result

        input -= ((input + 2) % 5) - 2
        input /= 5
    }

    return result
}

private fun part1(input: List<String>): String {
    val sum = sumAllInput(input)
    println("Sum is $sum")
    val result = convertToSNUFF(sum)
    val temp = convertToDecimal(result)

    return result
}

private fun part2(input: List<String>): Int {
    return 0
}

fun main() {
    val input = readInput("day25/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}