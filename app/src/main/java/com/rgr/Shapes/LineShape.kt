package com.rgr.Shapes

import android.graphics.Canvas
import com.rgr.Shapes.Interfaces.ILineShape

open class LineShape : Shape("Лінія"), ILineShape {

    override fun draw(canvas: Canvas, isHighlighted: Boolean, isDrawing: Boolean) {
        val paint = getPaint(isHighlighted)
        drawCoordLine(canvas, startX, startY, endX, endY, paint, isDrawing)
    }

}
