package com.example.proyectofinal

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.view.Menu
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.example.proyectofinal.data.Personaje
import com.example.proyectofinal.data.PersonajeDatabase
import com.example.proyectofinal.data.PersonajeRepository
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val personajeViewModel:PersonajeViewModel by viewModels{
        PersonajeViewModelFactory(
            PersonajeRepository(PersonajeDatabase.getDatabase(this).pYCDAO()),
        Singleton.nombre
        )
    }


override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContentView(R.layout.activity_main)
val toolbar: Toolbar = findViewById(R.id.toolbar)
setSupportActionBar(toolbar)

val personaje= Personaje(0,"Aygon",0,100,1,1,1,1,1,1)
personajeViewModel.guardarDatosPersonaje(personaje)





val fab: FloatingActionButton = findViewById(R.id.fab)
fab.setOnClickListener { view ->

  val personaje= Personaje(1,"Aygon",0,100,1,1,1,1,1,1)
  personajeViewModel.guardarDatosPersonaje(personaje)
  Snackbar.make(view, "Se creo un personaje", Snackbar.LENGTH_LONG)
      .setAction("Action", null).show()
    var filename="archivoPersonaje0"
    var filecontents="Aygon"
    openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(filecontents.toByteArray())
    }
    //

    val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

//     Creates a file with this name, or replaces an existing file
//     that has the same name. Note that the file name cannot contain
//     path separators.

    val fileToWrite = "my_sensitive_data.txt"
    try {
        this.deleteFile(fileToWrite)
    }
    catch (e: Exception)
    {}

    val encryptedFile = EncryptedFile.Builder(
        File(filesDir, fileToWrite),
        this,
        masterKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()
          encryptedFile.openFileOutput().bufferedWriter().use { writer ->
                writer.write("Aygon")
            }
    //
    //Leer

    val fileToRead = "my_sensitive_data.txt"
    lateinit var byteStream: ByteArrayOutputStream
    val encryptedFile2 = EncryptedFile.Builder(
        File(filesDir, fileToRead),
        this,
        masterKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    val contents = encryptedFile2.openFileInput().bufferedReader().useLines { lines ->
        lines.fold("") { working, line ->
            "$working\n$line"
        }
    }
    Snackbar.make(view, "Contenido: $contents!", Snackbar.LENGTH_LONG).show()
    Singleton.nombre=contents

    //
}
val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
val navView: NavigationView = findViewById(R.id.nav_view)
val navController = findNavController(R.id.nav_host_fragment)
// Passing each menu ID as a set of Ids because each
// menu should be considered as top level destinations.
appBarConfiguration = AppBarConfiguration(setOf(
      R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
setupActionBarWithNavController(navController, appBarConfiguration)
navView.setupWithNavController(navController)
}

override fun onCreateOptionsMenu(menu: Menu): Boolean {
// Inflate the menu; this adds items to the action bar if it is present.
menuInflater.inflate(R.menu.main, menu)
return true
}

override fun onSupportNavigateUp(): Boolean {
val navController = findNavController(R.id.nav_host_fragment)
return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
}
}
