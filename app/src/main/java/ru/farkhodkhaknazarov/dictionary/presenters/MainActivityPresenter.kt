package ru.farkhodkhaknazarov.dictionary.presenters

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.farkhodkhaknazarov.dictionary.App
import ru.farkhodkhaknazarov.dictionary.R
import ru.farkhodkhaknazarov.dictionary.adapters.DictionaryAdapter
import ru.farkhodkhaknazarov.dictionary.adapters.DictionaryAdapterListener
import ru.farkhodkhaknazarov.dictionary.data.Repository
import ru.farkhodkhaknazarov.dictionary.data.room.DictionaryItem
import ru.farkhodkhaknazarov.dictionary.ui.DictionaryItemFragment
import ru.farkhodkhaknazarov.dictionary.ui.MainActivity
import ru.farkhodkhaknazarov.dictionary.ui.fragments.MainActivityView
import ru.farkhodkhaknazarov.dictionary.ui.utils.SettingPrefActivity

class MainActivityPresenter : MainActivityView.Presenter, DictionaryAdapterListener,
    SharedPreferences.OnSharedPreferenceChangeListener {
    lateinit var translationDirection: String
    lateinit var view: MainActivityView.View
    lateinit var recyclerView: RecyclerView
    lateinit var dictionaryList: ArrayList<DictionaryItem>
    lateinit var mAdapter: DictionaryAdapter
    lateinit var searchView: SearchView
    lateinit var floatingActionButton: FloatingActionButton
    val NEW_ITEM_FRAG_TAG = "DictionaruNewItem"

    val repository by lazy {
        Repository().setActivity(view as MainActivity)
    }

    override fun initView(view: MainActivityView.View) {
        this.view = view as MainActivity
        recyclerView = view.findViewById(R.id.mainRecyclerView)
        dictionaryList = arrayListOf()

        mAdapter = DictionaryAdapter(App.mInstance, dictionaryList, this)

        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(App.mInstance)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()

        recyclerView.adapter = mAdapter

        floatingActionButton = view.findViewById(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener { view ->
            openItemFragment()
        }

        val context: Context =
            view.applicationContext
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.registerOnSharedPreferenceChangeListener(this)
        translationDirection = prefs.getString("translationDirection", "ru-en") as String
    }

    override fun prepareOptionsMenu(menu: Menu?): Boolean {

        val searchManager =
            (view as MainActivity).getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.action_search)!!.actionView as SearchView
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo((view as MainActivity).componentName)
        )

        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                mAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                mAdapter.filter.filter(query)
                return false
            }
        })

        val settingsItem = menu.findItem(R.id.action_settings)

        settingsItem.setOnMenuItemClickListener {
            openSettings()
        }

        return true
    }

    override fun onViewOptionSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            R.id.action_search -> true
            else ->{
                onBackPressed()
                true
            }
        }

    fun onBackPressed(){
        removeFragment()
    }

    fun openItemFragment(dictItem:DictionaryItem? = null) {
        val params = Bundle()
        params.putString("translationDirection", translationDirection)
        dictItem?.let {params.putParcelable("item", dictItem)}
        recyclerView.visibility = View.INVISIBLE
        floatingActionButton.visibility = View.INVISIBLE

        (view as MainActivity).supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, DictionaryItemFragment::class.java, params, NEW_ITEM_FRAG_TAG)
            .commit()
    }

    fun openSettings(): Boolean {

        val intent = Intent(App.mInstance, SettingPrefActivity::class.java)
        startActivity(App.mInstance, intent, null)
        return true
    }

    override fun onItemSelected(dictItem: DictionaryItem) {
        openItemFragment(dictItem)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        translationDirection = sharedPreferences?.getString("translationDirection", "ru-en") as String
    }

    override fun onResumeView() {
        updateList()
        recyclerView.visibility = View.VISIBLE
        floatingActionButton.visibility = View.VISIBLE
    }

    fun updateList() {
        dictionaryList.clear()

        GlobalScope.launch(Dispatchers.IO) {
            delay(500)
            dictionaryList.addAll(repository.getData())
            GlobalScope.launch(Dispatchers.Main) {
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun removeFragment() {
        val fragmentManager = (view as MainActivity).supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(NEW_ITEM_FRAG_TAG)

        if(fragment!=null){
            fragmentManager.beginTransaction().remove(fragment).commit()
        }
    }
}