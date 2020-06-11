package com.example.proyectofinal

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class ComprarDialogFragment(private val position:Int) : DialogFragment() {
    internal lateinit var listener: EliminarAlumnoDialogListener
    interface EliminarAlumnoDialogListener {
        fun onDialogPositiveClick(position: Int)
        fun onDialogNegativeClick(position: Int)
    }
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as EliminarAlumnoDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Comprar ${Singleton2.dataSet.get(position).nombre}?")
            builder.setMessage("Deseas comprar ${Singleton2.dataSet.get(position).nombre} con un precio de" +
                    " ${Singleton2.dataSet.get(position).Precio} C/U")
                .setPositiveButton("Si",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogPositiveClick(position)
                    })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        listener.onDialogNegativeClick(position)
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}