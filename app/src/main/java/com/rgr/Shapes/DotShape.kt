package com.rgr.Shapes

import android.graphics.Canvas

class DotShape : Shape("Крапка") {

  override fun draw(canvas: Canvas, isHighlighted: Boolean, isDrawing: Boolean) {
    val paint = getPaint(isHighlighted)
    if (!isDrawing) {
      canvas.drawCircle(startX, startY, 10f, paint)
    }
  }
}