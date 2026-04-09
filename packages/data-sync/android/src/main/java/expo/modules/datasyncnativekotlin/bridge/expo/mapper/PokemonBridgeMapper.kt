package expo.modules.datasyncnativekotlin.bridge.expo.mapper

import expo.modules.datasyncnativekotlin.bridge.expo.dto.PokemonJSDto
import expo.modules.datasyncnativekotlin.bridge.expo.dto.PokemonPageJSDto
import expo.modules.datasyncnativekotlin.sdk.api.SdkPokemonPage

fun SdkPokemonPage.toJSDto(): PokemonPageJSDto =
    PokemonPageJSDto(
        count = count,
        next = next,
        previous = previous,
        results = results.map { PokemonJSDto(it.name, it.detailUrl) },
    )
