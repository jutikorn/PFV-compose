package com.jutikorn.pathfinder.usecases

import com.jutikorn.pathfinder.Traversal
import com.jutikorn.pathfinder.methods.BFS
import com.jutikorn.pathfinder.methods.DFS
import com.jutikorn.pathfinder.methods.Dijkstra
import com.jutikorn.pathfinder.model.Board
import kotlinx.coroutines.flow.Flow

class RenderUseCase(
    private val bfs: BFS,
    private val dijkstra: Dijkstra,
    private val dfs: DFS
) {
    operator fun invoke(traversal: Traversal, Board: Board): Flow<Board> {
        return when (traversal) {
            Traversal.BFS -> bfs.invoke(Board)
            Traversal.DIJKSTRA -> dijkstra.invoke(Board)
            Traversal.DFS -> dfs.invoke(Board)
        }
    }
}