package ru.farkhodkhaknazarov.dictionary.ui.fragments

import android.view.Menu
import android.view.MenuItem

interface MainActivityView {
    interface View{

    }

    interface Presenter {
        fun initView(view: View)
        fun onResumeView()
        fun prepareOptionsMenu(menu: Menu?): Boolean
        fun onViewOptionSelected(item: MenuItem): Boolean
        fun removeFragment()
    }
}