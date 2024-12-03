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
import android.net.Uri
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import com.rgr.adapters.FileOptionsAdapter


class MainActivity : AppCompatActivity() {

  lateinit var filesBtn: Button
  lateinit var editorView: Editor

  lateinit var dotBtn: ImageButton
  lateinit var lineBtn: ImageButton
  lateinit var dashedLineBtn: ImageButton
  lateinit var segmentBtn: ImageButton
  lateinit var arrowBtn: ImageButton
  lateinit var rectBtn: ImageButton
  lateinit var ellipseBtn: ImageButton
  lateinit var diamondBtn: ImageButton
  lateinit var cubeBtn: ImageButton
  lateinit var cylinderBtn: ImageButton


  private var selectedButton: ImageButton? = null

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

    // Shape buttons

    dotBtn = findViewById(R.id.dot_btn)
    lineBtn = findViewById(R.id.line_btn)
    dashedLineBtn = findViewById(R.id.dashed_line_btn)
    segmentBtn = findViewById(R.id.segment_btn)
    arrowBtn = findViewById(R.id.arrow_btn)
    rectBtn = findViewById(R.id.rect_btn)
    ellipseBtn = findViewById(R.id.ellipse_btn)
    diamondBtn = findViewById(R.id.diamond_btn)
    cubeBtn = findViewById(R.id.cube_btn)
    cylinderBtn = findViewById(R.id.cylinder_btn)

    // History buttons
    historyBtn = findViewById(R.id.history_btn)

    // Lists
    filesList = findViewById(R.id.filesList)
    val buttons = listOf(dotBtn, lineBtn, dashedLineBtn, segmentBtn, arrowBtn, rectBtn, ellipseBtn, diamondBtn, cubeBtn, cylinderBtn)

    // Editor View
    editorView = findViewById(R.id.editorView)
    Editor.init(editorView)

    shapeHistoryDialog = ShapeHistoryDialog(this)

    // Adapters
    val fileAdapter = FileOptionsAdapter(this)
    filesList.adapter = fileAdapter


    for (button in buttons) {
      button.setOnClickListener {
        val currentEditor = Editor.getInstance()

        // Check if the current button is already selected
        if (button == selectedButton) {
          // If it's the same button, clear the selection and reset currentShape to null
          selectedButton?.setBackgroundColor(Color.parseColor("#6B89FF")) // Reset previous selection
          selectedButton = null
          currentEditor.setCurrentShape(null) // Set currentShape to null
        } else {
          // If it's a different button, set the current shape
          selectedButton?.setBackgroundColor(Color.parseColor("#6B89FF"))
          button.setBackgroundColor(Color.parseColor("#5067BF"))

          selectedButton = button
          currentEditor.setCurrentShape(button.contentDescription.toString())
        }
      }

      button.setOnLongClickListener {
        Toast.makeText(this, "${button.contentDescription}", Toast.LENGTH_SHORT).show()
        true
      }
    }


    // History
    historyBtn.setOnClickListener{shapeHistoryDialog.toggle()}

    val saveAsLauncher = registerForActivityResult(
      ActivityResultContracts.CreateDocument("text/plain")
    ) { uri: Uri? ->
      uri?.let {
        val currentEditor = Editor.getInstance()
        currentEditor.saveShapesToUri(it)
      }
    }



    // File picker
    filesBtn.setOnClickListener {
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
          val fileName = "shapes_${System.currentTimeMillis()}.txt"
          saveAsLauncher.launch(fileName)
        }
        2 -> {
          filePicker.launch("text/*")
        }
      }
      filesList.visibility = View.GONE
      editorView.visibility = View.VISIBLE
    }

  }
}
