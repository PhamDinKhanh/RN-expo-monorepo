package expo.modules.datasyncnativekotlin.presentation.model

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

data class PokemonJSDto(
    @Field val name: String,
    @Field val detailUrl: String
) : Record
