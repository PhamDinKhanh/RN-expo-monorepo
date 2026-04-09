package expo.modules.datasyncnativekotlin.sdk.data.mapper

import expo.modules.datasyncnativekotlin.sdk.data.local.entities.PokemonEntity
import expo.modules.datasyncnativekotlin.sdk.data.remote.dto.PokemonDto
import expo.modules.datasyncnativekotlin.sdk.data.remote.dto.PokemonListResponseDto
import expo.modules.datasyncnativekotlin.sdk.domain.model.Pokemon
import expo.modules.datasyncnativekotlin.sdk.domain.model.PokemonPage

/**
 * 1. NETWORK -> DOMAIN (parse response data from API to Domain)
 */
fun PokemonListResponseDto.toDomain(): PokemonPage =
    PokemonPage(
        count = this.count ?: 0,
        next = this.next,
        previous = this.previous,
        results = this.results?.map { it.toDomain() } ?: emptyList(),
    )

fun PokemonDto.toDomain(): Pokemon =
    Pokemon(
        name = this.name ?: "Unknown",
        detailUrl = this.url ?: "",
    )

/**
 * 2. NETWORK -> LOCAL (Save response data from API to Database)
 */
fun PokemonDto.toEntity(): PokemonEntity =
    PokemonEntity(
        name = this.name ?: "Unknown",
        url = this.url ?: "",
    )

/**
 * 3. LOCAL -> DOMAIN (Retrive from Database to Domain)
 */
fun PokemonEntity.toDomain(): Pokemon =
    Pokemon(
        name = this.name,
        detailUrl = this.url,
    )
