package com.jutikorn.pathfinder

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.jutikorn.pathfinder.ui.MapScreen
import com.jutikorn.pathfinder.ui.theme.PathfinderTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PathfinderTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val value by viewModel.state.collectAsState()
                    MapScreen(
                        viewState = value,
                        onClick = {
                            viewModel.dispatch(ViewAction.RenderClick)
                        },
                        onSizeUpdated = { screenWidth, screenHeight ->
                            viewModel.dispatch(
                                ViewAction.Initialize(
                                    pxToDp(screenWidth.toInt()),
                                    pxToDp(screenHeight.toInt()),
                                ),
                            )
                        },
                        onItemSelected = {
                            viewModel.dispatch(ViewAction.SelectTraversal(it))
                        },
                        onStopClick = {
                            viewModel.dispatch(ViewAction.StopRenderClick)
                        },
                        onShowWeight = {
                            viewModel.dispatch(ViewAction.OnWeightCheckBoxChanged(it))
                        },
                        onOptionSelected = {
                            viewModel.dispatch(ViewAction.SelectDirectionsOption(it))
                        },
                    )
                }
            }
        }
    }
}

fun pxToDp(px: Int): Int {
    return (px / Resources.getSystem().displayMetrics.density).toInt()
}

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}
