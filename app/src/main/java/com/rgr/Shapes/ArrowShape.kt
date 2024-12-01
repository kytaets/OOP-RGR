package com.rgr.Shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.rgr.Shapes.Interfaces.IEllipseShape
import com.rgr.Shapes.Interfaces.ILineShape
import com.rgr.Shapes.Interfaces.ITriangleShape

class ArrowShape : Shape("Стрілка"), ITriangleShape, ILineShape {

  override fun draw(canvas: Canvas, isHighlighted: Boolean, isDrawing: Boolean) {
    val paintToUse = getPaint(isHighlighted)

    // Draw the main line of the arrow using ILineShape
    drawCoordLine(canvas, startX, startY, endX, endY, paintToUse, isDrawing)

    // Draw the arrowhead (triangle) using ITriangleShape
    drawTriangle(canvas, startX, startY, endX, endY, paintToUse)
  }
}

