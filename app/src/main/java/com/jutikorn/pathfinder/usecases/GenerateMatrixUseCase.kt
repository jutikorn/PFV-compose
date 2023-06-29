package com.jutikorn.pathfinder.usecases

import com.jutikorn.pathfinder.ext.getRandomBoolean
import com.jutikorn.pathfinder.model.Block
import com.jutikorn.pathfinder.model.Board
import kotlin.random.Random

class GenerateMatrixUseCase {

    operator fun invoke(screenWidth: Int, screenHeight: Int, obstacleProbability: Double = 15.00): Board {

        val rows = screenHeight / 26
        val cols = screenWidth / 26

        val blocks = (0 until rows).map { row ->
            (0 until cols).map { col ->
                Block(
                    state = getSquareState(row, col, rows, cols, obstacleProbability),
                    distance = Random.nextInt(1, 11)
                )
            }
        }

        return Board(blockSize = 24, matrix = blocks, state = Board.State.IDLE)
    }

    private fun getSquareState(
        row: Int,
        col: Int,
        rows: Int,
        cols: Int,
        obstacleProbability: Double = 10.00
    ): Block.State =
        when {
            row == 0 && col == 0 -> Block.State.SOURCE
            row != (rows-1)  && col != (cols-1)  && getRandomBoolean(obstacleProbability) -> {
                Block.State.OBSTRACLE
            }
            else -> {
                Block.State.REGULAR
            }
        }
}