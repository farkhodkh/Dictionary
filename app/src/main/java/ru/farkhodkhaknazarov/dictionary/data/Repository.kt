package ru.farkhodkhaknazarov.dictionary.data

import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.farkhodkhaknazarov.dictionary.App
import ru.farkhodkhaknazarov.dictionary.BuildConfig
import ru.farkhodkhaknazarov.dictionary.common.Constants
import ru.farkhodkhaknazarov.dictionary.data.room.DictionaryItem
import ru.farkhodkhaknazarov.dictionary.data.room.RoomSingleton
import ru.farkhodkhaknazarov.dictionary.data.volley.TranslationResult
import ru.farkhodkhaknazarov.dictionary.presenters.DictionaryItemPresenter
import ru.farkhodkhaknazarov.dictionary.ui.MainActivity
import kotlin.reflect.KProperty

class Repository {

    var activity: MainActivity? = null
    var presenter: DictionaryItemPresenter? = null

    var mDb: RoomSingleton by lazy {
        RoomSingleton.getInstance(App.mInstance)
    }

    fun saveData(value: DictionaryItem?) {
        GlobalScope.launch(Dispatchers.IO) {
            value?.let {
                if(!value.textFrom.equals("")){
                    mDb.reposDao().insertRepo(value)
                }
            }
        }
    }

    fun deletaData(value: DictionaryItem?) {
        GlobalScope.launch(Dispatchers.IO) {
            value?.let {
                mDb.reposDao().deleteRepoById(value.id)
            }
        }
    }

    fun getData(): List<DictionaryItem> {
        val dictionaryList = arrayListOf<DictionaryItem>(
            DictionaryItem(0, "ru-en", "Привет", "Hi"),
            DictionaryItem(1, "ru-en", "Хорошо", "Good"),
            DictionaryItem(2, "ru-en", "Ночь", "Night")
        )

        dictionaryList.clear()
        dictionaryList.addAll(mDb.reposDao().allRepos())

        return dictionaryList
    }

    fun translateText(direction: String, text: String) {

        if (activity == null) {
            return
        }

        val queue = Volley.newRequestQueue(activity)

        val request = JsonObjectRequest(
            Request.Method.GET,
            Constants.URL + "/translate?key=" + BuildConfig.YANDEXTRANSLATEAPIKEY + "&text=$text&lang=$direction",
            null,
            object : Response.Listener<JSONObject?> {
                override fun onResponse(response: JSONObject?) {
                    if (response == null) {
                        Toast.makeText(
                            App.mInstance,
                            "Couldn't fetch the translation! Pleas try again.",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }

                    var direction =
                        Gson().fromJson<TranslationResult>(
                            response.toString(),
                            object : TypeToken<TranslationResult?>() {}.type
                        )
                    presenter?.updateTranslation(direction.text.get(0))
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    Toast.makeText(
                        App.mInstance,
                        "Error: " + error.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
        queue.add(request)
    }

    fun setActivity(activity: MainActivity): Repository {
        this.activity = activity
        return this
    }

    fun setPresenter(presenter: DictionaryItemPresenter): Repository {
        this.presenter = presenter
        return this

    }
}

private operator fun Any.setValue(
    repository: Repository,
    property: KProperty<*>,
    roomSingleton: RoomSingleton
) {
}
