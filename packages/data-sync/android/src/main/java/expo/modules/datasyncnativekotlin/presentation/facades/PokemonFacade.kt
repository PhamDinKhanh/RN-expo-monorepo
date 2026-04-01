package expo.modules.datasyncnativekotlin.presentation.facades

import expo.modules.datasyncnativekotlin.core.network.NetworkClient
import expo.modules.datasyncnativekotlin.data.remote.api.PokeApiService
import expo.modules.datasyncnativekotlin.data.repository.PokemonRepositoryImpl
import expo.modules.datasyncnativekotlin.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonFacade {
    private val apiService = NetworkClient.provideRetrofit().create(PokeApiService::class.java)
    private val repository = PokemonRepositoryImpl(apiService)
    private val getPokemonListUseCase = GetPokemonListUseCase(repository)

    // Hàm này sẽ được gọi từ Expo Module (DataSyncModule.kt)
    suspend fun fetchPokemonsForJS(limit: Int): String {
        return withContext(Dispatchers.IO) {
            val result = getPokemonListUseCase(limit = limit, offset = 0)

            result.fold(
                onSuccess = { pokemons ->
                    // Trả về JSON string hoặc map sang kiểu dữ liệu Expo hỗ trợ
                    "Success: Fetched ${pokemons.size} pokemons. First is ${pokemons.firstOrNull()?.name}"
                },
                onFailure = { error ->
                    "Error: ${error.message}"
                }
            )
        }
    }
}