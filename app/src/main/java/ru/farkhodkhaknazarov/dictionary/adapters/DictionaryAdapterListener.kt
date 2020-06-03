package ru.farkhodkhaknazarov.dictionary.adapters

import ru.farkhodkhaknazarov.dictionary.data.room.DictionaryItem

interface DictionaryAdapterListener {
    fun onItemSelected(contact: DictionaryItem)
}