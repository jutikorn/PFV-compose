package com.jutikorn.pathfinder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jutikorn.pathfinder.model.Board
import com.jutikorn.pathfinder.usecases.GenerateMatrixUseCase
import com.jutikorn.pathfinder.usecases.RenderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ViewState(
    val board: Board = Board(),
    val selectedTraversal: Traversal = Traversal.DIJKSTRA,
    val traversals: List<Traversal> = Traversal.values().toList(),
    val originalBoard: Board = Board(),
    val showWeight: Boolean = false,
    val directionsOptions: List<Directions> = Directions.values().toList(),
    val selectedDirectionsOption: Directions = Directions.FOUR,
)

enum class Traversal {
    DFS, BFS, DIJKSTRA, ASTAR_EUCLIDEAN, ASTAR_MANHATTAN
}

enum class Directions {
    FOUR, EIGHT
}

sealed interface ViewAction {
    data class Initialize(
        val screenWidth: Int,
        val screenHeight: Int,
    ) : ViewAction

    object RenderClick : ViewAction
    object StopRenderClick : ViewAction

    data class SelectTraversal(val method: Traversal) : ViewAction

    data class OnWeightCheckBoxChanged(val show: Boolean) : ViewAction

    data class SelectDirectionsOption(val directions: Directions) : ViewAction
}

class MainViewModel(
    private val generateMatrixUseCase: GenerateMatrixUseCase,
    private val renderUseCase: RenderUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
    val state = _state.asStateFlow()

    fun dispatch(action: ViewAction) {
        when (action) {
            is ViewAction.Initialize -> {
                viewModelScope.launch {
                    setState {
                        val initMatrix = generateMatrixUseCase(action.screenWidth, action.screenHeight)
                        copy(board = initMatrix, originalBoard = initMatrix)
                    }
                }
            }
            is ViewAction.SelectTraversal -> {
                viewModelScope.coroutineContext.cancelChildren()
                setState {
                    copy(selectedTraversal = action.method, board = this.originalBoard)
                }
            }
            ViewAction.RenderClick -> {
                setState { copy(board = state.value.board.copy(state = Board.State.RUNNING)) }
                viewModelScope.coroutineContext.cancelChildren()
                viewModelScope.launch {
                    renderUseCase.invoke(
                        state.value.selectedTraversal,
                        state.value.selectedDirectionsOption,
                        state.value.board,
                        state.value.showWeight,
                    ).flowOn(Dispatchers.IO).collect {
                        setState {
                            copy(board = it)
                        }
                    }
                }
            }
            ViewAction.StopRenderClick -> {
                viewModelScope.coroutineContext.cancelChildren()
                setState {
                    copy(board = this.originalBoard)
                }
            }
            is ViewAction.OnWeightCheckBoxChanged -> {
                viewModelScope.coroutineContext.cancelChildren()
                setState {
                    copy(showWeight = action.show, board = this.originalBoard)
                }
            }
            is ViewAction.SelectDirectionsOption -> {
                viewModelScope.coroutineContext.cancelChildren()
                setState {
                    copy(selectedDirectionsOption = action.directions, board = this.originalBoard)
                }
            }
        }
    }

    private fun setState(builder: ViewState.() -> ViewState) {
        _state.update(builder)
    }
}
