package com.jutikorn.pathfinder.methods.helpers

import kotlin.math.abs
import kotlin.math.sqrt

fun calculateEuclideanDistance(sourceRow: Int, sourceCol: Int, targetRow: Int, targetCol: Int): Double {
    val dx = sourceRow - targetRow
    val dy = sourceCol - targetCol
    return sqrt((dx * dx + dy * dy).toDouble())
}

fun calculateManhattanDistance(sourceRow: Int, sourceCol: Int, targetRow: Int, targetCol: Int): Int {
    val dx: Int = abs(sourceRow - targetRow)
    val dy: Int = abs(sourceCol - targetCol)
    return dx + dy
}
