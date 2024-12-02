package com.rgr.Shapes

import android.graphics.Canvas
import android.graphics.DashPathEffect

class DashedLineShape: Shape("Пунктирна лінія") {

  override fun draw(canvas: Canvas, isHighlighted: Boolean, isDrawing: Boolean) {
    val paint = getPaint(isHighlighted)

    paint.pathEffect = DashPathEffect(floatArrayOf(50f, 20f), 0f)
    canvas.drawLine(startX, startY, endX, endY, paint)
  }
}