package ru.farkhodkhaknazarov.dictionary.data.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionaryList")
class DictionaryItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var direction: String,
    var textFrom: String,
    var textTo: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(direction)
        parcel.writeString(textFrom)
        parcel.writeString(textTo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DictionaryItem> {
        override fun createFromParcel(parcel: Parcel): DictionaryItem {
            return DictionaryItem(parcel)
        }

        override fun newArray(size: Int): Array<DictionaryItem?> {
            return arrayOfNulls(size)
        }
    }
}