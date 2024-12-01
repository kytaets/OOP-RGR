package com.rgr.Shapes.Interfaces

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint

interface IRectangleShape {
  fun drawRectangleBase(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float, paint: Paint, isDrawing: Boolean) {
    paint.style = Paint.Style.STROKE

    paint.pathEffect = when (isDrawing) {
      true -> DashPathEffect(floatArrayOf(20f, 10f), 0f)
      else -> null
    }

    canvas.drawRect(startX, startY, endX, endY, paint)

  }
}