package com.example.jedi_windows.remoteyoutubeplay

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater

/**
 * Created by Jedi-Windows on 10.01.2018..
 */
class URLDialog : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater : LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.dialog_url,null))
                .setPositiveButton("Add",DialogInterface.OnClickListener({x,y->

        }))
                .setNegativeButton("Cancel",DialogInterface.OnClickListener({x,y->

        }))



        return builder.create()
    }
}