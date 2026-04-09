package expo.modules.datasyncnativekotlin.sdk.data.repository

import expo.modules.datasyncnativekotlin.sdk.data.local.dao.OutboxDao
import expo.modules.datasyncnativekotlin.sdk.data.local.dao.PokemonDao
import expo.modules.datasyncnativekotlin.sdk.data.local.entities.OutboxEntity
import expo.modules.datasyncnativekotlin.sdk.data.local.entities.PokemonEntity
import expo.modules.datasyncnativekotlin.sdk.data.mapper.AppJson
import expo.modules.datasyncnativekotlin.sdk.data.mapper.toDomain
import expo.modules.datasyncnativekotlin.sdk.data.mapper.toEntity
import expo.modules.datasyncnativekotlin.sdk.data.remote.api.PokemonApiService
import expo.modules.datasyncnativekotlin.sdk.data.transaction.TransactionRunner
import expo.modules.datasyncnativekotlin.sdk.domain.model.PokemonPage
import expo.modules.datasyncnativekotlin.sdk.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val pokemonDao: PokemonDao,
    private val outboxDao: OutboxDao,
    private val transactionRunner: TransactionRunner,
    private val apiService: PokemonApiService,
) : PokemonRepository {
    private suspend fun getLocalPokemonPage(
        limit: Int,
        offset: Int,
    ): PokemonPage {
        val localEntities = pokemonDao.getPokemons(limit, offset)
        val totalCount = pokemonDao.getTotalCount()

        return PokemonPage(
            count = totalCount,
            next = null,
            previous = null,
            results = localEntities.map { it.toDomain() },
        )
    }

    override suspend fun getPokemonList(
        limit: Int,
        offset: Int,
    ): Result<PokemonPage> {
        runCatching {
            val response = apiService.fetchPokemons(limit, offset)
            if (response.isSuccessful) {
                val dtos = response.body()?.results ?: emptyList()
                pokemonDao.upsertAll(dtos.map { it.toEntity() })
            }
        }.onFailure {
            // Log lỗi nếu cần, nhưng vẫn fallback về Room
        }

        val localPage = getLocalPokemonPage(limit, offset)

        return if (localPage.results.isNotEmpty()) {
            Result.success(localPage)
        } else {
            Result.failure(Exception("No data available offline or online"))
        }
    }

    override suspend fun savePokemonWithEvent(
        pokemon: PokemonEntity,
        isFromSync: Boolean,
    ) {
        transactionRunner.run {
            pokemonDao.upsert(pokemon)

            if (!isFromSync) {
                val event =
                    OutboxEntity(
                        aggregateId = pokemon.id.toString(),
                        eventType = "UPSERT_POKEMON",
                        payload = AppJson.instance.encodeToString(pokemon),
                    )
                outboxDao.upsert(event)
            }
        }
    }
}
