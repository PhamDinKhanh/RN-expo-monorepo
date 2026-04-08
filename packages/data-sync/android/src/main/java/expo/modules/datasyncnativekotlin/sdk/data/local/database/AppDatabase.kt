package expo.modules.datasyncnativekotlin.sdk.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import expo.modules.datasyncnativekotlin.sdk.data.local.dao.OutboxDao
import expo.modules.datasyncnativekotlin.sdk.data.local.dao.PokemonDao
import expo.modules.datasyncnativekotlin.sdk.data.local.entities.OutboxEntity
import expo.modules.datasyncnativekotlin.sdk.data.local.entities.PokemonEntity

@Database(
    entities = [PokemonEntity::class, OutboxEntity::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao

    abstract fun outboxDao(): OutboxDao
}
