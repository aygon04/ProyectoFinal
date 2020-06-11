package com.example.proyectofinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity(),ComprarDialogFragment.EliminarAlumnoDialogListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        LoadData()

        RV.layoutManager= LinearLayoutManager(this)
        RV.adapter=ItemsAdapter(onLongItemClickListener)
    }
    private fun LoadData()
    {
        for(i in 0..20)
        {
          //  Singleton2.dataSet.add(Item("Item ${i}",
          //      "Item num ${i.toString().padStart(2,'0')} ",50))
        }
    }
    val onLongItemClickListener: (Int) -> Unit = {position ->
        // Toast.makeText (this,"Eliminar a ${Singleton.dataSet.get(position).control}",Toast.LENGTH_LONG).show()

        DialogEliminarAlumno(position)
    }
    private fun DialogEliminarAlumno(position: Int) {
        val newFragment = ComprarDialogFragment(position)
        newFragment.show(supportFragmentManager, "EliminarAlumnoDialogFragment")
    }

    override fun onDialogPositiveClick(position: Int) {


        //recyclerView.adapter?.notifyItemRemoved(position)
        //Se compro
       Singleton3.dataSet.add(Singleton2.dataSet.get(position))
        Toast.makeText (this,"Se compro algo${Singleton2.dataSet.get(position).nombre}", Toast.LENGTH_LONG).show()
    }

    override fun onDialogNegativeClick(position: Int) {
        Toast.makeText (this,"No se realizo la compra", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        RV.adapter?.notifyDataSetChanged()
    }

}
