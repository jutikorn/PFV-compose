package com.jutikorn.pathfinder.ui.shape

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jutikorn.pathfinder.model.Block

@Preview
@Composable
fun SquareBoxPreview() {
    SquareBox(text = "100", size = 25)
}

@Preview
@Composable
fun SquareBoxVisitedPreview() {
    SquareBox(text = "9", state = Block.State.VISITED, size = 25)
}

@Preview
@Composable
fun SquareBoxObstaclePreview() {
    SquareBox(text = "12", state = Block.State.OBSTRACLE, size = 25)
}

@Preview
@Composable
fun SquareBoxDestPreview() {
    SquareBox(text = "12", state = Block.State.DESTINATION, size = 25)
}

@Composable
fun SquareBox(
    text: String = "",
    size: Int = 50,
    state: Block.State = Block.State.REGULAR,
    showWeight: Boolean = true
) {
    val transition = updateTransition(state, label = "box state")
    val color by transition.animateColor(
        transitionSpec = {
            when {
                Block.State.REGULAR isTransitioningTo Block.State.VISITED ->
                    spring(stiffness = 50f, dampingRatio = DampingRatioLowBouncy)
                else ->
                    tween(durationMillis = 500)
            }
        }, label = "color"
    ) { state ->
        when (state) {
            Block.State.DESTINATION -> Color.Green
            Block.State.OBSTRACLE -> Color.DarkGray
            Block.State.VISITED -> Color.Yellow
            Block.State.REGULAR -> Color.LightGray
            Block.State.SOURCE -> Color.Red
            Block.State.PATH -> Color.Cyan
        }
    }

    val value by animateDpAsState(
        targetValue = size.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Column(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(1.dp)
    ) {
        Box(
            modifier = Modifier
                .size(value)
                .clip(RectangleShape)
                .background(color = color)
        ) {
            if (state != Block.State.OBSTRACLE &&
                showWeight &&
                state != Block.State.DESTINATION &&
                state != Block.State.SOURCE
            ) {
                Text(
                    text = text,
                    color = Color.DarkGray,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = (size / 2.2).sp
                )
            }
        }
    }
}