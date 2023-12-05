import kotlin.math.max

enum class Color {
    RED, GREEN, BLUE
}

private data class BallRecord(val color: Color, val n: Int) {
    companion object {
        fun of(s: String): BallRecord {
            val (n, c) = s.split(" ")
            return BallRecord(Color.valueOf(c.uppercase()), n.toInt())
        }
    }

    fun possible(): Boolean {
        return when (color) {
            Color.RED -> 12 >= n
            Color.GREEN -> 13 >= n
            Color.BLUE -> 14 >= n
        }
    }

    override fun toString(): String {
        return "$color: $n"
    }
}

private data class Draw(val records: List<BallRecord>) {
    companion object {
        fun of(s: String): Draw {
            return Draw(
                s.split(", ")
                    .map { BallRecord.of(it) }
            )
        }
    }

    fun possible(): Boolean {
        return records.all { it.possible() }
    }
}

fun main() {
    fun parseInput(input: List<String>): List<List<Draw>> {
        return input.map { l ->
            val (_, ballsString) = l.split(": ")
            val records = ballsString.split("; ").map {
                Draw.of(it)
            }
            return@map records
        }
    }

    fun part1(input: List<String>): Int {
        val games = parseInput(input)
        return games.foldIndexed(0) { i, acc, g ->
            acc + if (g.all { it.possible() }) i + 1 else 0
        }
    }

    fun part2(input: List<String>): Int {
        val games = parseInput(input)
        return games.fold(0) { acc, g: List<Draw> ->
            acc + g.flatMap { it.records }
                .groupingBy { it.color }
                .fold(0) { currMax, br -> max(currMax, br.n) }
                .values
                .reduce { a, v -> a * v }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()

    check(part2(testInput) == 2286)
    part2(input).println()
}
