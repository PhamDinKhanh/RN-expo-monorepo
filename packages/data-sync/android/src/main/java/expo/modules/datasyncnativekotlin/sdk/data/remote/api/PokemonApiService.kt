package expo.modules.datasyncnativekotlin.sdk.data.remote.api

import expo.modules.datasyncnativekotlin.sdk.data.remote.dto.PokemonListResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    suspend fun fetchPokemons(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
    ): Response<PokemonListResponseDto>
}
