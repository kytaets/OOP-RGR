package com.rgr.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.rgr.adapters.ShapeSerializer
import com.rgr.Shapes.Shape
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FileManager(private val context: Context) {

  fun saveShapesToUri(uri: Uri, shapes: List<Shape>) {
    try {
      context.contentResolver.openOutputStream(uri)?.use { outputStream ->
        outputStream.bufferedWriter().use { writer ->
          shapes.forEach { shape ->
            val serializedShape = ShapeSerializer().serialize(shape)
            writer.write("$serializedShape\n")
          }
        }
      }
      Toast.makeText(context, "Файл збережено: $uri", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
      Log.e("FileManager", "Помилка збереження файлу", e)
      Toast.makeText(context, "Помилка збереження файлу.", Toast.LENGTH_SHORT).show()
    }
  }

  fun saveShapesToDownloads(fileName: String, shapes: List<Shape>) {
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
      Log.e("FileManager", "Помилка збереження файлу в Завантаження", e)
      Toast.makeText(context, "Помилка збереження файлу.", Toast.LENGTH_SHORT).show()
    }
  }

  fun loadShapesFromFile(inputStream: InputStream, shapes: MutableList<Shape>) {
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
      }
      Toast.makeText(context, "Файл завантажено успішно", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
      Log.e("FileManager", "Error loading shapes from file", e)
      Toast.makeText(context, "Не вдалося завантажити файл. Перевірте його формат.", Toast.LENGTH_SHORT).show()
    }
  }
}
