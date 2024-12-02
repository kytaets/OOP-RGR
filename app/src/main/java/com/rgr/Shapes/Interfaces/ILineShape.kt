package com.rgr.Shapes.Interfaces

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.DashPathEffect

interface ILineShape {

  fun drawCoordLine(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float,  paint: Paint, isDrawing: Boolean) {

    paint.pathEffect = when (isDrawing) {
      true -> DashPathEffect(floatArrayOf(20f, 10f), 0f)
      false -> null
    }
    canvas.drawLine(startX, startY, endX, endY, paint)
  }
}