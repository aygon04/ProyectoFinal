@file:Suppress("DEPRECATION")

package com.example.proyectofinal.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.provider.CalendarContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.preference.PreferenceManager
import com.example.proyectofinal.R
import com.example.proyectofinal.Singleton
import com.example.proyectofinal.Singleton3
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_calendar.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Error
import java.util.*


class Calendar : AppCompatActivity() {
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
// Initialize a list of required permissions to request runtime
        val list = listOf<String>(

            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_CALENDAR
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this,list,PermissionsRequestCode)
        var cosas=""
        for(x in 0 .. Singleton3.dataSet.size-1)
        {
            cosas=cosas+Singleton3.dataSet.get(x).nombre

        }
        tv1.text=cosas
        btnAtacar.setOnClickListener()
        {
            toast("${Singleton.nombre}Ataca!")
        }
        button.setOnClickListener()
        {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val name = sharedPreferences.getString("reply","nada!")
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Mi personaje ${Singleton.nombre} es un ${name}")
                type = "text/plain"
            }
            // Verify that the intent will resolve to an activity
            if (sendIntent.resolveActivity(packageManager) != null) {
                startActivity(sendIntent)
            }
        }
        // Button to check permissions states
    }
        //
        @SuppressLint("MissingPermission")
        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                                grantResults: IntArray) {

            when (requestCode) {
                PermissionsRequestCode ->{
                    val isPermissionsGranted = managePermissions.processPermissionsResult(requestCode,permissions,grantResults)
                    if(isPermissionsGranted){
                        // Do the task now
                        toast("Permissions granted.")
                        val tv = tv1

                        val cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI, null, null, null, null)
                       // toast("Entro")
                        while (cursor!!.moveToNext()) {
                            if (cursor != null) {
                                toast("Entrox2")
                                val id =
                                    cursor.getColumnName(cursor.getColumnIndex(CalendarContract.Events._ID))
                                val title =
                                    cursor.getColumnName(cursor.getColumnIndex(CalendarContract.Events.TITLE))
                                val description =
                                    cursor.getColumnName(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION))
                               toast("ID:$id Titulo:$title Descripcion: $description")

                                tv.text = "ID:$id Titulo:$title Descripcion: $description"
                                  //  .makeText(this,"ID:$id Titulo:$title Descripcion: $description",Toast.LENGTH_SHORT)
                            }
                        }
                    }else{
                        toast("Permissions denied.")
                    }
                    return
                }
            }
        }
        //
    fun Read(view: View)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            managePermissions.checkPermissions()
        }

    }
    fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }

    fun Write(view:View) {

        val uri2 = saveImageToExternalStorage(takeScreenshotOfView(view.rootView, 2160, 1080))
        val tv = tv1
        tv.text = uri2.toString()
        MediaScannerConnection.scanFile(this,  arrayOf(uri2.toString()) , arrayOf( "image/jpeg" ), null)
        intent=Intent(ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.setData(uri2);
        sendBroadcast(intent);


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
//
        //
        // Get the image from drawable resource as drawable object
        // Save image to gallery
        val savedImageURL = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            file.name,
            "Image of ${file.name}"
        )

        //
        //
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
