package expo.modules.datasyncnativekotlin.domain.usecase

import expo.modules.datasyncnativekotlin.domain.model.Pokemon
import expo.modules.datasyncnativekotlin.domain.repository.PokemonRepository

class GetPokemonListUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(limit: Int = 20, offset: Int = 0): Result<List<Pokemon>> {
        if (limit <= 0) return Result.failure(IllegalArgumentException("Limit must be > 0"))

        return repository.getPokemonList(limit, offset)
    }
}