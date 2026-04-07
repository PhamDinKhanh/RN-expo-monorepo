package expo.modules.datasyncnativekotlin.sdk.api

import expo.modules.datasyncnativekotlin.sdk.application.facade.PokemonCatalogFacade
import expo.modules.datasyncnativekotlin.sdk.domain.model.PokemonPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultDataSyncSdk(
    private val pokemonCatalogFacade: PokemonCatalogFacade
) : DataSyncSdk {

    override suspend fun fetchPokemons(limit: Int): SdkPokemonPage {
        return withContext(Dispatchers.IO) {
            pokemonCatalogFacade.fetchPokemons(limit = limit).toSdkModel()
        }
    }
}

private fun PokemonPage.toSdkModel(): SdkPokemonPage {
    return SdkPokemonPage(
        count = count,
        next = next,
        previous = previous,
        results = results.map { SdkPokemon(it.name, it.detailUrl) }
    )
}
