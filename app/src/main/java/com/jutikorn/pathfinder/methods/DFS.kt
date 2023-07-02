package com.jutikorn.pathfinder.methods

import com.jutikorn.pathfinder.Directions
import com.jutikorn.pathfinder.model.Block
import com.jutikorn.pathfinder.model.Board
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class DFS {

    operator fun invoke(input: Board, directions: Directions) = flow<Board> {
        val visited: HashSet<String> = HashSet()
        val matrix = input.copy().matrix.map { it.toMutableList() }.toMutableList()
        var board = input
        val rowDest = board.matrix.size - 1
        val colDest = board.matrix[0].size - 1

        suspend fun dfs(item: Item): Pair<Boolean, List<IntArray>> {
            val i = item.indices[0]
            val j = item.indices[1]
            val pathways = item.pathWays

            if (board.isWalkable(i, j).not()) return false to pathways
            if (visited.hasVisited(i, j)) return false to pathways
            if (board.matrix[i][j].state == Block.State.OBSTRACLE) return false to pathways

            if (i == rowDest && j == colDest) {
                board = emitBlockState(
                    row = i,
                    col = j,
                    state = Block.State.DESTINATION,
                    matrix = matrix,
                    board = board,
                )
                return true to pathways
            }

            delay(DELAY_TIME_MILLISECONDS)

            visited.add(i, j)

            // emit matrix here as it has been visited
            board = emitBlockState(
                row = i,
                col = j,
                state = Block.State.VISITED,
                matrix = matrix,
                board = board,
            )

            for (dir in directions.dirs) {
                val newRow = i + dir[0]
                val newCol = j + dir[1]

                if (board.isWalkable(newRow, newCol) && visited.hasVisited(newRow, newCol).not()) {
                    val result = dfs(
                        Item(
                            indices = intArrayOf(newRow, newCol),
                            pathWays = pathways + intArrayOf(newRow, newCol),
                        ),
                    )
                    if (result.first) return true to result.second
                }
            }

            return false to pathways
        }

        val result = dfs(Item(indices = intArrayOf(0, 0), pathWays = listOf(intArrayOf(0, 0))))
        val pathWays = result.second
        pathWays.forEach { indices ->
            delay(DELAY_TIME_MILLISECONDS)
            val row = indices[0]
            val col = indices[1]
            board = emitBlockState(
                row = row,
                col = col,
                state = Block.State.PATH,
                matrix = matrix,
                board = board,
            )
        }
        emit(board.copy(state = Board.State.IDLE))
    }
}
