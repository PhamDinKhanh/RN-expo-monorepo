package expo.modules.datasyncnativekotlin.sdk.api

data class DataSyncConfig(
    val baseUrl: String = "https://pokeapi.co/api/v2/",
    val databaseName: String = "tablet_offline_sync.db",
    val enableLogging: Boolean = false,
)
