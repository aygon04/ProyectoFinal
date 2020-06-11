package com.example.proyectofinal

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.proyectofinal.data.Personaje
import com.example.proyectofinal.data.PersonajeDatabase
import com.example.proyectofinal.data.PersonajeRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val personajeViewModel:PersonajeViewModel by viewModels{
        PersonajeViewModelFactory(
            PersonajeRepository(PersonajeDatabase.getDatabase(this).pYCDAO()),
        Singleton.nombre
        )
    }


@SuppressLint("RestrictedApi")
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

    /*val intent = Intent(getActivity(this), RecyclerView::class.java)
    startActivity(intent)*/
   //

    mNotificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notifyIntent=Intent(this,AlarmReceiver::class.java)
    val alarmUp=PendingIntent.getBroadcast(this, 0,notifyIntent,PendingIntent.FLAG_NO_CREATE) !=null
    val notifyPendingIntent=PendingIntent.getBroadcast(this, 0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager= getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val repeatInterval=300L
    val triggerTime=SystemClock.elapsedRealtime()+600L

    if( alarmManager!=null)
    {
        Log.e("Entro al alamr","Entro al alarm")
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            repeatInterval,
            notifyPendingIntent
        )
    }
createNotificationChannel()
    //
}
    //
    /**
* Creates a Notification channel, for OREO and higher.
*/
fun createNotificationChannel() {

   // Notification channels are only available in OREO and higher.
   // So, add a check on SDK version.
   if (Build.VERSION.SDK_INT >=
           android.os.Build.VERSION_CODES.O) {

       // Create the NotificationChannel with all the parameters.
      val notificationChannel = NotificationChannel(
       "primary_notification_channel",
                       "Stand up notification",
                       NotificationManager.IMPORTANCE_HIGH);

       notificationChannel.enableLights(true);
       notificationChannel.lightColor=Color.RED
       notificationChannel.enableVibration(true);
       notificationChannel.description=
               ("Notifies every 15 minutes to stand up and walk");
       mNotificationManager.createNotificationChannel(notificationChannel);
   }
}
    //
lateinit var  mNotificationManager:NotificationManager;
override fun onCreateOptionsMenu(menu: Menu): Boolean {
// Inflate the menu; this adds items to the action bar if it is present.
menuInflater.inflate(R.menu.main, menu)
return true
}

override fun onSupportNavigateUp(): Boolean {
val navController = findNavController(R.id.nav_host_fragment)
return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
}
//Settings

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle item selection
    return when (item.itemId) {
       R.id.action_settings ->
       {
           AbrirSettings()
           true
       }
        R.id.popup ->
        {
            showPopup(button2)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}

    private fun AbrirSettings() {
      startActivity(Intent(this,SettingsActivity::class.java))
    }

    //Emergente
open fun showPopup(v: View?): Unit {

    val popup = PopupMenu(this,v)
    val inflater = popup.menuInflater
    inflater.inflate(R.menu.menu, popup.menu)
    popup.setOnMenuItemClickListener { item ->
        when (item.itemId) {
            R.id.dm -> {
               // #006400

               bg.setBackgroundColor(Color.parseColor("#006400"));
                true
            }
            R.id.lm -> {
                bg.setBackgroundColor(Color.parseColor("#ffffff"));
                ConsultarServicioWeb()
                true
            }
            else -> false
        }
    }
    popup.show()
}
//web service
    private fun ConsultarServicioWeb()
    {
    var url= "http://192.168.0.9:8012/test/test.php"
        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->

                for (x in 0..response.length()-1)
                {
                    val jsonObject = response.getJSONObject(x)

                    Singleton2.dataSet.add(Item(jsonObject.getString("Nombre"),jsonObject.getString("Descripcion"),jsonObject.getInt("Precio")))
                }

             //   txtJson.text = "Response: %s".format(response.toString())
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }


}
