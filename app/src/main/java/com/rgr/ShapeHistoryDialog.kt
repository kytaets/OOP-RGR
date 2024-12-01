package com.rgr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ListView
import com.rgr.adapters.ShapeHistoryAdapter

class ShapeHistoryDialog(private val context: Context) {

  private var isVisible = false
  private var historyView: View? = null
  private var adapter: ShapeHistoryAdapter? = null

  fun toggle() {
    if (historyView == null) {
      historyView = LayoutInflater.from(context).inflate(R.layout.history_list, null)

      val editor = Editor.getInstance()
      val listView: ListView = historyView!!.findViewById(R.id.listView)

      adapter = ShapeHistoryAdapter(context, editor.shapes)
      listView.adapter = adapter

      listView.setOnItemClickListener { _, _, position, _ ->
        editor.highlightShape(position)
      }

      editor.updateShapesCallback = { newShapes ->
        adapter?.updateShapes(newShapes)
      }

      (context as MainActivity).findViewById<FrameLayout>(R.id.frameLayout).addView(historyView)
    }

    historyView?.visibility = if (isVisible) View.GONE else View.VISIBLE
    isVisible = !isVisible
  }
}
