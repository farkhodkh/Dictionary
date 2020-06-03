package ru.farkhodkhaknazarov.dictionary.data.volley

import com.google.gson.annotations.SerializedName

data class TranslationResult(
    @SerializedName("code")
    var code: Int,
    @SerializedName("lang")
    var lang: String,
    @SerializedName("text")
    var text: ArrayList<String>
)