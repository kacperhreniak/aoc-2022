package day16

import readInput

fun parse(input: List<String>): HashMap<String, Pair<Int, Set<String>>> {
    val items = HashMap<String, Pair<Int, Set<String>>>()
    for (line in input) {
        val parts = line.split("Valve ", " has flow rate=", "; tunnels lead to valves ", "; tunnel leads to valve ")

        val key = parts[1]
        val rate = parts[2].toInt()
        val childs = parts[3].split(", ").toSet()

        items[key] = Pair(rate, childs)
    }

    return items
}

private fun solution(
    input: HashMap<String, Pair<Int, Set<String>>>,
    startPoint: String
): Int {
    val opened = hashSetOf<String>().apply { add(startPoint) }
    val dp = HashMap<String, Int>()
    return helper(
        input,
        startPoint,
        dp,
        opened,
        30,
        0
    )
}

private fun helper(
    input: HashMap<String, Pair<Int, Set<String>>>,
    key: String,
    dp: HashMap<String, Int>,
    opened: MutableSet<String>,
    timer: Int,
    pressureCount: Int
): Int {
    fun dpKey(key: String, timer: Int, opened: MutableSet<String>): String {
        return "${key}_${timer}_${opened.toSortedSet()}"
    }
    if (timer <= 0) return 0
    if (opened.size == input.size) return timer * pressureCount

    val dpKey = dpKey(key, timer, opened)
    if (dp.contains(dpKey)) return dp[dpKey]!!

    var max = Int.MIN_VALUE
    if (opened.contains(key).not() && timer >= 2) {
        val temp = HashSet(opened).apply { add(key) }
        val newPressureCount = pressureCount + input[key]!!.first
        max = newPressureCount + (input[key]!!.second
            .filter { it != key }.maxOfOrNull {
                pressureCount + helper(input, it, dp, temp, timer - 2, newPressureCount)
            } ?: 0)
    }
    val tempMax = input[key]!!.second
        .filter { it != key }.maxOfOrNull {
            helper(input, it, dp, HashSet(opened), timer - 1, pressureCount)
        } ?: 0

    max = (pressureCount + tempMax).coerceAtLeast(max)
    dp[dpKey] = max
    return max
}

private fun part1(input: List<String>): Int {
    val parsesInput = parse(input)
    return solution(parsesInput, "AA")
}

private fun part2(input: List<String>): Long {
    return 0
}

fun main() {
    val input = readInput("day16/test-input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
