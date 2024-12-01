package com.rgr.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ObjectsListAdapter(context: Context, private val items: Array<String>) :
  ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items) {

  private var selectedPosition: Int = -1

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
    val textView = view.findViewById<TextView>(android.R.id.text1)
    textView.text = items[position]
    textView.setTextColor(Color.BLACK)

    if (position == selectedPosition) {
      view.setBackgroundColor(Color.GRAY)
    } else {
      view.setBackgroundColor(Color.TRANSPARENT)
    }
    return view
  }

  fun setSelectedPosition(position: Int) {
    selectedPosition = position
    notifyDataSetChanged()
  }
}
