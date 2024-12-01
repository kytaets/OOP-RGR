package com.rgr.Shapes

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

abstract class Shape(val name: String) {

    private val defaultPaint = Paint().apply {
        strokeWidth = 8f
        color = Color.BLACK
    }

    private val highlightPaint = Paint().apply {
        strokeWidth = 8f
        color = Color.RED
    }

    val paint: Paint = Paint()

    var highlighted: Boolean = false

    var startX:  Float = 0f
    var startY:  Float = 0f
    var endX:  Float = 0f
    var endY: Float = 0f


    init {
        paint.strokeWidth = 8f
    }

    open fun setCoordinates(startX: Float, startY: Float, endX: Float, endY: Float) {
        this.startX = startX
        this.startY = startY
        this.endX = endX
        this.endY = endY
    }

    protected fun getPaint(isHighlighted: Boolean): Paint {
        return if (isHighlighted) {
            highlightPaint
        } else {
            defaultPaint
        }
    }

    abstract fun draw(canvas: Canvas, isHighlighted: Boolean, isDrawing: Boolean)
}
