package com.example.proyectofinal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Personaje::class), version = 1,exportSchema = false)
abstract  class PersonajeDatabase: RoomDatabase()
{
    abstract fun pYCDAO(): PersonajeDAO
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PersonajeDatabase? = null

        fun getDatabase(context: Context): PersonajeDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PersonajeDatabase::class.java,
                    "Personaje_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}