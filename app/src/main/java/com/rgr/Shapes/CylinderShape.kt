package com.rgr.Shapes

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.abs

class CylinderShape : Shape("Циліндр") {

  override fun draw(canvas: Canvas, isHighlighted: Boolean, isDrawing: Boolean) {
    val paint = getPaint(isHighlighted)

    paint.style = Paint.Style.STROKE
    paint.pathEffect = if (isDrawing) DashPathEffect(floatArrayOf(20f, 10f), 0f) else null

    val radius = abs(endX - startX) / 2

    val topPath = Path().apply {
      addOval(startX, startY, endX, startY + radius, Path.Direction.CW)
    }
    canvas.drawPath(topPath, paint)

    val bottomPath = Path().apply {
      addOval(startX, endY - radius, endX, endY, Path.Direction.CW)
    }
    canvas.drawPath(bottomPath, paint)

    canvas.drawLine(startX, startY + radius / 2, startX, endY - radius / 2, paint)
    canvas.drawLine(endX, startY + radius / 2, endX, endY - radius / 2, paint)
  }
}
