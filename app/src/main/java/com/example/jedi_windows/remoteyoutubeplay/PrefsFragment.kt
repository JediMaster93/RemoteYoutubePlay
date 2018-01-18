package com.example.jedi_windows.remoteyoutubeplay

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

/**
 * Created by Z003CHEK on 18.1.2018..
 */
public  class  PrefsFragment() : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootKey)
     }
}