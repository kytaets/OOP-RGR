package com.rgr.Shapes

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint

class EllipseShape : Shape("Еліпс") {

    override fun draw(canvas: Canvas, isHighlighted: Boolean,isDrawing: Boolean) {
        val paint = getPaint(isHighlighted)

        val basicPaint: Paint = paint
        basicPaint.style = Paint.Style.STROKE

        val fillPaint: Paint = Paint().apply {
            color = Color.parseColor("#59BFFF")
            style = Paint.Style.FILL
            strokeWidth = 8f
        }

        if (isDrawing) {
            paint.pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
            canvas.drawOval(startX, startY, endX, endY, paint)
        } else {
            paint.pathEffect = null

            canvas.drawOval(startX, startY, endX, endY, basicPaint)
            canvas.drawOval(startX, startY, endX, endY, fillPaint)
        }
    }
}
