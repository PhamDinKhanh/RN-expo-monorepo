package expo.modules.datasyncnativekotlin.sdk.api

data class SdkPokemonPage(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<SdkPokemon>,
)
