package ru.farkhodkhaknazarov.dictionary.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.farkhodkhaknazarov.dictionary.data.room.DictionaryItem

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM dictionaryList")
    fun allRepos(): List<DictionaryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepo(repo: DictionaryItem)

    @Query("DELETE FROM dictionarylist")
    fun deleteAllRepos()

    @Query("DELETE FROM dictionarylist WHERE id = :id")
    fun deleteRepoById(id: Long)

    @Query("SELECT * FROM dictionarylist WHERE id= :id")
    fun getrepoById(id: Int): DictionaryItem?
}