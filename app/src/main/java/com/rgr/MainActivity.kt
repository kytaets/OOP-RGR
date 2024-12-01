package com.rgr

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.rgr.adapters.ObjectsListAdapter
import com.rgr.adapters.FileOptionsAdapter


class MainActivity : AppCompatActivity() {

  lateinit var filesBtn: Button
  lateinit var objectsBtn: Button
  lateinit var objectsList: ListView
  lateinit var editorView: Editor

  lateinit var dotBtn: Button
  lateinit var lineBtn: Button
  lateinit var rectBtn: Button
  lateinit var ellipseBtn: Button
  lateinit var cubeBtn: Button
  lateinit var segmentBtn: Button
  private var selectedButton: Button? = null

  lateinit var objectName: String

  lateinit var prevShapeBtn: Button
  lateinit var nextShapeBtn: Button
  lateinit var historyBtn: Button
  private lateinit var shapeHistoryDialog: ShapeHistoryDialog

  lateinit var filesList: ListView
  private lateinit var filePicker: ActivityResultLauncher<String>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_main)
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    // Main buttons
    filesBtn = findViewById(R.id.filesButton)
    objectsBtn = findViewById(R.id.objectsButton)

    // Object buttons
    dotBtn = findViewById(R.id.dot_btn)
    lineBtn = findViewById(R.id.line_btn)
    rectBtn = findViewById(R.id.rect_btn)
    ellipseBtn = findViewById(R.id.ellipse_btn)
    cubeBtn = findViewById(R.id.cube_btn)
    segmentBtn = findViewById(R.id.segment_btn)

    // History buttons
    prevShapeBtn = findViewById(R.id.prevShapeBtn)
    nextShapeBtn = findViewById(R.id.nextShapeBtn)
    historyBtn = findViewById(R.id.history_btn)

    // Lists
    objectsList = findViewById(R.id.objectsList)
    filesList = findViewById(R.id.filesList)
    val buttons = listOf(dotBtn, lineBtn, rectBtn, ellipseBtn, cubeBtn, segmentBtn)
    val objects = resources.getStringArray(R.array.objects)

    // Editor View
    editorView = findViewById(R.id.editorView)
    Editor.init(editorView)

    shapeHistoryDialog = ShapeHistoryDialog(this)

    // Adapters
    val objectListAdapter = ObjectsListAdapter(this, objects)
    val fileAdapter = FileOptionsAdapter(this)
    objectsList.adapter = objectListAdapter
    filesList.adapter = fileAdapter


    // Object buttons listeners
    objectsBtn.setOnClickListener {
      filesList.visibility = View.GONE

      when {
        objectsList.visibility == View.VISIBLE -> objectsList.visibility = View.GONE
        else -> objectsList.visibility = View.VISIBLE
      }
    }

    objectsList.setOnItemClickListener { parent, _, position, _ ->
      val currentEditor = Editor.getInstance()

      objectName = parent.getItemAtPosition(position).toString()

      objectListAdapter.setSelectedPosition(position)

      objectsList.visibility = View.GONE
      currentEditor.visibility = View.VISIBLE

      selectedButton?.setBackgroundColor(Color.parseColor("#6B89FF"))
      selectedButton = buttons.find { it.text == objectName }
      selectedButton?.setBackgroundColor(Color.parseColor("#5067BF"))

      currentEditor.setCurrentShape(objectName)
    }

    for (button in buttons) {
      button.setOnClickListener {
        val currentEditor = Editor.getInstance()

        selectedButton?.setBackgroundColor(Color.parseColor("#6B89FF"))
        button.setBackgroundColor(Color.parseColor("#5067BF"))

        selectedButton = button
        currentEditor.setCurrentShape(button.text.toString())

        objectListAdapter.setSelectedPosition(buttons.indexOf(selectedButton))
      }

      button.setOnLongClickListener {
        Toast.makeText(this, "${button.text}", Toast.LENGTH_SHORT).show()
        true
      }
    }


    // History buttons listeners
    prevShapeBtn.setOnClickListener {
      val currentEditor = Editor.getInstance()
      currentEditor.setShapeIndex(-1)
    }

    nextShapeBtn.setOnClickListener {
      val currentEditor = Editor.getInstance()
      currentEditor.setShapeIndex(+1)
    }

    historyBtn.setOnClickListener{shapeHistoryDialog.toggle()}


    // File picker
    filesBtn.setOnClickListener {
      objectsList.visibility = View.GONE

      when {
        filesList.visibility == View.VISIBLE -> filesList.visibility = View.GONE
        else -> filesList.visibility = View.VISIBLE
      }
    }

    filePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
      uri?.let {
        val inputStream = contentResolver.openInputStream(it)
        val currentEditor = Editor.getInstance()
        inputStream?.let { stream ->
          currentEditor.loadShapesFromFile(stream)
        }
      }
    }

    filesList.setOnItemClickListener { _, _, position, _ ->
      when (position) {
        0 -> {
          val currentEditor = Editor.getInstance()
          val fileName = "shapes_${System.currentTimeMillis()}.txt"
          currentEditor.saveShapesToDownloads(fileName)
        }
        1 -> {
          filePicker.launch("text/*")
        }
      }
      filesList.visibility = View.GONE
      editorView.visibility = View.VISIBLE
    }

  }
}
