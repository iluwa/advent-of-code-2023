private data class Card(val actualNumbers: Set<Int>, val winningNumbers: Set<Int>) {
    fun calculatePoints(): Int {
        return actualNumbers.filter { winningNumbers.contains(it) }
            .fold(0) { acc: Int, _ ->
                return@fold when (acc) {
                    0 -> 1
                    else -> acc * 2
                }
            }
    }

    fun nextCardsToAdd(): Int {
        return actualNumbers.filter { winningNumbers.contains(it) }.size
    }
}

fun main() {
    fun parseInput(input: List<String>): List<Card> {
        return input.map { it.replace("(Card)\\s+[0-9]+(:)\\s".toRegex(), "") }
            .map { it.split(" | ") }
            .mapIndexed { i, el ->
                Card(
                    el[0].trim().split("\\s+".toRegex()).map { it.toInt() }.toSet(),
                    el[1].trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
                )
            }
    }

    fun part1(input: List<String>): Int {
        return parseInput(input)
            .sumOf { it.calculatePoints() }
    }

    fun part2(input: List<String>): Int {
        val cardAmount: MutableList<Int> = MutableList(input.size) { 1 }
        val cards = parseInput(input)
        cards.forEachIndexed { i, card ->
            for (j in i + 1..i + card.nextCardsToAdd()) {
                cardAmount[j] = cardAmount[j] + cardAmount[i]
            }
        }

        return cardAmount.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()

    check(part2(testInput) == 30)
    part2(input).println()
}
