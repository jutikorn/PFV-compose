package com.jutikorn.pathfinder.methods

const val DELAY_TIME_MILLISECONDS = 50L

internal val DIRECTIONS = arrayOf(
    intArrayOf(-1, 0),
    intArrayOf(0, -1),
    intArrayOf(0, 1),
    intArrayOf(1, 0),
)

internal val DIRECTIONS_WITH_DIAGONAL = DIRECTIONS + arrayOf(
    intArrayOf(-1, -1),
    intArrayOf(1, -1),
    intArrayOf(-1, 1),
    intArrayOf(1, 1),
)