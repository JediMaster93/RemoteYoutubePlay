package com.example.jedi_windows.remoteyoutubeplay

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

/**
 * Created by Jedi-Windows on 05.01.2018..
 */
data class VolumeInfo(val MasterVolumeLevel:Float =0.0f,
                      val VolumeRange:List<Float>){


     class Deserializer : ResponseDeserializable<VolumeInfo> {
        override fun deserialize(content: String) = Gson().fromJson(content, VolumeInfo::class.java)
    }

}