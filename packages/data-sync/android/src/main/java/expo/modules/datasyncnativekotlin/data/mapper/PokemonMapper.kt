package expo.modules.datasyncnativekotlin.data.mapper

import expo.modules.datasyncnativekotlin.data.local.entities.PokemonEntity
import expo.modules.datasyncnativekotlin.data.remote.dto.PokemonDto
import expo.modules.datasyncnativekotlin.data.remote.dto.PokemonListResponseDto
import expo.modules.datasyncnativekotlin.domain.model.Pokemon
import expo.modules.datasyncnativekotlin.domain.model.PokemonPage

/**
 * 1. NETWORK -> DOMAIN (parse response data from API to Domain)
 */
fun PokemonListResponseDto.toDomain(): PokemonPage {
    return PokemonPage(
        count = this.count ?: 0,
        next = this.next,
        previous = this.previous,
        results = this.results?.map { it.toDomain() } ?: emptyList()
    )
}

fun PokemonDto.toDomain(): Pokemon {
    return Pokemon(
        name = this.name ?: "Unknown",

        detailUrl = this.url ?: ""
    )
}

/**
 * 2. NETWORK -> LOCAL (Save response data from API to Database)
 */
fun PokemonDto.toEntity(): PokemonEntity {
    return PokemonEntity(
        name = this.name ?: "Unknown",
        url = this.url ?: ""
    )
}

/**
 * 3. LOCAL -> DOMAIN (Retrive from Database to Domain)
 */
fun PokemonEntity.toDomain(): Pokemon {
    return Pokemon(
        name = this.name,
        detailUrl = this.url
    )
}