package com.example.humaninteraction.ui

import android.util.Log
import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.humaninteraction.ui.theme.HumanInteractionTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import kotlin.random.Random

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(columnCount: Int = 6, rowCount: Int = 4) {
    val isShow = remember {
        mutableStateOf(false)
    }
    val button = remember {
        mutableStateOf(false)
    }
    val isResultCell = remember {
        mutableStateOf(Pair(-1, -1))
    }
    val isSelectPressCell = remember {
        mutableStateOf(false)
    }
    val selectedCell = remember {
        mutableStateOf(Pair(-1, -1))
    }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenHeight = remember(density) {
        with(density) {
            configuration.screenHeightDp.dp.roundToPx()
        }
    }
    val targetX = remember(density) {
        with(density) {
            configuration.screenWidthDp.dp.roundToPx()
        }
    }
    val startPosition = remember(density) {
        with(density) {
            -50.dp.roundToPx()
        }
    }
    val xPositionRange = remember(density) {
        with(density) {
            Pair(50.dp.roundToPx(),targetX-50.dp.roundToPx())
        }
    }


    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(startPosition.toFloat()) }

    /*val offsetX: Dp by animateDpAsState(
        0.dp,
        tween(durationMillis = 5000, easing = LinearEasing)
    )
    val offsetY: Dp by animateDpAsState(
        if (button.value)0.dp else screenHeight-100.dp,
        tween(durationMillis = 5000, easing = LinearEasing)
    )*/

    LaunchedEffect(key1 = button.value) {
        if (button.value) {

           /* launch {
                offsetX.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 5000,
                        easing = LinearEasing
                    )
                )
            }*/

            launch {
                offsetY.animateTo(
                    targetValue = screenHeight.toFloat(),
                    animationSpec = tween(
                        durationMillis = 5000,
                        easing = LinearEasing
                    )
                )
            }

        } else {
            offsetY.snapTo(startPosition.toFloat())
            offsetX.snapTo((xPositionRange.first..xPositionRange.second).random().toFloat())
        }
    }
    LaunchedEffect(isSelectPressCell.value, selectedCell.value) {
        if (isSelectPressCell.value && selectedCell.value.first != -1 && selectedCell.value.second != -1) {
            isResultCell.value = selectedCell.value


        }
    }
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        //val boxWithConstraintsScope = this
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(color = Color.Blue)
            ) {
                repeat(columnCount) { column ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        repeat(rowCount) { row ->
                            Box(modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .border(width = 0.5.dp, color = Color.White)
                                .pointerInput(Unit) {
                                    detectTapGestures(onPress = {
                                        if (isResultCell.value.first != -1 && isResultCell.value.second != -1) {
                                            return@detectTapGestures
                                        }
                                        if (columnCount - 1 == column) {
                                            isSelectPressCell.value = true
                                            delay(300)
                                            isShow.value = true
                                            val released = try {
                                                tryAwaitRelease()
                                            } catch (c: CancellationException) {
                                                false
                                            }
                                            if (released) {
                                                isShow.value = false
                                                isSelectPressCell.value = false
                                            }
                                        } else {
                                            selectedCell.value = Pair(column, row)
                                            val released = try {
                                                tryAwaitRelease()
                                            } catch (c: CancellationException) {
                                                false
                                            }
                                            if (released) {
                                                selectedCell.value = Pair(-1, -1)
                                            }
                                        }
                                    })
                                }) {
                                if ((columnCount - 1) == column) {
                                    Text(
                                        modifier = Modifier
                                            .padding(bottom = 10.dp)
                                            .align(Alignment.BottomCenter),
                                        text = "Press ${row + 1}"
                                    )
                                } else {
                                    if (isResultCell.value.first != -1 && isResultCell.value.second != -1) {
                                        Text(
                                            modifier = Modifier.align(Alignment.Center),
                                            text = if (isResultCell.value.first == column && isResultCell.value.second == row) "${((column * rowCount) + row) + 1}" else ""
                                        )
                                    } else {
                                        Text(
                                            modifier = Modifier.align(Alignment.Center),
                                            text = if (isShow.value) "${((column * rowCount) + row) + 1}" else ""
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }

            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                isShow.value = false
                isSelectPressCell.value = false
                selectedCell.value = Pair(-1, -1)
                isResultCell.value = Pair(-1, -1)
                button.value = !button.value

                // offsetY.snapTo(0f)


            }) {
                Text(text = "RESET")
            }
        }

        Box(modifier = Modifier
            .offset {
                IntOffset(
                    offsetX.value.toInt(),
                    offsetY.value.toInt()
                )
            }
            .size(50.dp)
            .clip(CircleShape)
            .background(color = Color.Black)
            .clickable {

            }
        ) {

        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HumanInteractionTheme {
        HomeScreen()
    }
}