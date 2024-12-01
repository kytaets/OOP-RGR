package com.rgr.Shapes.Interfaces

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

interface IEllipseShape {
  fun drawCircle(canvas: Canvas, centerX: Float, centerY: Float, radius: Float, paint: Paint) {
    val finalRadius = if (centerX == 0f) 0f else radius

    val basicCirclePaint: Paint = paint
    basicCirclePaint.style = Paint.Style.STROKE

    val circlePaint: Paint = Paint().apply {
      color = Color.parseColor("#59BFFF")
      style = Paint.Style.FILL
      strokeWidth = 8f
    }

    canvas.drawOval(
      centerX - finalRadius, centerY - finalRadius,
      centerX + finalRadius, centerY + finalRadius,
      circlePaint
    )

    canvas.drawOval(
      centerX - finalRadius, centerY - finalRadius,
      centerX + finalRadius, centerY + finalRadius,
      basicCirclePaint
    )
  }

}