package top.topsea.calculator.database

import androidx.room.*

@Dao
interface RecordDao {
    @Query("SELECT * FROM record")
    fun getAll(): List<Record>

    @Query("SELECT * FROM record WHERE recordId IN (:recordIds)")
    fun loadAllByIds(recordIds: IntArray): List<Record>

    @Query("SELECT formula FROM record")
    fun getAllRecord():List<String>

    @Update()
    fun updateRecord(record: Record)

    @Insert
    fun insertAll(vararg record: Record)

    @Delete
    fun delete(record: Record)
}