package com.example.jedi_windows.remoteyoutubeplay

import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v7.preference.PreferenceFragmentCompat

/**
 * Created by Z003CHEK on 18.1.2018..
 */
public  class  PrefsFragment() : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}

