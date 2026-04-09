package expo.modules.datasyncnativekotlin.sdk.data.local.dao

import androidx.room.Delete
import androidx.room.Upsert

interface BaseDao<T> {
    @Upsert
    suspend fun upsert(obj: T)

    @Upsert
    suspend fun upsertAll(objList: List<T>)

    @Delete
    suspend fun delete(obj: T)
}
