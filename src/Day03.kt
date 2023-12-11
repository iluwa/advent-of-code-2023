private fun Char.isSymbol(): Boolean = this != '.' && !this.isDigit()
private fun Char.isStar(): Boolean = this == '*'

private data class Tracking(
    var current: String, val neighbours: MutableSet<Char>, var sum: Int = 0,
    val coordinates: MutableList<Pair<Int, Int>>
) {
    companion object {
        fun empty(): Tracking {
            return Tracking("", mutableSetOf(), coordinates = mutableListOf())
        }
    }

    fun resetIfNecessary(condition: Char.() -> Boolean): PartNumber? {
        return when {
            isEmpty() -> null
            else -> {
                val partNumber = if (neighbours.any { it.condition() }) {
                    sum += current.toInt()
                    PartNumber(current.toInt(), coordinates.toList())
                } else null
                current = ""
                neighbours.clear()
                coordinates.clear()
                return partNumber
            }
        }
    }

    fun isEmpty() = current == ""

    fun add(ch: Char, neighbours: Set<Char>, coordinate: Pair<Int, Int>) {
        current += ch
        this.neighbours.addAll(neighbours)
        this.coordinates.add(coordinate)
    }
}

private data class PartNumber(val n: Int, val coordinates: List<Pair<Int, Int>>)

fun main() {
    fun parseInput(input: List<String>): List<List<Char>> {
        return input.map { it.toCharArray().toList() }
    }

    fun List<List<Char>>.neighbours(i: Int, j: Int): Set<Char> {
        val lowI = (i - 1).coerceAtLeast(0)
        val lowJ = (j - 1).coerceAtLeast(0)
        val highI = (i + 1).coerceAtMost(this.size - 1)
        val highJ = (j + 1).coerceAtMost(this.size - 1)

        return setOf(
            this[lowI][lowJ],
            this[lowI][j],
            this[lowI][highJ],
            this[i][lowJ],
            this[i][highJ],
            this[highI][lowJ],
            this[highI][j],
            this[highI][highJ]
        )
    }

    fun Map<Pair<Int, Int>, PartNumber>.neighbourPartNumbers(i: Int, j: Int): List<PartNumber> {
        val lowI = (i - 1).coerceAtLeast(0)
        val lowJ = (j - 1).coerceAtLeast(0)
        val highI = (i + 1).coerceAtMost(this.size - 1)
        val highJ = (j + 1).coerceAtMost(this.size - 1)

        return setOf(
            this[Pair(lowI, lowJ)],
            this[Pair(lowI, j)],
            this[Pair(lowI, highJ)],
            this[Pair(i, lowJ)],
            this[Pair(i, highJ)],
            this[Pair(highI, lowJ)],
            this[Pair(highI, j)],
            this[Pair(highI, highJ)]
        ).filterNotNull()
    }

    fun part1(input: List<String>): Int {
        val engine = parseInput(input)
        val tracking = Tracking.empty()
        for (i in engine.indices) {
            for (j in engine[i].indices) {
                if (engine[i][j].isDigit()) {
                    tracking.add(engine[i][j], engine.neighbours(i, j), Pair(i, j))
                } else {
                    tracking.resetIfNecessary { isSymbol() }
                }
            }
            tracking.resetIfNecessary { isSymbol() }
        }
        return tracking.sum
    }

    fun part2(input: List<String>): Int {
        val engine = parseInput(input)
        val tracking = Tracking.empty()
        val partNumbers = mutableListOf<PartNumber>()
        for (i in engine.indices) {
            for (j in engine[i].indices) {
                if (engine[i][j].isDigit()) {
                    tracking.add(engine[i][j], engine.neighbours(i, j), Pair(i, j))
                } else {
                    tracking.resetIfNecessary { isStar() }?.also { partNumbers.add(it) }
                }
            }
            tracking.resetIfNecessary { isStar() }?.also { partNumbers.add(it) }
        }

        val map = mutableMapOf<Pair<Int, Int>, PartNumber>()
        for (partNumber in partNumbers) {
            for (c in partNumber.coordinates) {
                map[c] = partNumber
            }
        }

        var gearRatios = 0
        for (i in engine.indices) {
            for (j in engine[i].indices) {
                if (engine[i][j].isStar()) {
                    val neighbourPartNumbers = map.neighbourPartNumbers(i, j)
                    if (neighbourPartNumbers.size == 2) {
                        gearRatios += neighbourPartNumbers[0].n * neighbourPartNumbers[1].n
                    }
                }
            }
        }

        return gearRatios
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()

    check(part2(testInput) == 467835)
    part2(input).println()
}
