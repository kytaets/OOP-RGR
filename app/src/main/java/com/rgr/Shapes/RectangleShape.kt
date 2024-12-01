package com.rgr.Shapes

import android.graphics.Canvas
import com.rgr.Shapes.Interfaces.IRectangleShape

open class RectangleShape : Shape("Прямокутник"), IRectangleShape {

    override fun draw(canvas: Canvas, isHighlighted: Boolean, isDrawing: Boolean) {
        val paint = getPaint(isHighlighted)
        drawRectangleBase(canvas, startX, startY, endX, endY, paint, isDrawing)
    }

}
