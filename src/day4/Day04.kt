package day4

import readInput

private fun part1(input: List<String>): Int {
    var result = 0

    for(line in input) {
        val items = line.split(',')

        val first = items[0].split("-")
        val second = items[1].split("-")

        if((first[0].toInt() >= second[0].toInt() && first[1].toInt() <= second[1].toInt()) ||
                (second[0].toInt() >= first[0].toInt() && second[1].toInt() <= first[1].toInt())) {
            result++
        }
    }

    return result
}

private fun part2(input: List<String>): Int {
    var result = 0
    for(line in input) {
        val items = line.split(',')
        val first = items[0].split("-")
        val second = items[1].split("-")

        if(
            first[0].toInt() > second[1].toInt() ||
                    first[1].toInt() < second[0].toInt() ||
                    second[1].toInt() < first[0].toInt() ||
                    second[0].toInt() > first[1].toInt()
        ) {
            result++
        }
    }
    return input.size - result
}

fun main() {
    val input = readInput("day4/input")
    println(part1(input))
    println(part2(input))
}
