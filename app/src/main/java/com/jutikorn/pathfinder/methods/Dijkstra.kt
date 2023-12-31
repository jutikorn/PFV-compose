package com.jutikorn.pathfinder.methods

import com.jutikorn.pathfinder.Directions
import com.jutikorn.pathfinder.ext.getOrDefault
import com.jutikorn.pathfinder.model.Block
import com.jutikorn.pathfinder.model.Board
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.PriorityQueue

class Dijkstra {

    operator fun invoke(
        input: Board,
        withWeight: Boolean,
        directions: Directions,
    ) = flow {
        // minheap
        val queue: PriorityQueue<Item> = PriorityQueue { a, b ->
            a.indices[2] - b.indices[2]
        }

        val visited: HashSet<String> = HashSet()

        val matrix = input.copy().matrix.map { it.toMutableList() }.toMutableList()
        var board = input
        val rowDest = board.matrix.size - 1
        val colDest = board.matrix[0].size - 1
        var pathWays: List<IntArray> = emptyList()

        queue.add(
            Item(
                indices = intArrayOf(
                    0, // row
                    0, // col
                    0, // gCost
                ),
                pathWays = listOf(intArrayOf(0, 0)),
            ),
        )

        while (queue.isEmpty().not()) {
            val qSize = queue.size
            for (k in 0 until qSize) {
                delay(DELAY_TIME_MILLISECONDS)
                val item = queue.poll() ?: break
                val points = item.indices
                pathWays = item.pathWays
                val i = points.getOrDefault(0)
                val j = points.getOrDefault(1)
                val gCost = points.getOrDefault(2)

                if (board.isWalkable(i, j).not()) continue
                if (visited.hasVisited(i, j)) continue

                if (i == rowDest && j == colDest) {
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

                for (dir in directions.dirs) {
                    val newRow = i + dir[0]
                    val newCol = j + dir[1]

                    if (board.isWalkable(newRow, newCol) && visited.hasVisited(newRow, newCol).not()) {
                        val newGCost = if (withWeight) {
                            gCost + board.matrix[newRow][newCol].distance
                        } else {
                            gCost + 1
                        }
                        val indices = intArrayOf(newRow, newCol, newGCost + board.matrix[newRow][newCol].distance)
                        val newWeight = newGCost + board.matrix[newRow][newCol].distance
                        queue.add(
                            Item(
                                indices = indices,
                                pathWays = item.pathWays + intArrayOf(newRow, newCol, newWeight),
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
}
