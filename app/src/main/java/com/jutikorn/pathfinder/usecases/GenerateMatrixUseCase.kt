package com.jutikorn.pathfinder.usecases

import com.jutikorn.pathfinder.ext.getRandomBoolean
import com.jutikorn.pathfinder.model.Block
import com.jutikorn.pathfinder.model.Board
import kotlin.random.Random

class GenerateMatrixUseCase {

    operator fun invoke(screenWidth: Int, screenHeight: Int, obstacleProbability: Double = 15.00): Board {
        val rows = (screenHeight / BLOCK_SIZE_WITH_PADDING).toInt()
        val cols = (screenWidth / BLOCK_SIZE_WITH_PADDING).toInt()

        val blocks = (0 until rows).map { row ->
            (0 until cols).map { col ->
                Block(
                    state = getSquareState(row, col, rows, cols, obstacleProbability),
                    distance = Random.nextInt(1, 11),
                )
            }
        }

        return Board(blockSize = BLOCK_SIZE, matrix = blocks, state = Board.State.IDLE)
    }

    private fun getSquareState(
        row: Int,
        col: Int,
        rows: Int,
        cols: Int,
        obstacleProbability: Double = 10.00,
    ): Block.State =
        when {
            row == 0 && col == 0 -> Block.State.SOURCE
            row != (rows - 1) && col != (cols - 1) && getRandomBoolean(obstacleProbability) -> {
                Block.State.OBSTRACLE
            }
            else -> {
                Block.State.REGULAR
            }
        }

    companion object {
        const val BLOCK_SIZE_WITH_PADDING = 18.5
        const val BLOCK_SIZE = 16
    }
}
