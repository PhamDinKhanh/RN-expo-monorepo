package expo.modules.datasyncnativekotlin.sdk.api

import expo.modules.datasyncnativekotlin.sdk.application.facade.PokemonCatalogFacade
import expo.modules.datasyncnativekotlin.sdk.domain.model.PokemonPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DefaultDataSyncSdk(
    private val pokemonCatalogFacade: PokemonCatalogFacade,
    private val featureFlagsApi: FeatureFlagsApi,
    private val networkApi: NetworkApi,
    private val nfcApi: NfcApi,
) : DataSyncSdk {
    override suspend fun fetchPokemons(
        limit: Int,
        offset: Int,
    ): SdkPokemonPage =
        withContext(Dispatchers.IO) {
            pokemonCatalogFacade.fetchPokemons(limit = limit, offset = offset).toSdkModel()
        }

    override fun isFeatureEnabled(
        featureKey: String,
        defaultValue: Boolean,
    ): Boolean = featureFlagsApi.isFeatureEnabled(featureKey, defaultValue)

    override fun getAllFlags(): Map<String, Boolean> = featureFlagsApi.getAllFlags()

    override suspend fun syncFlags() {
        featureFlagsApi.syncFlags()
    }

    override fun isConnected(): Boolean = networkApi.isConnected()

    override fun getNetworkStatus(): NetworkStatus = networkApi.getNetworkStatus()

    override fun observeNetworkStatus(): Flow<NetworkStatus> = networkApi.observeStatus()

    override fun startNfcSession(onTagRead: (String) -> Unit): Boolean = nfcApi.startSession(onTagRead)

    override fun stopNfcSession() {
        nfcApi.stopSession()
    }
}

private fun PokemonPage.toSdkModel(): SdkPokemonPage =
    SdkPokemonPage(
        count = count,
        next = next,
        previous = previous,
        results = results.map { SdkPokemon(it.name, it.detailUrl) },
    )
