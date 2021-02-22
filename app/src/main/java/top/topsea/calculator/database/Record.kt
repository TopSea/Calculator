package top.topsea.calculator.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Record (
    @PrimaryKey
    var recordId: Int,
    @ColumnInfo(name = "formula") var formula: String?,
    @ColumnInfo(name = "time") var time: String?
)