package com.example.jedi_windows.remoteyoutubeplay

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import com.afollestad.materialdialogs.MaterialDialog
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.mikepenz.iconics.context.IconicsLayoutInflater

/* com.example.jedi_windows.remoteyoutubeplay  F3:D3:D4:78:C0:6C:00:89:AF:62:A3:2A:F0:DE:36:79:91:FC:CE:AC*/
class MainActivity : AppCompatActivity(){

    var volume : VolumeInfo  = VolumeInfo(0f,listOf(0f,0f))
    lateinit var slider:SeekBar
    lateinit var  videos : MutableList<YoutubeApiVideo>
    lateinit var adapter : MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),  IconicsLayoutInflater(getDelegate()))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPrefs = getPreferences(Context.MODE_PRIVATE)
        val url = "http://${sharedPrefs.getString(getString(R.string.preferenceIP),"none")}:${sharedPrefs.getString(getString(R.string.preferencePort),"none")}"
        if ("none" in url){
            //todo force user to input port and adress somehow.
        }

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        initVolumeSlider()

        var videosJson =  sharedPrefs.getString("videos",null)
        videos = mutableListOf<YoutubeApiVideo>()

        videosJson?.let {
            var urlList  = Gson().fromJson<List<String>>(it)
            urlList.forEach { i-> videos.add(YoutubeApiVideo(i)) }
        }

        var  rv : RecyclerView = findViewById<RecyclerView>(R.id.rv)
        var llm : LinearLayoutManager = LinearLayoutManager(this)
        rv.layoutManager = llm
        var arr = arrayOf<String>( )
        adapter = MyAdapter(videos)
        videos.forEach { video->video.listener=adapter}
        rv.adapter = adapter
        var fab = findViewById<View>(R.id.fab)
        fab.setOnClickListener { view ->
            MaterialDialog.Builder(this)
                    .title("Youtube URL")
                    .input("192.168.0.11","",MaterialDialog.InputCallback { dialog, input ->
                        videos.add(YoutubeApiVideo(input.toString(),adapter))
                        adapter.notifyDataSetChanged()
                    }).show()
         }

        var callback = SimpleItemTouchHelperCallback(adapter)
        var touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(rv)

        var bottomSheet = findViewById<View>(R.id.bottom_sheet)
        var bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehaviour.setBottomSheetCallback(object:BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                fab.animate().scaleX(1-slideOffset).scaleY(1-slideOffset).setDuration(0).start()
             }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
             }
        })


    }

    private fun initVolumeSlider() {
        slider = findViewById<SeekBar>(R.id.seekBar) as SeekBar
        "http://192.168.0.11:8080/init/getVolumeInfo".httpGet().responseObject(VolumeInfo.Deserializer()) { req, res, result ->
            val (x, err) = result
            if (x != null) {
                volume = x
            }
            if (volume != null) {
                slider.max = 100
                slider.progress = (normalise(volume.MasterVolumeLevel, volume.VolumeRange[0], volume.VolumeRange[1]) * 100f).toInt()
            }

        }
        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                val original = normaliseReverse(p1.toFloat() / 100f, volume.VolumeRange[0], volume.VolumeRange[1])
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
    override fun onPause() {
        super.onPause()
        val sharedPrefs = getPreferences(Context.MODE_PRIVATE)
        with(sharedPrefs.edit()){
            videos.forEach { vid-> vid.listener=null }
            val str : String = Gson().toJson(videos.map { it.youtubeURL })
            putString("videos", str)
            commit()
            Log.d("log","sting is ${str} and len is ${str.length}")
        }


    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

}
