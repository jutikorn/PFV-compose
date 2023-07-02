package com.jutikorn.pathfinder.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jutikorn.pathfinder.Directions
import com.jutikorn.pathfinder.R
import com.jutikorn.pathfinder.Traversal
import com.jutikorn.pathfinder.ViewState
import com.jutikorn.pathfinder.model.Board

@Composable
fun ControlPanel(
    traversals: List<Traversal> = Traversal.values().toList(),
    selectedTraversals: Traversal = Traversal.BFS,
    state: Board.State = Board.State.IDLE,
    showWeight: Boolean = true,
    directions: List<Directions> = Directions.values().toList(),
    selectedDir: Directions = Directions.FOUR,
    onRenderClick: () -> Unit = {},
    onResetClick: () -> Unit = {},
    onCheckedChange: ((Boolean) -> Unit) = {},
    onItemSelected: (Traversal) -> Unit = {},
    onOptionSelected: (Directions) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Button(
                modifier = Modifier.padding(end = 4.dp),
                enabled = state == Board.State.IDLE,
                onClick = { onRenderClick.invoke() },
            ) {
                Text(text = "Render")
            }
            Button(
                modifier = Modifier.padding(end = 4.dp),
                onClick = { onResetClick.invoke() },
            ) {
                Text(text = "Reset")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row {
                Checkbox(
                    checked = showWeight,
                    onCheckedChange = onCheckedChange,
                    enabled = selectedTraversals != Traversal.DFS &&
                        selectedTraversals != Traversal.BFS &&
                        state == Board.State.IDLE,
                )
                Text(text = "With Weight")
            }

            DirectionOptionsView(
                enabled = state == Board.State.IDLE,
                selectedDir = selectedDir,
                directions = directions,
                onOptionSelected = onOptionSelected,
            )

            Spinner(
                enabled = state == Board.State.IDLE,
                modifier = Modifier
                    .fillMaxWidth(),
                items = traversals,
                selectedItem = selectedTraversals,
                onItemSelected = onItemSelected,
                selectedItemFactory = { mod, t ->

                    Box(
                        modifier = mod
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                1.dp,
                                Color.Black,
                                shape = RoundedCornerShape(8.dp),
                            ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Center),
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(1f),
                                text = stringResource(id = t.toDisplayName()),
                                style = MaterialTheme.typography.subtitle2,
                            )

                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "",
                                modifier = Modifier.align(CenterVertically),
                                tint = Color.Black,
                            )
                        }
                    }
                },
                dropdownItemFactory = { t, _ ->
                    Text(text = stringResource(id = t.toDisplayName()))
                },
            )
        }
    }
}

@StringRes
fun Traversal.toDisplayName(): Int {
    return when (this) {
        Traversal.DFS -> R.string.algorithm_dfs
        Traversal.BFS -> R.string.algorithm_bfs
        Traversal.DIJKSTRA -> R.string.algorithm_dijkstra
        Traversal.ASTAR_MANHATTAN -> R.string.algorithm_a_star_manhattan
        Traversal.ASTAR_EUCLIDEAN -> R.string.algorithm_a_star_euclidean
    }
}

@StringRes
fun Directions.toDisplayName(): Int {
    return when (this) {
        Directions.FOUR -> R.string.direction_4
        Directions.EIGHT -> R.string.direction_8
    }
}

@Composable
fun DirectionOptionsView(
    enabled: Boolean = true,
    directions: List<Directions> = Directions.values().toList(),
    selectedDir: Directions = Directions.FOUR,
    onOptionSelected: (Directions) -> Unit = {},
) {
    Column {
        directions.forEach { dir ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (dir == selectedDir),
                        onClick = {
                            onOptionSelected(dir)
                        },
                    )
                    .padding(horizontal = 16.dp),
            ) {
                RadioButton(
                    enabled = enabled,
                    selected = (dir == selectedDir),
                    onClick = { onOptionSelected(dir) },
                )
                Text(
                    text = stringResource(id = dir.toDisplayName()),
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview_ControlPanel() {
    ControlPanel()
}

@Composable
fun MapScreen(
    viewState: ViewState,
    onClick: () -> Unit = {},
    onStopClick: () -> Unit,
    onItemSelected: (Traversal) -> Unit = {},
    onSizeUpdated: (width: Float, height: Float) -> Unit = { _, _ -> },
    onShowWeight: ((Boolean) -> Unit) = {},
    onOptionSelected: (Directions) -> Unit = {},
) {
    // Create element height in pixel state
    var columnHeightPx by remember { mutableStateOf(0f) }

    // Create element height in dp state
    var columnWidthPx by remember { mutableStateOf(0f) }

    Column(modifier = Modifier.fillMaxSize()) {
        ControlPanel(
            onRenderClick = onClick,
            state = viewState.board.state,
            selectedTraversals = viewState.selectedTraversal,
            traversals = viewState.traversals,
            onItemSelected = onItemSelected,
            onResetClick = onStopClick,
            onCheckedChange = onShowWeight,
            showWeight = viewState.showWeight,
            directions = viewState.directionsOptions,
            selectedDir = viewState.selectedDirectionsOption,
            onOptionSelected = onOptionSelected,
        )
        PathMap(
            board = viewState.board,
            showWeight = viewState.showWeight &&
                viewState.selectedTraversal != Traversal.DFS &&
                viewState.selectedTraversal != Traversal.BFS,
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .align(CenterHorizontally)
                .onGloballyPositioned { coordinates ->
                    // Set column height using the LayoutCoordinates
                    val w = coordinates.size.width.toFloat()
                    val h = coordinates.size.height.toFloat()

                    if (w != columnWidthPx || h != columnHeightPx) {
                        columnWidthPx = w
                        columnHeightPx = h
                        onSizeUpdated.invoke(columnWidthPx, columnHeightPx)
                    }
                },
        )
    }
}
