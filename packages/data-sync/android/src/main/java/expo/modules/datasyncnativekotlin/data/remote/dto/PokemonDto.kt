package expo.modules.datasyncnativekotlin.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponseDto(
    @SerialName("count") val count: Int? = null,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<PokemonDto>? = null
)

@Serializable
data class PokemonDto(
    @SerialName("name") val name: String? = null,
    @SerialName("url") val url: String? = null
)