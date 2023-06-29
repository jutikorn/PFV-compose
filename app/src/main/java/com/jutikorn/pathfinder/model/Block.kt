package com.jutikorn.pathfinder.model

data class Board(
    val blockSize: Int = 30,
    val matrix: List<List<Block>> = emptyList(),
    val state: State = State.LOADING,
) {
    val size = matrix.size

    enum class State {
        LOADING, RUNNING, IDLE
    }
}

data class Block(
    val distance: Int = 0,
    val state: State = State.REGULAR,
) {
    enum class State {
        REGULAR, DESTINATION, SOURCE, VISITED, OBSTRACLE, PATH
    }
}
