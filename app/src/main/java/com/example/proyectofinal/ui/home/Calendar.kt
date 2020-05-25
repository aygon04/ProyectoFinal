package com.example.proyectofinal.ui.home

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.proyectofinal.R
import kotlinx.android.synthetic.main.activity_calendar.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Error
import java.util.*


class Calendar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener()
        { view ->

            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_CONTACTS,android.Manifest.permission.WRITE_CALENDAR),10)
                Toast.makeText(
                    this,
                    "nopermisos",
                    Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            val cursor =
                contentResolver.query(CalendarContract.Events.CONTENT_URI, null, null, null, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id =
                        cursor.getColumnName(cursor.getColumnIndex(CalendarContract.Events._ID))
                    val title =
                        cursor.getColumnName(cursor.getColumnIndex(CalendarContract.Events.TITLE))
                    val description =
                        cursor.getColumnName(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION))
                    Toast.makeText(
                        this,
                        "ID:$id Titulo:$title Descripcion: $description",
                        Toast.LENGTH_SHORT
                    )
                }
            } else {
                print("WRONG")
                Toast.makeText(
                    this,
                    "asdasd",
                    Toast.LENGTH_SHORT)
            }
        }



    }

    fun Read(view: View)
    {

    }
    fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }

    fun Write(view:View) {
       // val uri = bitmapToFile(takeScreenshotOfView(view,100,110))
        val uri2=saveImageToExternalStorage(takeScreenshotOfView(view,900,500))
        //image_view_file.setImageURI(uri)
        val tv=tv1
      //  tv.text=uri.toString()
        tv.text=uri2.toString()
    }
    fun takeScreenshotOfView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }
    fun getPrivateAlbumStorageDir(context: Context, albumName: String): File? {
        // Get the directory for the app's private pictures directory.
        val file = File(context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES), albumName)
        if (!file?.mkdirs()) {
            Log.e("Error", "Directory not created")
        }
        return file
    }
    private fun saveImageToExternalStorage(bitmap:Bitmap):Uri{
        // Get the external storage directory path
        val path = getExternalFilesDir("Images").toString()
       // path = this.getExternalFilesDir(null)?.getAbsolutePath().toString();

        // Create a file to save the image
        val file = File(path, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the output stream
            stream.flush()

            // Close the output stream
            stream.close()
            toast("Image saved successful.")
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
            toast("Error to save image.")
        }

        // Return the saved image path to uri
        return Uri.parse(file.absolutePath)
    }

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
    private fun bitmapToFile(bitmap:Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(this)

        // Initialize a new file instance to save bitmap object
       var file = wrapper.getDir("Images",Context.MODE_PRIVATE)



        file = File(file,"${UUID.randomUUID()}.jpg")

        try{
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }
}
