package ru.farkhodkhaknazarov.dictionary.presenters

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ru.farkhodkhaknazarov.dictionary.R
import ru.farkhodkhaknazarov.dictionary.data.Repository
import ru.farkhodkhaknazarov.dictionary.data.room.DictionaryItem
import ru.farkhodkhaknazarov.dictionary.ui.MainActivity
import ru.farkhodkhaknazarov.dictionary.ui.fragments.DictionaryItemFragmentInterface
import kotlin.properties.Delegates

class DictionaryItemPresenter : DictionaryItemFragmentInterface.Presenter {
    lateinit var activity: MainActivity
    lateinit var tvTextFrom: TextView
    lateinit var tvTextTo: TextView
    lateinit var etText: EditText
    lateinit var tvTranslation: TextView
    lateinit var delButton: Button
    lateinit var translationDirection: String
    var dictItem: DictionaryItem? = null

    var transText: String by Delegates.observable("translateTextByYandexTranslate") { prop, old, new ->
        dictItem?.textFrom = new
        repo.translateText(translationDirection, new)
    }

    val repo: Repository by lazy {
        Repository().setActivity(activity).setPresenter(this)
    }

    override fun initView(activity: MainActivity, view: View?, arguments: Bundle?) {

        this.activity = activity

        etText = view?.findViewById(R.id.etText)!!
        tvTextFrom = view.findViewById(R.id.tvTextFrom)
        tvTextTo = view.findViewById(R.id.tvTextTo)
        tvTranslation = view.findViewById<TextView>(R.id.tvTranslation)

        if (arguments?.containsKey("translationDirection") == true) {
            translationDirection = arguments.get("translationDirection") as String
            val descriptionArray = activity.resources.getStringArray(R.array.translation_direction)
            val descriptionDescriptionArray =
                activity.resources.getStringArray(R.array.translation_direction_description)

            val index = descriptionArray.indexOf(translationDirection)

            val value = descriptionDescriptionArray.get(index)
            val dividerIndex = value.indexOf("-")

            tvTextFrom.text = value.substring(0, dividerIndex)
            tvTextTo.text = value.substring(dividerIndex + 1)
        }

        if (arguments?.containsKey("item") == true) {
            dictItem = arguments.get("item") as DictionaryItem
            etText.setText(dictItem?.textFrom)
            tvTranslation.text = dictItem?.textTo
        }else{
            dictItem = DictionaryItem(0, translationDirection, "", "")
        }

        etText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(source: Editable?) {
                println(source)
                transText = source.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        delButton = view.findViewById(R.id.btnDelete)
        delButton.setOnClickListener {
            repo.deletaData(dictItem)
            dictItem=null
            activity.presenter.removeFragment()
        }
    }

    override fun onViewPaused() {
        dictItem?.let{
            repo.saveData(dictItem)
        }
    }

    override fun updateTranslation(value: String) {
        tvTranslation.text = value
        dictItem?.textTo = value
    }

    override fun onStopView() {
        activity.presenter.onResumeView()
    }
}