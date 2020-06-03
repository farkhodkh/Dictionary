package ru.farkhodkhaknazarov.dictionary.ui.fragments

import android.os.Bundle
import ru.farkhodkhaknazarov.dictionary.ui.MainActivity

interface DictionaryItemFragmentInterface {
    interface View{

    }

    interface Presenter{
        fun initView(activityView: MainActivity, view: android.view.View?, arguments: Bundle?)
        fun updateTranslation(value: String)
        fun onViewPaused()
        fun onStopView()
    }
}