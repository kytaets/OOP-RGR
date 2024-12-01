package com.rgr

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.rgr.Shapes.ArrowShape
import com.rgr.Shapes.CubeShape
import com.rgr.Shapes.DotShape
import com.rgr.Shapes.EllipseShape
import com.rgr.Shapes.LineShape
import com.rgr.Shapes.RectangleShape
import com.rgr.Shapes.SegmentShape
import com.rgr.Shapes.Shape
import com.rgr.adapters.ShapeSerializer
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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

    private var currentShape: Shape? = null
    private var currentShapeType: String = "Прямокутник"

    val shapes: MutableList<Shape> = mutableListOf()
    var shapesIndex: Int? = 0

    private val shapeLogger: Logger = Logger(context)

    var updateShapesCallback: ((List<Shape>) -> Unit)? = null


    // Shape interaction
    fun setCurrentShape(shapeType: String) {
        currentShapeType = shapeType
        currentShape = when (shapeType) {
            "Крапка" -> DotShape()
            "Лінія" -> LineShape()
            "Пунктирна лінія" -> LineShape()
            "Відрізок" -> SegmentShape()
            "Стрілка" -> ArrowShape()
            "Прямокутник" -> RectangleShape()
            "Еліпс" -> EllipseShape()
            "Ромб" -> RectangleShape()
            "Куб" -> CubeShape()
            "Циліндр" -> CubeShape()
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


    // File picker interaction
    fun saveShapesToDownloads(fileName: String) {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            val outputStream = FileOutputStream(file)
            outputStream.bufferedWriter().use { writer ->
                shapes.forEach { shape ->
                    val serializedShape = ShapeSerializer().serialize(shape)
                    writer.write("$serializedShape\n")
                }
            }
            Toast.makeText(context, "Файл збережено у 'Завантаження': ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("Editor", "Помилка збереження файлу в Завантаження", e)
            Toast.makeText(context, "Помилка збереження файлу.", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadShapesFromFile(inputStream: InputStream) {
        val shapeSerializer = ShapeSerializer()

        try {
            inputStream.bufferedReader().use { reader ->
                val fileContent = reader.readText()
                shapes.clear()

                fileContent.lines().filter { it.isNotBlank() }.forEach { line ->
                    try {
                        val shape = shapeSerializer.deserialize(line)
                        shapes.add(shape)
                    } catch (e: Exception) {
                        Log.e("ShapeSerializer", "Error deserializing shape: $line", e)
                    }
                }

                shapesIndex = shapes.size
                updateShapesCallback?.invoke(shapes)
                invalidate()
            }
        } catch (e: Exception) {
            Log.e("Editor", "Error loading shapes from file", e)
            Toast.makeText(context, "Не вдалося завантажити файл. Перевірте його формат.", Toast.LENGTH_SHORT).show()
        }
    }


    // Basic functions
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (shape in shapes) {
            shape.draw(canvas, shape.highlighted, false)
        }


        currentShape?.draw(canvas, false, true)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                currentShape?.setCoordinates(event.x, event.y, event.x, event.y)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                currentShape?.setCoordinates(currentShape?.startX ?: 0f, currentShape?.startY ?: 0f, event.x, event.y)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                currentShape?.setCoordinates(currentShape?.startX ?: 0f, currentShape?.startY ?: 0f, event.x, event.y)
                currentShape?.let {
                    addShape(it)

                    shapeLogger.logShape(currentShapeType, it)
                }
                setCurrentShape(currentShapeType)
                invalidate()
            }
        }
        return true
    }
}
