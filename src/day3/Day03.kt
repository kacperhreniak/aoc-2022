import java.math.BigInteger

private fun createResult(text: String): BigInteger {
    var result = BigInteger.ZERO
    for (item in text) {
        val index = item - if (item.isUpperCase()) 'A' - 26 else 'a'
        result = result or (BigInteger.ONE shl index)
    }
    return result
}

private fun findIndex(item: BigInteger): Int {
    var temp = item
    for (index in 0 until 52) {
        if ((temp and BigInteger.ONE) == BigInteger.ONE) {
            return index
        }
        temp = temp shr 1
    }
    return -1
}

private fun part1(input: List<String>): Int {
    var result = 0
    for (rucksack in input) {
        val size = rucksack.length / 2
        val firstResult = createResult(rucksack.substring(0, size))
        val secondResult = createResult(rucksack.substring(size))

        result += findIndex(firstResult and secondResult) + 1

    }
    return result
}

private fun part2(input: List<String>): Int {
    var result = 0
    var temp = (BigInteger.ONE shr 52) - BigInteger.ONE
    for ((index, item) in input.withIndex()) {
        val itemResult = createResult(item)
        temp = temp and itemResult

        if ((index + 1) % 3 == 0) {
            result += findIndex(temp) + 1
            temp = (BigInteger.ONE shr 52) - BigInteger.ONE
        }
    }
    return result
}

fun main() {
    val input = readInput("day3/input")
    println(part1(input))
    println(part2(input))
}
