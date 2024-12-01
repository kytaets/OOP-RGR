package com.rgr.Shapes.Interfaces

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.atan2

interface ITriangleShape {
  fun drawTriangle(
    canvas: Canvas,
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    paint: Paint
  ) {
    if (startX == 0f) {
      return
    }

    val arrowHeadLength = 30f
    val arrowAngle = atan2((startY - endY).toDouble(), (startX - endX).toDouble()).toFloat()

    val arrowLeftX = endX + arrowHeadLength * Math.cos(arrowAngle + Math.PI / 6).toFloat()
    val arrowLeftY = endY + arrowHeadLength * Math.sin(arrowAngle + Math.PI / 6).toFloat()
    val arrowRightX = endX + arrowHeadLength * Math.cos(arrowAngle - Math.PI / 6).toFloat()
    val arrowRightY = endY + arrowHeadLength * Math.sin(arrowAngle - Math.PI / 6).toFloat()

    val arrowHeadPath = Path().apply {
      moveTo(endX, endY)
      lineTo(arrowLeftX, arrowLeftY)
      lineTo(arrowRightX, arrowRightY)
      close()
    }

    canvas.drawPath(arrowHeadPath, paint)
  }
}
