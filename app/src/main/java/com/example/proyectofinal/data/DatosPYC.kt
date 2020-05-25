package com.example.proyectofinal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Personaje")
data class Personaje (
    val Nivel: Int,
    @PrimaryKey val Nombre: String,
    val Exp: Int,
    val NLExp: Int,
    val Dist: Int,
    val xpDist: Int,
    val Magia: Int,
    val xpMagia: Int,
    val Cuerpo: Int,
    val xpCuerpo: Int
    ){

}
@Entity(tableName = "Clase")
data class Clase (
    val mDist: Int,
    val mCuerpo: Int,
    val mMagia: Int,
    val AumentoHP: Int,
    val AumentoMana: Int,
   @PrimaryKey val Nombre: String
    ){

}