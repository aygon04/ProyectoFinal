package com.example.proyectofinal

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_layout_items.view.*



class ItemsAdapter (private val longItemClickListener:(Int)->Unit):RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvControl=v.txtNameItem
        val tvNombre=v.txtDescripcion
        val tvCarrera=v.txtPrecio
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.activity_layout_items, viewGroup, false)

        return ViewHolder(v)
    }
    override fun getItemCount()=Singleton2.dataSet.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.setOnClickListener{
            // Toast.makeText(viewHolder.itemView.context,dataSet.get(position).nombre,Toast.LENGTH_LONG).show()
            val intent= Intent(viewHolder.itemView.context,MainActivity::class.java)//Checar esto
            intent.putExtra("control",Singleton2.dataSet.get(position).nombre)
            intent.putExtra("nombre",Singleton2.dataSet.get(position).desc)
            intent.putExtra("carrera",Singleton2.dataSet.get(position).Precio)
            viewHolder.itemView.context.startActivity(intent)
        }
        viewHolder.itemView.setOnLongClickListener{
            longItemClickListener.invoke(position)
            true
        }

        viewHolder.tvControl.text = Singleton2.dataSet.get(position).nombre
        viewHolder.tvNombre.text = Singleton2.dataSet.get(position).desc
        viewHolder.tvCarrera.text = Singleton2.dataSet.get(position).Precio.toString()
    }
}