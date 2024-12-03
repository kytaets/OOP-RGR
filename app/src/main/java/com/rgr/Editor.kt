package com.rgr

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.rgr.Shapes.ArrowShape
import com.rgr.Shapes.CubeShape
import com.rgr.Shapes.CylinderShape
import com.rgr.Shapes.DashedLineShape
import com.rgr.Shapes.DiamondShape
import com.rgr.Shapes.DotShape
import com.rgr.Shapes.EllipseShape
import com.rgr.Shapes.LineShape
import com.rgr.Shapes.RectangleShape
import com.rgr.Shapes.SegmentShape
import com.rgr.Shapes.Shape
import java.io.InputStream
import android.net.Uri
import android.view.ScaleGestureDetector
import com.rgr.utils.FileManager

class Editor @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        @Volatile
        private var instance: Editor? = null

        fun init(editor: Editor): Editor {
            return instance ?: synchronized(this) {
                instance ?: editor.also { instance = it }
            }
        }

        fun getInstance(): Editor {
            return instance ?: throw IllegalStateException("Editor instance is not initialized. Call getInstance(editor) first.")
        }
    }

    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private val matrix = Matrix()
    private var scaleFactor = 1f

    private var currentShape: Shape? = null
    private var currentShapeType: String? = null

    val shapes: MutableList<Shape> = mutableListOf()
    var shapesIndex: Int? = 0

    private val shapeLogger: Logger = Logger(context)

    var updateShapesCallback: ((List<Shape>) -> Unit)? = null

    private val fileManager = FileManager(context)


    // Shape interaction
    fun setCurrentShape(shapeType: String?) {
        currentShapeType = shapeType
        currentShape = when (shapeType) {
            "Крапка" -> DotShape()
            "Лінія" -> LineShape()
            "Пунктирна лінія" -> DashedLineShape()
            "Відрізок" -> SegmentShape()
            "Стрілка" -> ArrowShape()
            "Прямокутник" -> RectangleShape()
            "Еліпс" -> EllipseShape()
            "Ромб" -> DiamondShape()
            "Куб" -> CubeShape()
            "Циліндр" -> CylinderShape()
            else -> null
        }
    }

    // History interaction
    fun highlightShape(index: Int) {
        shapes.forEachIndexed { i, shape ->
            shape.highlighted = (i == index)
        }
        invalidate()
    }

    fun addShape(shape: Shape) {
        shapes.add(shape)
        shapesIndex = shapes.size
        updateShapesCallback?.invoke(shapes)
        invalidate()
    }

    fun removeShapeAt(index: Int) {
        if (index in shapes.indices) {
            shapes.removeAt(index)
            shapesIndex = shapes.size
            updateShapesCallback?.invoke(shapes)
            invalidate()
        }
    }

    // Files interactions
    fun saveShapesToUri(uri: Uri) {
        fileManager.saveShapesToUri(uri, shapes)
    }

    fun saveShapesToDownloads(fileName: String) {
        fileManager.saveShapesToDownloads(fileName, shapes)
    }

    fun loadShapesFromFile(inputStream: InputStream) {
        fileManager.loadShapesFromFile(inputStream, shapes)
        shapesIndex = shapes.size
        updateShapesCallback?.invoke(shapes)
        invalidate()
    }

    private inner class ScaleListener : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.1f, 5.0f)
            matrix.setScale(scaleFactor, scaleFactor)
            invalidate()
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {}
    }


    // Basic functions
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.concat(matrix)

        for (shape in shapes) {
            shape.draw(canvas, shape.highlighted, false)
        }

        currentShape?.draw(canvas, false, true)
        canvas.restore()

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event) // Handle scaling gestures

        // Handle touch events for drawing and moving shapes
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentShape?.setCoordinates(event.x / scaleFactor, event.y / scaleFactor, event.x / scaleFactor, event.y / scaleFactor)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                currentShape?.setCoordinates(currentShape?.startX ?: 0f, currentShape?.startY ?: 0f, event.x / scaleFactor, event.y / scaleFactor)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                currentShape?.setCoordinates(currentShape?.startX ?: 0f, currentShape?.startY ?: 0f, event.x / scaleFactor, event.y / scaleFactor)
                currentShape?.let {
                    addShape(it) // Add the shape to the list
                    shapeLogger.logShape(currentShapeType ?: "Unknown", it) // Log the shape
                }
                setCurrentShape(currentShapeType) // Set the current shape type
                invalidate()
            }
        }
        return true
    }


}
