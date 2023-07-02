package com.jutikorn.pathfinder.usecases

import com.jutikorn.pathfinder.Directions
import com.jutikorn.pathfinder.Traversal
import com.jutikorn.pathfinder.methods.AStarEuclidean
import com.jutikorn.pathfinder.methods.AStarManhattan
import com.jutikorn.pathfinder.methods.BFS
import com.jutikorn.pathfinder.methods.DFS
import com.jutikorn.pathfinder.methods.Dijkstra
import com.jutikorn.pathfinder.model.Board
import kotlinx.coroutines.flow.Flow

class RenderUseCase(
    private val bfs: BFS,
    private val dijkstra: Dijkstra,
    private val dfs: DFS,
    private val aStarEuclidean: AStarEuclidean,
    private val aStarManhattan: AStarManhattan,
) {
    operator fun invoke(
        traversal: Traversal,
        directions: Directions,
        board: Board,
        withWeight: Boolean,
    ): Flow<Board> {
        return when (traversal) {
            Traversal.BFS -> bfs.invoke(board, directions)
            Traversal.DFS -> dfs.invoke(board, directions)
            Traversal.DIJKSTRA -> dijkstra.invoke(board, withWeight, directions)
            Traversal.ASTAR_EUCLIDEAN -> aStarEuclidean.invoke(board, withWeight, directions)
            Traversal.ASTAR_MANHATTAN -> aStarManhattan.invoke(board, withWeight, directions)
        }
    }
}
