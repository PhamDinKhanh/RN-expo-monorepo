package expo.modules.datasyncnativekotlin.sdk.domain.model

data class PokemonPage(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>,
)

data class Pokemon(
    val name: String,
    val detailUrl: String,
)
