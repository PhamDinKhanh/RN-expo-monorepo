package expo.modules.datasyncnativekotlin.domain.usecase

import expo.modules.datasyncnativekotlin.domain.model.PokemonPage
import expo.modules.datasyncnativekotlin.domain.repository.PokemonRepository

class GetPokemonListUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(limit: Int = 20, offset: Int = 0): Result<PokemonPage> {
        return repository.getPokemonList(limit, offset)
    }
}