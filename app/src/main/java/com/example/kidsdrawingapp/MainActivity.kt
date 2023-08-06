package com.example.kidsdrawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private var drawingView : DrawingView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView     =findViewById(R.id.drawing_view)
        drawingView?.setSizeOfBrush(20.toFloat())

        val ib_brush : ImageButton = findViewById(R.id.ibtn_brush)
        ib_brush.setOnClickListener{
            showBrushSizeChooserDialog()
        }
    }

    private fun showBrushSizeChooserDialog()
    {
       val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dailog_brush_size)
        brushDialog.setTitle("Brush size : ")
        val smallBtn : ImageButton  =brushDialog.findViewById(R.id.ibtn_small_brush)
        smallBtn.setOnClickListener{
            drawingView?.setSizeOfBrush(10.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }
}