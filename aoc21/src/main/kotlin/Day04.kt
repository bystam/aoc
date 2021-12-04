import java.lang.IllegalStateException
import java.util.*

fun main(args: Array<String>) = solve(Day04())

class Day04: Day(4) {
    override fun task1(input: Input): Any {
        val game = input.lines.first().split(",").map { it.toInt() }
        val boards = Board.parse(input.lines.drop(2))

        val winningBoard = firstWinningBoard(boards, game)
        return winningBoard.score()
    }

    override fun task2(input: Input): Any {
        val game = input.lines.first().split(",").map { it.toInt() }
        val boards = Board.parse(input.lines.drop(2))

        val winningBoard = lastWinningBoard(boards, game)
        return winningBoard.score()
    }

    private fun firstWinningBoard(boards: List<Board>, game: List<Int>): Board {
        for (number in game) {
            for (board in boards) {
                if (board.place(number)) {
                    return board
                }
            }
        }
        throw IllegalStateException("No winner?")
    }

    private fun lastWinningBoard(boards: List<Board>, game: List<Int>): Board {
        val winOrder = mutableListOf<Board>()
        for (number in game) {
            for (board in boards) {
                if (board.place(number) && !winOrder.contains(board)) {
                    winOrder.add(board)
                    if (winOrder.size == boards.size) {
                        return board
                    }
                }
            }
        }
        throw IllegalStateException("No winner?")
    }

    class Board {
        private val grid: MutableList<MutableList<Square>> = mutableListOf()
        private val lookup: MutableMap<Int, Pair<Int, Int>> = mutableMapOf()
        private val numbersPlayed: MutableList<Int> = mutableListOf()

        fun score(): Int {
            val unmarkedSum = grid.flatten().filter { !it.marked }.sumOf { it.number }
            return unmarkedSum * numbersPlayed.last()
        }

        fun place(number: Int): Boolean {
            val (r, c) = lookup[number] ?: return false
            val square = grid[r][c]
            assert(!square.marked)
            numbersPlayed.add(number)
            square.marked = true
            return grid.all { it[c].marked }
                    || grid[r].all { it.marked }
        }

        companion object {
            fun parse(lines: Sequence<String>): List<Board> {
                val whitespace = "\\s+".toRegex()
                val output = mutableListOf<Board>()
                var current = Board()
                for (line in lines) {
                    if (line.isEmpty()) {
                        output.add(current)
                        current = Board()
                        continue
                    }
                    val row = line.split(whitespace)
                        .filter { it.isNotEmpty() }
                        .map { Square(it.toInt()) }
                        .toMutableList()
                    val rowIndex = current.grid.size
                    current.grid.add(row)
                    row.forEachIndexed { column, square ->
                        current.lookup[square.number] = rowIndex to column
                    }
                }
                assert(current.grid.size == 5)
                output.add(current)
                return output
            }
        }
    }

    class Square(
        val number: Int,
        var marked: Boolean = false
    )
}