package ru.farkhodkhaknazarov.dictionary.ui.utils

import android.os.Bundle
import android.preference.PreferenceActivity
import ru.farkhodkhaknazarov.dictionary.R


class SettingPrefActivity: PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings_preference)
    }
}