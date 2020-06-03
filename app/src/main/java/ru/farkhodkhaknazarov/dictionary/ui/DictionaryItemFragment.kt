package ru.farkhodkhaknazarov.dictionary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.farkhodkhaknazarov.dictionary.R
import ru.farkhodkhaknazarov.dictionary.presenters.DictionaryItemPresenter
import ru.farkhodkhaknazarov.dictionary.ui.fragments.DictionaryItemFragmentInterface

class DictionaryItemFragment : Fragment(), DictionaryItemFragmentInterface.View {
    val presenter: DictionaryItemFragmentInterface.Presenter by lazy {
        DictionaryItemPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dictionary_item, container, false)
        presenter.initView(activity as MainActivity, view, arguments)
        return view
    }

    override fun onPause() {
        super.onPause()
        presenter.onViewPaused()
    }

    override fun onStop() {
        presenter.onStopView()
        super.onStop()
    }
}