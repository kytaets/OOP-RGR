package com.rgr.adapters

import java.util.Locale
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.rgr.MainActivity
import com.rgr.R
import com.rgr.Shapes.Shape

class ShapeHistoryAdapter(
  private val context: Context,
  private var shapes: List<Shape>
) : BaseAdapter() {

  private var selectedPosition: Int = -1
  private val sharedPreferences = context.getSharedPreferences("ShapePrefs", Context.MODE_PRIVATE)

  init {
    selectedPosition = -1
  }

  private fun saveSelectedPosition() {
    sharedPreferences.edit()
      .putInt("SELECTED_POSITION", selectedPosition)
      .apply()
  }

  fun updateShapes(newShapes: List<Shape>) {
    shapes = newShapes
    notifyDataSetChanged()
  }

  override fun getCount(): Int = shapes.size

  override fun getItem(position: Int): Any = shapes[position]

  override fun getItemId(position: Int): Long = position.toLong()

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)

    val shapeName = view.findViewById<TextView>(R.id.shapeName)
    val x1 = view.findViewById<TextView>(R.id.x1)
    val y1 = view.findViewById<TextView>(R.id.y1)
    val x2 = view.findViewById<TextView>(R.id.x2)
    val y2 = view.findViewById<TextView>(R.id.y2)

    val shape = shapes[position]

    shapeName.text = shape.name

    x1.text = String.format(Locale.US, "%.1f", shape.startX)
    y1.text = String.format(Locale.US,"%.1f", shape.startY)
    x2.text = String.format(Locale.US,"%.1f", shape.endX)
    y2.text = String.format(Locale.US,"%.1f", shape.endY)

    view.setBackgroundColor(
      when {
        position == selectedPosition -> Color.LTGRAY
        else -> Color.WHITE
      }
    )

    view.setOnClickListener {
      if (selectedPosition != position) {
        selectedPosition = position
        saveSelectedPosition()
        notifyDataSetChanged()
        (context as MainActivity).editorView.highlightShape(position)
      }
    }

    view.setOnLongClickListener {
      (context as MainActivity).editorView.removeShapeAt(position)
      if (position == selectedPosition) {
        selectedPosition = -1
        saveSelectedPosition()
      }
      notifyDataSetChanged()
      true
    }

    return view
  }
}

