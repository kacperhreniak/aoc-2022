package day16

import readInput

fun parse(input: List<String>): Pair<Map<String, Map<String, Int>>, Map<String, Int>> {
    val rates = HashMap<String, Int>()
    val graph = HashMap<String, MutableMap<String, Int>>()

    // create connections based on input
    for (line in input) {
        val parts = line.split("Valve ", " has flow rate=", "; tunnels lead to valves ", "; tunnel leads to valve ")

        val key = parts[1]
        val rate = parts[2].toInt()
        val children = parts[3].split(", ").toMutableSet()

        rates[key] = rate
        graph[key] = children.associateWith { 1 }.toMutableMap()
        graph[key]!![key] = 0
    }

    // check if can change
    fun HashMap<String, MutableMap<String, Int>>.getHelper(start: String, end: String): Int {
        val values = get(start).orEmpty()
        return values[end] ?: Int.MAX_VALUE
    }

    fun HashMap<String, MutableMap<String, Int>>.checkCondition(i: String, j: String, k: String): Boolean {
        if (getHelper(i, k) == Int.MAX_VALUE || getHelper(k, j) == Int.MAX_VALUE) {
            return false
        }
        return getHelper(i, j) > getHelper(i, k) + getHelper(k, j)
    }

    // find all paths for all nodes
    val keys = graph.keys
    for (k in keys) {
        for (i in keys) {
            for (j in keys) {
                if (graph.checkCondition(i, j, k)) {
                    graph[i]!![j] = graph.getHelper(i, k) + graph.getHelper(k, j)
                }
            }
        }
    }

    // remove all insignificant paths
    val result = graph.filter { it.key == "AA" || rates.getOrDefault(it.key, 0) > 0 }
        .map { parent -> parent.key to parent.value.filter { it.key != parent.key && rates.getOrDefault(it.key, 0) > 0 } }
        .filter { it.second.isNotEmpty() }
        .toMap()

    return Pair(result, rates)
}

private fun helper(
    grid: Map<String, Map<String, Int>>,
    rates: Map<String, Int>,
    toVisit: Set<String>,
    point: String = "AA",
    time: Int = 30
): Int {
    if(time <= 0) return 0
    var max = 0
    for (next in toVisit) {
        val moveTime = grid[point]!![next]!!
        val pressure = rates[next]!!
        val newVisited = HashSet(toVisit).apply { remove(next) }
        val newTime = time - moveTime - 1
        val value = newTime * pressure

        val temp = value + helper(
            grid, rates, newVisited, next, time - moveTime - 1
        )
        max = temp.coerceAtLeast(max)
    }
    return max
}

private fun part1(input: List<String>): Int {
    val parsesInput = parse(input)
    val toVisit = parsesInput.second.filter { it.value > 0 }.keys
    return helper(parsesInput.first, parsesInput.second, toVisit)
}

private fun part2(input: List<String>): Long {
    return 0
}

fun main() {
    val input = readInput("day16/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
