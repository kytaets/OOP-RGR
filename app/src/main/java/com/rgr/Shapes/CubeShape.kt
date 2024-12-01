package com.rgr.Shapes

import android.graphics.Canvas
import com.rgr.Shapes.Interfaces.ILineShape
import com.rgr.Shapes.Interfaces.IRectangleShape

class CubeShape : Shape("Куб"), IRectangleShape, ILineShape {

  override fun draw(canvas: Canvas, isHighlighted: Boolean, isDrawing: Boolean) {
    val paint = getPaint(isHighlighted)
    val width = endX - startX
    val offset = width / 4

    drawCoordLine(canvas, startX, startY, startX + offset, startY - offset, paint, isDrawing)
    drawCoordLine(canvas, endX, startY, endX + offset, startY - offset, paint, isDrawing)
    drawCoordLine(canvas, startX, endY, startX + offset, endY - offset, paint, isDrawing)
    drawCoordLine(canvas, endX, endY, endX + offset, endY - offset, paint, isDrawing)

    drawRectangleBase(canvas, startX + offset, startY - offset, endX + offset, endY - offset, paint, isDrawing)
    drawRectangleBase(canvas, startX, startY, endX, endY, paint, isDrawing)
  }
}
