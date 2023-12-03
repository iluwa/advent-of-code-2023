fun main() {
    val stringToNumbers = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

    fun String.safeSubstring(r: IntRange): String {
        return this.substring(r.first until kotlin.math.min(r.last, this.length))
    }

    fun String.findFirstOfNumbers(reversed: Boolean = false): Char {
        val r = if (reversed) this.indices.reversed() else this.indices
        for (i in r) {
            if (this[i].isDigit()) {
                return this[i]
            } else if (stringToNumbers.containsKey(this.safeSubstring(i .. i + 5))) {
                return stringToNumbers[this.safeSubstring(i .. i + 5)]?.first()!!
            } else if (stringToNumbers.containsKey(this.safeSubstring(i .. i + 4))) {
                return stringToNumbers[this.safeSubstring(i .. i + 4)]?.first()!!
            } else if (stringToNumbers.containsKey(this.safeSubstring(i .. i + 3))) {
                return stringToNumbers[this.safeSubstring(i .. i + 3)]?.first()!!
            }
        }
        throw Exception()
    }


    fun concatTwoCharsToInt(ch1: Char, ch2: Char): Int = "$ch1$ch2".toInt()

    fun part1(input: List<String>): Int {
        return input.fold(0) { acc, l ->
            acc + concatTwoCharsToInt(
                l[l.indexOfFirst { i -> i.isDigit() }],
                l[l.indexOfLast { i -> i.isDigit() }]
            )
        }
    }

    fun part2(input: List<String>): Int {
        return input.fold(0) { acc, l ->
            acc + concatTwoCharsToInt(l.findFirstOfNumbers(), l.findFirstOfNumbers(true))
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()

    check(part2(testInput) == 281)
    part2(input).println()
}
