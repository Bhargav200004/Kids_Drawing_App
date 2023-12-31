package com.example.kidsdrawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View



class DrawingView(context : Context, attrs : AttributeSet) : View(context,attrs)
{

    private var mDrawPath           : CustomPath? = null
    private var mCanvasBitmap       : Bitmap? = null
    private var mDrawPaint          : Paint? = null                     //------> Holds the color , style , information about Geometry
    private var mCanvasPaint        : Paint? = null
    private var mBrushSize          : Float = 0.toFloat()               //------> Define the value as 0
    private var color           = Color.BLACK
    private var canvas : Canvas?    = null
    private val mPaths              = ArrayList<CustomPath>()
    private val mUndos              = ArrayList<CustomPath>()

    init
    {
            setUpDrawing()
    }

    fun onClickUndo(){
        if (mPaths.size > 0){
            mUndos.add(mPaths.removeAt(mPaths.size-1))
            invalidate()
        }
    }

    private fun setUpDrawing()
    {
            mDrawPaint               = Paint()
            mDrawPath                = CustomPath(color,mBrushSize)     //---------->Parameter cannot be empty
            mDrawPaint!!.color       = color
            mDrawPaint!!.style       = Paint.Style.STROKE
            mDrawPaint!!.strokeJoin  = Paint.Join.ROUND
            mDrawPaint!!.strokeCap   = Paint.Cap.ROUND
            mCanvasPaint             = Paint(Paint.DITHER_FLAG)         //---------->DITHER means shaking
            //mBrushSize               = 20.toFloat()                     //---------->change the float value to 20
    }

    //for view changing in the phone
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)    //----------->w(width),h(height),oldW(oldWidth),oldH(OldHeight)
    {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap   = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas          = Canvas(mCanvasBitmap!!)

    }

    //In these draw is draw
    override fun onDraw(canvas: Canvas)                                 //--------------->If Canvas to Canvas? if fails
    {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        //for saving data of strokes created by Brush
        for (paths in mPaths)
        {
            mDrawPaint!!.strokeWidth    = paths.brushThickness
            mDrawPaint!!.color          = paths.color
            canvas.drawPath(paths , mDrawPaint!!)
        }

        if (!mDrawPath!!.isEmpty)
        {
            mDrawPaint!!.strokeWidth    = mDrawPath!!.brushThickness
            mDrawPaint!!.color          = mDrawPath!!.color
            canvas.drawPath(mDrawPath!! , mDrawPaint!!)
        }
    }

    //On touch function to  draw on the screen
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when(event?.action)
        {
            MotionEvent.ACTION_DOWN ->
            {
                mDrawPath!!.color           = color
                mDrawPath!!.brushThickness  = mBrushSize

                mDrawPath!!.reset()
                mDrawPath!!.moveTo(touchX!!,touchY!!)
            }

            MotionEvent.ACTION_MOVE ->
            {
                mDrawPath!!.lineTo(touchX!!,touchY!!)
            }

            MotionEvent.ACTION_UP ->
            {
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color,mBrushSize)

            }

            else -> return false
        }

        invalidate()
        return true

    }

    fun setSizeOfBrush(newSize : Float)
    {
        mBrushSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP ,
            newSize ,                                               //------------> change the thickness of brush accordingly to screen
            resources.displayMetrics
        )
        mDrawPaint!!.strokeWidth = mBrushSize
    }


    fun setColor(newColor : String){
        color               = Color.parseColor(newColor)
        mDrawPaint!!.color  = color
    }


    //nested class ----->Importing drawing screen by Graphic class
    internal inner class CustomPath(var color : Int , var brushThickness : Float) : Path()
    {

    }

}