package com.rgr.adapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FileOptionsAdapter(context: Context) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1) {

  private val fileOptions = arrayOf("Зберегти файл", "Зберегти файл як...", "Завантажити файл")

  init {
    addAll(*fileOptions)
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view = super.getView(position, convertView, parent)

    val textView = view.findViewById<TextView>(android.R.id.text1)
    textView.textSize = 18f
    textView.setTextColor(Color.BLACK)

    return view
  }
}

