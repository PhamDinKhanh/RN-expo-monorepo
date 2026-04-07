package expo.modules.datasyncnativekotlin.sdk.api

import expo.modules.datasyncnativekotlin.domain.usecase.GetPokemonListUseCase
import expo.modules.datasyncnativekotlin.presentation.model.PokemonJSDto
import expo.modules.datasyncnativekotlin.presentation.model.PokemonPageJSDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultDataSyncSdk(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : DataSyncSdk {

    override suspend fun fetchPokemons(limit: Int): PokemonPageJSDto {
        return withContext(Dispatchers.IO) {
            val result = getPokemonListUseCase(limit = limit, offset = 0)

            result.fold(
                onSuccess = { page ->
                    PokemonPageJSDto(
                        count = page.count,
                        next = page.next,
                        previous = page.previous,
                        results = page.results.map { PokemonJSDto(it.name, it.detailUrl) }
                    )
                },
                onFailure = { error ->
                    throw Exception("Failed to fetch Pokemon: ${error.message}", error)
                }
            )
        }
    }
}