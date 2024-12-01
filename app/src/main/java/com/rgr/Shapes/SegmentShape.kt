package com.rgr.Shapes

import android.graphics.Canvas
import com.rgr.Shapes.Interfaces.IEllipseShape
import com.rgr.Shapes.Interfaces.ILineShape

class SegmentShape : Shape("Відрізок"), IEllipseShape, ILineShape {

  private val radius = 25f

  override fun draw(canvas: Canvas, isHighlighted: Boolean, isDrawing: Boolean) {
    val paint = getPaint(isHighlighted)
    drawCoordLine(canvas, startX, startY, endX, endY, paint, isDrawing)

    drawCircle(canvas, startX, startY, radius, paint)
    drawCircle(canvas, endX, endY, radius, paint)
  }
}

