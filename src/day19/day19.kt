package day19

import readInput
import java.lang.Math.ceil
import java.lang.Math.max

private fun parse(input: List<String>): List<Blueprint> {
    return input.map { line ->
        val parts = line.split(": ", ". ")

        val ore = parts[1].split(" ")[4].toInt().let { Item.Ore(it) }
        val clay = parts[2].split(" ")[4].toInt().let { Item.Clay(it) }
        val obsidian = parts[3].split(" ").run {
            Item.Obsidian(oreCount = get(4).toInt(), clayCount = get(7).toInt())
        }
        val geode = parts[4].split(" ").run {
            Item.Geode(oreCount = get(4).toInt(), obsidianCount = get(7).toInt())
        }

        Blueprint(ore, clay, obsidian, geode)
    }
}

private fun helper(blueprint: Blueprint, state: State, cache: HashMap<Int, Int>): Int {
    if (state.time <= 0) return 0
    if (cache.contains(state.hashCode())) return cache[state.hashCode()]!!
    var result = 0

    fun createOre(item: Item.Ore): Int {
        var waitTime = ceil((item.count - state.oreCount) / state.oreMachine.toDouble())
            .toInt().coerceAtLeast(0)

        val shouldCreate = state.oreMachine < max(
            max(blueprint.ore.count, blueprint.clay.count),
            max(blueprint.obsidian.oreCount, blueprint.geode.oreCount)
        )
        return if (shouldCreate && state.time > waitTime) {
            waitTime++
            val newState = state.copy(
                oreCount = state.oreCount - item.count + waitTime * state.oreMachine,
                clayCount = state.clayCount + waitTime * state.clayMachine,
                obsidianCount = state.obsidianCount + waitTime * state.obsidianMachine,
                oreMachine = state.oreMachine + 1,
                time = state.time - waitTime
            )

            helper(blueprint, newState, cache)
        } else 0
    }

    fun createClay(item: Item.Clay): Int {
        var waitTime = ceil((item.count - state.oreCount) / state.oreMachine.toDouble())
            .toInt().coerceAtLeast(0)

        val shouldCreate = state.clayMachine < blueprint.obsidian.clayCount
        return if (shouldCreate && state.time > waitTime) {
            waitTime++
            val newState = state.copy(
                oreCount = state.oreCount - item.count + waitTime * state.oreMachine,
                clayCount = state.clayCount + waitTime * state.clayMachine,
                obsidianCount = state.obsidianCount + waitTime * state.obsidianMachine,
                clayMachine = state.clayMachine + 1,
                time = state.time - waitTime
            )

            helper(blueprint, newState, cache)
        } else 0
    }

    fun createObsidian(item: Item.Obsidian): Int {
        var waitTime = if (state.oreMachine > 0 && state.clayMachine > 0) {
            val waitOreTime = ceil((item.oreCount - state.oreCount) / state.oreMachine.toDouble()).toInt()
            val waitClayTime = ceil((item.clayCount - state.clayCount) / state.clayMachine.toDouble()).toInt()

            max(waitOreTime, waitClayTime).coerceAtLeast(0)
        } else Int.MAX_VALUE
        val shouldCreate = state.obsidianMachine < blueprint.geode.obsidianCount

        return if (shouldCreate && state.time > waitTime) {
            waitTime++

            val newState = state.copy(
                oreCount = state.oreCount - item.oreCount + waitTime * state.oreMachine,
                clayCount = state.clayCount - item.clayCount + waitTime * state.clayMachine,
                obsidianCount = state.obsidianCount + waitTime * state.obsidianMachine,
                obsidianMachine = state.obsidianMachine + 1,
                time = state.time - waitTime
            )

            helper(blueprint, newState, cache)
        } else 0
    }

    fun createGeode(item: Item.Geode): Int {
        var waitTime = if (state.oreMachine > 0 && state.obsidianMachine > 0) {
            val waitOreTime = ceil((item.oreCount - state.oreCount) / state.oreMachine.toDouble()).toInt()
            val waitObsidianTime = ceil((item.obsidianCount - state.obsidianCount) / state.obsidianMachine.toDouble()).toInt()

            max(waitOreTime, waitObsidianTime).coerceAtLeast(0)
        } else Int.MAX_VALUE

        return if (state.time > waitTime) {
            waitTime++
            val newState = state.copy(
                oreCount = state.oreCount - item.oreCount + waitTime * state.oreMachine,
                clayCount = state.clayCount + waitTime * state.clayMachine,
                obsidianCount = state.obsidianCount - item.obsidianCount + waitTime * state.obsidianMachine,
                time = state.time - waitTime
            )

            (state.time - waitTime) + helper(blueprint, newState, cache)
        } else 0
    }
    result = createOre(blueprint.ore).coerceAtLeast(result)
    result = createClay(blueprint.clay).coerceAtLeast(result)
    result = createObsidian(blueprint.obsidian).coerceAtLeast(result)
    result = createGeode(blueprint.geode).coerceAtLeast(result)

    cache[state.hashCode()] = result
    return result
}

private fun part1(input: List<String>): Int {
    val blueprints = parse(input)
    val initialState = State(oreMachine = 1, time = 24)
    return blueprints.asSequence()
        .map { helper(it, initialState.copy(), hashMapOf()) }
        .foldIndexed(0) { index, acc, value -> acc + (index + 1) * value }
}

private fun part2(input: List<String>): Int {
    val blueprints = parse(input)
    val initialState = State(oreMachine = 1, time = 32)
    return blueprints.subList(0,3)
        .map { helper(it, initialState.copy(), hashMapOf()) }
        .fold(1) { acc: Int, result: Int -> acc * result }
}

data class State(
    val oreCount: Int = 0,
    val clayCount: Int = 0,
    val obsidianCount: Int = 0,
    val oreMachine: Int = 1,
    val clayMachine: Int = 0,
    val obsidianMachine: Int = 0,
    val time: Int = 0
)

data class Blueprint(
    val ore: Item.Ore,
    val clay: Item.Clay,
    val obsidian: Item.Obsidian,
    val geode: Item.Geode
)

sealed interface Item {
    data class Ore(val count: Int) : Item
    data class Clay(val count: Int) : Item
    data class Obsidian(val oreCount: Int, val clayCount: Int) : Item
    data class Geode(val oreCount: Int, val obsidianCount: Int) : Item
}

fun main() {
    val input = readInput("day19/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
