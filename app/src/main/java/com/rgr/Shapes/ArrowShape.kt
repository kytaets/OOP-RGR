package com.rgr.Shapes

import android.graphics.Canvas
import com.rgr.Shapes.Interfaces.ILineShape
import com.rgr.Shapes.Interfaces.ITriangleShape

class ArrowShape : Shape("Стрілка"), ITriangleShape, ILineShape {

  override fun draw(canvas: Canvas, isHighlighted: Boolean, isDrawing: Boolean) {
    val paintToUse = getPaint(isHighlighted)

    drawCoordLine(canvas, startX, startY, endX, endY, paintToUse, isDrawing)

    drawTriangle(canvas, startX, startY, endX, endY, paintToUse)
  }
}

