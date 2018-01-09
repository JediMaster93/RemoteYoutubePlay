package com.example.jedi_windows.remoteyoutubeplay

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SeekBar
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/* com.example.jedi_windows.remoteyoutubeplay  F3:D3:D4:78:C0:6C:00:89:AF:62:A3:2A:F0:DE:36:79:91:FC:CE:AC*/
class MainActivity : AppCompatActivity() {
     var volume : VolumeInfo  = VolumeInfo(0f,listOf(0f,0f))
    lateinit var slider:SeekBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val lst: MutableList<YoutubeApiVideo> = mutableListOf(YoutubeApiVideo("http://192.168.0.11:8080/https://www.youtube.com/watch?v=1OfoS6u_8N4"),YoutubeApiVideo("http://192.168.0.11:8080/https://www.youtube.com/watch?v=1OfoS6u_8N4"))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }



        slider = findViewById<SeekBar>(R.id.seekBar) as SeekBar
        "http://192.168.0.11:8080/init/getVolumeInfo".httpGet().responseObject(VolumeInfo.Deserializer()){req,res,result->

            val(x,err)  = result
            if (x != null) {
                volume = x

            }
            Log.d("log",result.toString())
            Log.d("log",volume.toString())
            Log.d("log",res.toString())

            if (volume != null){
                slider.max =  100
                slider.progress = (normalise(volume.MasterVolumeLevel,volume.VolumeRange[0],volume.VolumeRange[1]) * 100f).toInt()
                Log.d("log", "SliderMax " + slider.max)
                Log.d("log", "progress " + normalise(volume.MasterVolumeLevel,volume.VolumeRange[0],volume.VolumeRange[1]))
            }

        }
        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                val original = normaliseReverse(p1.toFloat()/100f,volume.VolumeRange[0],volume.VolumeRange[1])
                var jsonString = "{\"VolumeLevel\":$original}"
                Fuel.post("http://192.168.0.11:8080").body(jsonString).response { request, response, result ->
                }

                // Fuel.get("http://192.168.0.11:8080/conf/vol/" + original).response { request, response, result ->  }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })


        var  rv : RecyclerView = findViewById<RecyclerView>(R.id.rv)
        var llm : LinearLayoutManager = LinearLayoutManager(this)
        rv.layoutManager = llm
        var arr = arrayOf<String>( )
        var adapter = MyAdapter(lst)
        rv.adapter = adapter
        var button  = findViewById<Button>(R.id.button)

        button.setOnClickListener { view->
            //lst.add("1")
            //adapter.notifyDataSetChanged()
            lst.add(YoutubeApiVideo("https://www.youtube.com/watch?v=1OfoS6u_8N4"))
            adapter.notifyDataSetChanged()
        }




    }
    private fun normalise(value: Float,min:Float,max:Float) : Float{
        return (value-min) / (max - min)
    }
    private fun normaliseReverse(normalisedValue: Float,min:Float,max:Float) : Float{
        return  (normalisedValue * (max - min)) +min
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
