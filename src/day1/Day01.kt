import java.util.PriorityQueue
import kotlin.math.max

private const val MAX_SIZE = 3

private fun part1(input: List<String>): Int {
    var max = Int.MIN_VALUE
    var temp = 0

    input.forEach {
        if (it.isEmpty() && temp > 0) {
            max = max(max, temp)
            temp = 0
        } else {
            temp += it.toInt()
        }
    }

    return max
}

private fun part2(input: List<String>): Int {
    fun PriorityQueue<Int>.addNewItem(value: Int) {
        if (isNotEmpty() && size == MAX_SIZE && peek() < value) poll()
        if (value != 0) add(value)
    }

    val queue = PriorityQueue<Int>(MAX_SIZE, compareByDescending { it })
    var temp = 0

    input.forEach {
        if (it.isEmpty() && temp > 0) {
            queue.addNewItem(value = temp)
            temp = 0
        } else {
            temp += it.toInt()
        }
    }
    queue.addNewItem(value = temp)

    var result = 0
    for (index in 0 until MAX_SIZE) {
        result += queue.poll()
    }
    return result
}

fun main() {
    val input = readInput("day1/input")
    println(part1(input))
    println(part2(input))
}
