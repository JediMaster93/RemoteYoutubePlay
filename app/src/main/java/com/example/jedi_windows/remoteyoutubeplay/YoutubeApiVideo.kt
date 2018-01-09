package com.example.jedi_windows.remoteyoutubeplay

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.google.gson.JsonObject

/**
 * Created by Jedi-Windows on 08.01.2018..
 */

class YoutubeApiVideo(val youtubeURL: String) {
    var id: String? = null
    var thumbnailURL: String? = null
    var title: String? = null
    private val apiURL = "https://www.googleapis.com/youtube/v3/videos"

    init {
        val id = getID()
        Fuel.get(apiURL, listOf("id" to id, "part" to "snippet", "key" to "AIzaSyDpO3xPsORhtVpJxxVehpF6gBC6amgUR7I")).responseString { request, response, result ->
            val (d, err) = result
            Log.d("log", "Response is $d}")
            val gson = Gson()
            if (d != null) {
                val json: JsonObject = gson.fromJson(d)
                thumbnailURL = json["items"][0]["snippet"]["thumbnails"]["high"]["url"].toString().trim('\"')
                Log.d("log", "thumbnail url is $thumbnailURL}")

                title = json["items"][0]["snippet"]["title"].toString()

            }
        }

    }

    private fun getID(): String? {
        val regex = Regex("""watch\?v=(\w+)""")
        val result: MatchResult? = regex.find(youtubeURL)
        return result?.groups?.get(1)?.value
    }
}
