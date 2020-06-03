package ru.farkhodkhaknazarov.dictionary.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ru.farkhodkhaknazarov.dictionary.R
import ru.farkhodkhaknazarov.dictionary.presenters.MainActivityPresenter
import ru.farkhodkhaknazarov.dictionary.ui.fragments.MainActivityView

class MainActivity : AppCompatActivity(), MainActivityView.View {
    lateinit var presenter: MainActivityView.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.search_hint)

        presenter = MainActivityPresenter()
        presenter.initView(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return presenter.prepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return presenter.onViewOptionSelected(item)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResumeView()
    }

}