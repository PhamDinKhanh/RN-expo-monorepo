package expo.modules.datasyncnativekotlin.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SyncQueueDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun enqueue(action: SyncQueueEntity)

    @Query("SELECT * FROM sync_queue ORDER BY occurredAt ASC LIMIT :limit")
    suspend fun getNextBatch(limit: Int): List<SyncQueueEntity>

    @Query("DELETE FROM sync_queue WHERE queueId = :id")
    suspend fun dequeue(id: Long)

    @Delete
    suspend fun deleteBatch(items: List<SyncQueueEntity>)

    @Query("UPDATE sync_queue SET retryCount = retryCount + 1 WHERE queueId = :id")
    suspend fun incrementRetryCount(id: Long)

    @Query("SELECT COUNT(*) FROM sync_queue")
    suspend fun getQueueSize(): Int

    @Query("DELETE FROM sync_queue")
    suspend fun clearQueue()
}