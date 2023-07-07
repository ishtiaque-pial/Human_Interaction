package com.example.humaninteraction.ui

import android.util.Log
import android.view.MotionEvent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Intersects() {
    val circleCenter = Offset(200f, 150f)
    val lineStart = remember {
        mutableStateOf( Offset(0f, 0f))
    }
    val lineEnd = remember {
        mutableStateOf( Offset(0f, 0f))
    }
    val isIntersect = remember {
        mutableStateOf(false)
    }


    val offsetY = remember { Animatable(0f) }
    val path = remember { Path() }

    LaunchedEffect(key1 = true){
        launch {
            offsetY.animateTo(
                targetValue = 570f,
                animationSpec = tween(
                    durationMillis = 10000,
                    easing = LinearEasing
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw circle
            drawCircle(
                color = if (isIntersect.value) Color.Red else Color.Blue,
                radius = 50f,
                center = Offset(200f,offsetY.value),
                style = Stroke(width = 2f.dp.toPx())
            )

            // Draw line
            /*drawLine(
                color = Color.Red,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 2f.dp.toPx()
            )*/

            // Calculate distance
            val distance = calculateDistance(lineStart.value.x,lineEnd.value.x,circleCenter.x,lineStart.value.y,lineEnd.value.y,offsetY.value)

            Log.e("Sdfsdf",""+distance)
            if (distance==50f){
                Log.e("Sdfsdf","Touch")
            } else if (50f>distance){
                isIntersect.value=true
                Log.e("Sdfsdf","Intersect")
            } else {
                Log.e("Sdfsdf","Outside")
            }
            // Draw distance text
        }
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        path.reset()
                        lineStart.value= Offset(it.x,it.y)
                        path.moveTo(it.x, it.y)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        lineEnd.value= Offset(it.x,it.y)
                        path.lineTo(it.x, it.y)
                    }

                    MotionEvent.ACTION_UP -> {
                        lineEnd.value= Offset(it.x,it.y)
                        path.reset()
                    }

                    else -> false
                }
                true
            }){

            drawPath(path =path, color = Color.Red, style = Stroke(width = 4.dp.toPx()))
        }
    }
}

fun calculateDistance(x1:Float,x2:Float,x0:Float,y1:Float,y2:Float,y0:Float): Float {

    val topPart= ((x2-x1)*(y1-y0)-(x1-x0)*(y2-y1))
    val bottomPart= sqrt((x2-x1).pow(2)+ (y2-y1).pow(2))
    val res = topPart/bottomPart

    return if (res<0) (res*-1) else res
}