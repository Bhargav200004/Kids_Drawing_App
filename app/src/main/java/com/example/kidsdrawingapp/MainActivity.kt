package com.example.kidsdrawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView : DrawingView?              = null
    private var mImageButtonCurrentPaint : ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView     =findViewById(R.id.drawing_view)
        drawingView?.setSizeOfBrush(20.toFloat())

        //finding the is of linear layout id
        val linearLayoutPaintColor  = findViewById<LinearLayout>(R.id.ll_paint_colors)

        //to select the color of linear layout      ----------->using as array
        mImageButtonCurrentPaint    = linearLayoutPaintColor[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this,R.drawable.pallet_pressedl)
        )


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

        val smallBtn : ImageButton  = brushDialog.findViewById(R.id.ibtn_small_brush)
        smallBtn.setOnClickListener{
            drawingView?.setSizeOfBrush(10.toFloat())
            brushDialog.dismiss()
        }
        val meadiumBtn : ImageButton  = brushDialog.findViewById(R.id.ibtn_medium_brush)
        meadiumBtn.setOnClickListener{
            drawingView?.setSizeOfBrush(20.toFloat())
            brushDialog.dismiss()
        }
        val largeBtn : ImageButton  = brushDialog.findViewById(R.id.ibtn_large_brush)
        largeBtn.setOnClickListener{
            drawingView?.setSizeOfBrush(30.toFloat())
            brushDialog.dismiss()
        }


        brushDialog.show()
    }

    fun paintClicked(view: View){
        Toast.makeText(this, "clicked paint", Toast.LENGTH_SHORT).show()
    }


}