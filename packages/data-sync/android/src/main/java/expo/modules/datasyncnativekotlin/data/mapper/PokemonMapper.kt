package expo.modules.datasyncnativekotlin.data.mapper

import expo.modules.datasyncnativekotlin.data.remote.dto.PokemonDto
import expo.modules.datasyncnativekotlin.domain.model.Pokemon

fun PokemonDto.toDomain(): Pokemon {
    return Pokemon(
        name = this.name ?: "Unknown",

        detailUrl = this.url ?: ""
    )
}