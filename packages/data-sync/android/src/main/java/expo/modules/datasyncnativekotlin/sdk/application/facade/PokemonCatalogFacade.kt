package expo.modules.datasyncnativekotlin.sdk.application.facade

import expo.modules.datasyncnativekotlin.sdk.application.usecase.GetPokemonListUseCase
import expo.modules.datasyncnativekotlin.sdk.domain.model.PokemonPage

class PokemonCatalogFacade(
    private val getPokemonListUseCase: GetPokemonListUseCase,
) {
    suspend fun fetchPokemons(
        limit: Int,
        offset: Int = 0,
    ): PokemonPage {
        val normalizedLimit = limit.coerceAtLeast(1)
        val normalizedOffset = offset.coerceAtLeast(0)

        return getPokemonListUseCase(normalizedLimit, normalizedOffset).getOrElse { error ->
            throw PokemonCatalogException(
                limit = normalizedLimit,
                offset = normalizedOffset,
                cause = error,
            )
        }
    }
}

class PokemonCatalogException(
    limit: Int,
    offset: Int,
    cause: Throwable,
) : RuntimeException(
        "Failed to fetch pokemons with limit=$limit and offset=$offset",
        cause,
    )
