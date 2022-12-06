package day6

import readInput

private fun calculate(input: String, uniqueCharCount: Int): Int {
    val cache = hashMapOf<Char, Int>()
    for((index, item) in input.withIndex()) {
        if(cache.containsKey(item).not()) {
            cache.put(item, index)
        } else {
            val currentIndex = cache[item]!! + 1
            val temp = HashMap(cache)
            for((item, index) in temp) {
                if(index < currentIndex) cache.remove(item)
            }
            cache[item] = index
        }
        if(cache.size == uniqueCharCount) return index + 1
    }
    return  - 1
}

private fun part1(input: List<String>): Int {
    return calculate(input[0], 4)
}

private fun part2(input: List<String>): Int {
    return calculate(input[0], 14)
}

fun main() {
    val input = readInput("day6/input")
    println(part1(input))
    println(part2(input))
}
