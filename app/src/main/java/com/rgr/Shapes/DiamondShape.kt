package com.rgr.Shapes

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path

class DiamondShape : Shape("Diamond") {

  override fun draw(canvas: Canvas, isHighlighted: Boolean, isDrawing: Boolean) {
    val paint = getPaint(isHighlighted)

    paint.style = Paint.Style.STROKE
    paint.pathEffect = if (isDrawing) DashPathEffect(floatArrayOf(20f, 10f), 0f) else null

    val centerX = (startX + endX) / 2
    val centerY = (startY + endY) / 2

    val path = Path().apply {
      moveTo(centerX, startY)
      lineTo(endX, centerY)
      lineTo(centerX, endY)
      lineTo(startX, centerY)
      close()
    }

    canvas.drawPath(path, paint)

    if (!isDrawing) {
      val fillPaint = Paint(paint).apply {
        style = Paint.Style.FILL
        pathEffect = null
        color = Color.parseColor("#3572EF")
      }
      canvas.drawPath(path, fillPaint)
    }
  }
}