package com.example.proyectofinal.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.*
import com.example.proyectofinal.data.PersonajeDatabase
import com.example.proyectofinal.data.PersonajeRepository
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private val personajeViewModel: PersonajeViewModel by viewModels{
        PersonajeViewModelFactory(
            PersonajeRepository(PersonajeDatabase.getDatabase(requireContext()).pYCDAO()),
            Singleton.nombre
        )
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val boton=root.findViewById<Button>(R.id.button2)
        val boton2=root.findViewById<Button>(R.id.btnCompra)
        boton.setOnClickListener()
        {
            view ->
            val intent = Intent(this.context, Calendar::class.java).apply {

            }
            startActivity(intent)
        }
        boton2.setOnClickListener()
        {
                view ->
            val intent = Intent(this.context, Main2Activity::class.java).apply {

            }
            startActivity(intent)
        }
        val tvNombre=root.findViewById<TextView>(R.id.tvNombre)
        val tvNivel=root.findViewById<TextView>(R.id.tvNivel)
        personajeViewModel.personaje.observe(viewLifecycleOwner, Observer {
            datos_personaje->
           tvNombre.text=datos_personaje.Nombre
           tvNivel.text=datos_personaje.Nivel.toString()
        })
        return root
    }
}
