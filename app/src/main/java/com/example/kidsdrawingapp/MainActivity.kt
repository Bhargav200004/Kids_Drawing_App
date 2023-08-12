package com.example.kidsdrawingapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView : DrawingView?              = null
    private var mImageButtonCurrentPaint : ImageButton? = null

    val openGalleryLauncher : ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if (result.resultCode == RESULT_OK && result.data!=null){
                //selecting image from gallery
                val imageBackground : ImageView = findViewById(R.id.iv_backgraound)

                imageBackground.setImageURI(result.data?.data)
            }
        }

    //permission
    val galleryPermission : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach {
                val permissionName  = it.key
                val isGranted       = it.value

                if (isGranted){
                    Toast.makeText(this@MainActivity,
                        "Permission granted now you can read the storage files",
                        Toast.LENGTH_LONG).show()

                    val pickIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)


                }else
                {
                    if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(this@MainActivity,
                            "Opps! you just denied the permission" ,
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

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

        val ib_gallery : ImageButton = findViewById(R.id.ibtn_gallary)
        ib_gallery.setOnClickListener {

            requestStoragePermission()

        }
    }


    //function for ib gallery
    private fun requestStoragePermission() {
        if (
            ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            ){
            showRationalDialog( "Kids Drawing App",
                            "Drawing app needs permission for External storage")
            }
        else {
            galleryPermission.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                    //TODO - ADD writing external storage on it
                )
            )
        }
    }

    //importing function
    private fun showRationalDialog(
        title   : String,
        message : String
    ){
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){ dialog ,_->
                dialog.dismiss()
            }
        builder.create().show()
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
        if(view !== mImageButtonCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag    = imageButton.tag.toString()
            drawingView?.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_pressedl)
            )

            mImageButtonCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_normal)
            )

            mImageButtonCurrentPaint = view

        }
    }


}