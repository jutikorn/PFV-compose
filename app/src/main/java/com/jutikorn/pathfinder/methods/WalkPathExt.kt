package com.jutikorn.pathfinder.methods

import com.jutikorn.pathfinder.model.Block
import com.jutikorn.pathfinder.model.Board
import kotlinx.coroutines.flow.FlowCollector

fun Board.isInBound(i: Int, j: Int): Boolean {
    return i >= 0 && j >= 0 && i < this.size && j < this.matrix[i].size
}

fun Board.isWalkable(i: Int, j: Int): Boolean {
    return isInBound(i, j) &&
        this.matrix[i][j].state != Block.State.OBSTRACLE &&
        this.matrix[i][j].state != Block.State.VISITED
}

fun HashSet<String>.add(i: Int, j: Int) {
    this.add("row:$i col:$j")
}

fun HashSet<String>.hasVisited(i: Int, j: Int): Boolean = this.contains("row:$i col:$j")

internal suspend fun FlowCollector<Board>.emitBlockState(
    row: Int,
    col: Int,
    matrix: MutableList<MutableList<Block>>,
    board: Board,
    state: Block.State,
): Board {
    val rows = matrix[row].toMutableList()
    rows[col] = rows[col].copy(state = state)
    matrix[row] = rows
    val newPathState = board.copy(matrix = matrix.toList())
    emit(newPathState)
    return newPathState
}