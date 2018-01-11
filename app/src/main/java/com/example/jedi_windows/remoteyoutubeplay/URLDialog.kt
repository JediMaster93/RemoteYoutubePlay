package com.example.jedi_windows.remoteyoutubeplay

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText

/**
 * Created by Jedi-Windows on 10.01.2018..
 */
class URLDialog : DialogFragment(){
    interface urlDialogListener{
        fun onPositiveClick( dialog: URLDialog)
        fun onNegativeClick( dialog: URLDialog)
    }
    lateinit var listener : urlDialogListener
    lateinit var urlInput : EditText
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater : LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.dialog_url,null))
         /*       .setPositiveButton("Add",DialogInterface.OnClickListener({x,y->

        }))
                .setNegativeButton("Cancel",DialogInterface.OnClickListener({x,y->

        }))*/
        builder.setPositiveButton("Add", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                listener?.onPositiveClick(this@URLDialog)
             }
        })
        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                listener?.onNegativeClick(this@URLDialog)
            }
        })
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        urlInput = dialog.findViewById(R.id.url_input)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is urlDialogListener ){
            listener = context
        }
    }
}