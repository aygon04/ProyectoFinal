package com.example.proyectofinal.data

class PersonajeRepository(val personajeDAO: PersonajeDAO)
{
    suspend fun guardarDatosPersonaje(personaje:Personaje)= personajeDAO.guardarDatosPersonaje(personaje)
     fun obtenerDatosPersonaje(nombre:String)= personajeDAO.obtenerDatosPersonaje(nombre)
}