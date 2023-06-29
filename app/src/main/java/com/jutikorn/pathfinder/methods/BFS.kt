package com.jutikorn.pathfinder.methods

import com.jutikorn.pathfinder.model.Block
import com.jutikorn.pathfinder.model.Board
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.LinkedList
import java.util.Queue

class BFS {

    operator fun invoke(input: Board) = flow {
        val queue: Queue<Item> = LinkedList()
        val visited: HashSet<String> = HashSet()

        val matrix = input.copy().matrix.map { it.toMutableList() }.toMutableList()
        var board = input
        val rowDest = board.matrix.size - 1
        val colDest = board.matrix[0].size - 1

        queue.add(Item(indices = intArrayOf(0, 0), pathWays = listOf(intArrayOf(0, 0))))

        var pathWays: List<IntArray> = emptyList()

        while (queue.isEmpty().not()) {
            val qSize = queue.size
            for (k in 0 until qSize) {
                delay(DELAY_TIME_MILLISECONDS)
                val item = queue.poll()
                val points = item.indices
                pathWays = item.pathWays
                val i = points.getOrDefault(0)
                val j = points.getOrDefault(1)

                if (board.isWalkable(i, j).not()) continue
                if (visited.hasVisited(i, j)) continue

                if (i == rowDest && j == colDest) {
                    // emit destination found
                    board = emitBlockState(
                        row = i,
                        col = j,
                        matrix = matrix,
                        board = board,
                        state = Block.State.DESTINATION,
                    )
                    queue.clear()
                    break
                }

                visited.add(i, j)
                // emit matrix here as it has been visited
                board = emitBlockState(
                    row = i,
                    col = j,
                    matrix = matrix,
                    board = board,
                    state = Block.State.VISITED,
                )

                for (dir in DIRECTIONS) {
                    val newRow = i + dir[0]
                    val newCol = j + dir[1]

                    if (board.isWalkable(newRow, newCol) &&
                        visited.hasVisited(newRow, newCol).not()
                    ) {
                        queue.add(
                            Item(
                                indices = intArrayOf(newRow, newCol),
                                pathWays = item.pathWays + intArrayOf(newRow, newCol),
                            ),
                        )
                    }
                }
            }
        }
        pathWays.forEach { indices ->
            delay(DELAY_TIME_MILLISECONDS)
            val row = indices[0]
            val col = indices[1]
            board = emitBlockState(
                row = row,
                col = col,
                matrix = matrix,
                board = board,
                state = Block.State.PATH,
            )
        }
        emit(board.copy(state = Board.State.IDLE))
    }

    private fun IntArray?.getOrDefault(index: Int, default: Int = -1): Int = this?.get(index) ?: default
}
