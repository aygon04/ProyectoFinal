package com.example.proyectofinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.Personaje
import com.example.proyectofinal.data.PersonajeRepository
import kotlinx.coroutines.launch

class PersonajeViewModel (val personajeRepository: PersonajeRepository, val nombre: String):ViewModel()
{
    val personaje= personajeRepository.obtenerDatosPersonaje(nombre)

    fun guardarDatosPersonaje(personaje:Personaje){
        viewModelScope.launch { personajeRepository.guardarDatosPersonaje(personaje) }
    }
}