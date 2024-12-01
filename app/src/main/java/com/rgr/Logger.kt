package com.rgr

import android.content.Context
import com.rgr.Shapes.Shape
import java.io.File
import java.io.FileWriter
import java.io.IOException

class Logger (context: Context){

  private val file: File = File(context.filesDir, "shapes_log.txt")

  init {
    try {
      file.writeText("")
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  fun logShape(shapeType: String, shape: Shape) {
    try {
      FileWriter(file, true).use { writer ->
        val logEntry = "${shapeType}: (${shape.startX}, ${shape.startY}) -> (${shape.endX}, ${shape.endY})\n"
        writer.append(logEntry)
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

}