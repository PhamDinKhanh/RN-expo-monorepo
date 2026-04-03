package expo.modules.datasyncnativekotlin.presentation.model

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

data class PokemonPageJSDto(
    @Field val count: Int,
    @Field val next: String?,
    @Field val previous: String?,
    @Field val results: List<PokemonJSDto>
) : Record

