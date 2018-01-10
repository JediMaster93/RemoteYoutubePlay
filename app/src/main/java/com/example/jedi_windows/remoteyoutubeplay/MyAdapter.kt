package com.example.jedi_windows.remoteyoutubeplay

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.jedi_windows.remoteyoutubeplay.MyAdapter.ViewHolder

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.squareup.picasso.Picasso

/**
 * Created by Jedi-Windows on 07.01.2018..
 */

class MyAdapter(private val dataSet: MutableList<YoutubeApiVideo>) : RecyclerView.Adapter<MyAdapter.ViewHolder>(),ValueChangeListener {
    override fun onValueChanged(newValue: String) {
        Log.d("log","data set changed")
        this.notifyDataSetChanged()
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var cardview: CardView

        init {
            this.cardview = v.findViewById(R.id.card_view)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view, parent, false) as View

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardview.setOnClickListener {
            Fuel.get(dataSet[position].youtubeURL).responseString(object : Handler<String> {
                override fun success(request: Request, response: Response, s: String) {

                }

                override fun failure(request: Request, response: Response, fuelError: FuelError) {

                }
            })
        }
        val tv = holder.cardview.findViewById<TextView>(R.id.my_text_view)
        tv.text = this.dataSet[position].title

        val imgView = holder.cardview.findViewById<ImageView>(R.id.thumbnail)
        Log.d("log", "url to load is ${dataSet[position].thumbnailURL} for item in position $position")
        if (dataSet[position].thumbnailURL !== ""){
            Picasso.with(holder.cardview.context).load(dataSet[position].thumbnailURL).into(imgView)
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


}
