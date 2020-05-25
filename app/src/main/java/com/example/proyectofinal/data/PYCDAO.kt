package com.example.proyectofinal.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PersonajeDAO
{
    //Insertar en la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarDatosPersonaje(personaje: Personaje)

    @Query("SELECT * FROM personaje WHERE nombre = :nombre")
    fun obtenerDatosPersonaje(nombre: String): LiveData<Personaje>
}