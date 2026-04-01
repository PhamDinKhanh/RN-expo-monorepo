package expo.modules.datasyncnativekotlin.data.repository

import expo.modules.datasyncnativekotlin.data.mapper.toDomain
import expo.modules.datasyncnativekotlin.data.remote.api.PokeApiService
import expo.modules.datasyncnativekotlin.domain.model.Pokemon
import expo.modules.datasyncnativekotlin.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val apiService: PokeApiService
) : PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>> {
        return try {
            val response = apiService.fetchPokemons(limit, offset)

            if (response.isSuccessful) {
                // Lấy body, map danh sách DTO sang danh sách Domain Model
                val dtoList = response.body()?.results ?: emptyList()
                val pokemons = dtoList.map { it.toDomain() }

                Result.success(pokemons)
            } else {
                Result.failure(Exception("API Error: Code ${response.code()}"))
            }
        } catch (e: Exception) {
            // Bắt các lỗi như mất mạng, timeout...
            Result.failure(e)
        }
    }
}