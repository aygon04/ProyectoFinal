package com.example.proyectofinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.data.PersonajeRepository

class PersonajeViewModelFactory(private val personajeRepository: PersonajeRepository,private val nombre:String) :
    ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PersonajeViewModel(personajeRepository,nombre) as T
    }
}