package ru.farkhodkhaknazarov.dictionary.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.farkhodkhaknazarov.dictionary.data.room.dao.DictionaryDao

@Database(entities = arrayOf(DictionaryItem::class), version = 1, exportSchema = false)
abstract class RoomSingleton: RoomDatabase() {
    abstract fun reposDao(): DictionaryDao

    companion object {
        private var INSTANCE: RoomSingleton? = null
        fun getInstance(context: Context): RoomSingleton {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    RoomSingleton::class.java,
                    "roomdb_dictionary"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as RoomSingleton
        }
    }
}