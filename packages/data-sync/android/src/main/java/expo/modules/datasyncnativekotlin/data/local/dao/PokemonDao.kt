package expo.modules.datasyncnativekotlin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import expo.modules.datasyncnativekotlin.data.local.entities.PokemonEntity

@Dao
interface PokemonDao : BaseDao<PokemonEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon_table LIMIT :limit OFFSET :offset")
    suspend fun getPokemons(limit: Int, offset: Int): List<PokemonEntity>

    @Query("SELECT COUNT(*) FROM pokemon_table")
    suspend fun getTotalCount(): Int
}

