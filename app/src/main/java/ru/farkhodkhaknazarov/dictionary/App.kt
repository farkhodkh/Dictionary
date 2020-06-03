package ru.farkhodkhaknazarov.dictionary

import android.app.Application

class App:Application() {
    companion object{
        lateinit var mInstance: App
    }

    override fun onCreate() {
        mInstance = this
        super.onCreate()
    }
}