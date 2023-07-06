package com.example.humaninteraction.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun Intersects() {
    val circleCenter = Offset(210f, 115f)
    val lineStart = Offset(220f, 20f)
    val lineEnd = Offset(220f, 100f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw circle
            drawCircle(
                color = Color.Blue,
                radius = 50f,
                center = circleCenter,
                style = Stroke(width = 2f.dp.toPx())
            )

            // Draw line
            drawLine(
                color = Color.Red,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 2f.dp.toPx()
            )

            // Calculate distance
            val distance = calculateDistance(lineStart.x,lineEnd.x,circleCenter.x,lineStart.y,lineEnd.y,center.y)

            Log.e("Sdfsdf",""+distance)
            if (distance==50f){
                Log.e("Sdfsdf","Touch")
            } else if (50f>distance){
                Log.e("Sdfsdf","Intersect")
            } else {
                Log.e("Sdfsdf","Outside")
            }
            // Draw distance text
        }
    }
}

fun calculateDistance(x1:Float,x2:Float,x0:Float,y1:Float,y2:Float,y0:Float): Float {

    val topPart= ((x2-x1)*(y1-y0)-(x1-x0)*(y2-y1))
    val bottomPart= sqrt((x2-x1).pow(2)+ (y2-y1).pow(2))

    return topPart/bottomPart
}