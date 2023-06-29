package com.jutikorn.pathfinder.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jutikorn.pathfinder.model.Board
import com.jutikorn.pathfinder.ui.shape.SquareBox
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview
import com.jutikorn.pathfinder.model.Block

@Composable
fun PathMap(modifier: Modifier = Modifier.fillMaxHeight(), board: Board, showWeight: Boolean = true) {
    LazyColumn(modifier = modifier, content = {
        items(items = board.matrix) { row ->

            LazyRow(modifier = Modifier.fillMaxWidth(), content = {
                items(items = row) { block ->
                    SquareBox(
                        text = "${block.distance}",
                        state = block.state,
                        size = board.blockSize,
                        showWeight = showWeight
                    )
                }
            }
            )
        }
    })
}

@Preview
@Composable
fun PreviewPathMap(){
    val Board = Board(
        state = Board.State.IDLE,
        blockSize = 25,
        matrix = listOf(
            listOf(Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block()),
            listOf(Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block()),
            listOf(Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block()),
            listOf(Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block()),
            listOf(Block(), Block(), Block(), Block(state = Block.State.OBSTRACLE), Block(), Block(), Block(), Block(), Block(), Block()),
            listOf(Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block()),
            listOf(Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block()),
            listOf(Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block()),
            listOf(Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block()),
            listOf(Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block(), Block())
        )
    )
    PathMap(board = Board)
}

