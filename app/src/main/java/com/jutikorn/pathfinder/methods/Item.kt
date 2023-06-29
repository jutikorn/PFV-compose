package com.jutikorn.pathfinder.methods

data class Item(
    val indices: IntArray,
    val pathWays: List<IntArray> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (!indices.contentEquals(other.indices)) return false
        if (pathWays != other.pathWays) return false

        return true
    }

    override fun hashCode(): Int {
        var result = indices.contentHashCode()
        result = 31 * result + pathWays.hashCode()
        return result
    }
}
