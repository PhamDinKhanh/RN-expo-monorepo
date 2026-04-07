package expo.modules.datasyncnativekotlin.data.repository

import expo.modules.datasyncnativekotlin.data.local.dao.PokemonDao
import expo.modules.datasyncnativekotlin.data.mapper.toDomain
import expo.modules.datasyncnativekotlin.data.remote.api.PokemonApiService
import expo.modules.datasyncnativekotlin.domain.model.PokemonPage
import expo.modules.datasyncnativekotlin.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val apiService: PokemonApiService,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    // ==========================================
    // COMMON FUNCTION: Chuyên đọc và map dữ liệu từ Room
    // ==========================================
    private suspend fun getLocalPokemonPage(limit: Int, offset: Int): PokemonPage {
        val localEntities = pokemonDao.getPokemons(limit, offset)
        val totalCount = pokemonDao.getTotalCount()

        return PokemonPage(
            count = totalCount,
            next = null,
            previous = null,
            results = localEntities.map { it.toDomain() }
        )
    }

    override suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonPage> {

        //Remove comment nếu bạn muốn lấy dữ liệu từ API -> local database -> RN
//        runCatching {
//            val response = apiService.fetchPokemons(limit, offset)
//            if (response.isSuccessful) {
//                val dtos = response.body()?.results ?: emptyList()
//                pokemonDao.insertAll(dtos.map { it.toEntity() })
//            }
//        }

        // 2. Đọc từ "Nguồn chân lý duy nhất" (Room)
        val localPage = getLocalPokemonPage(limit, offset)

        // 3. Quyết định kết quả trả về
        return if (localPage.results.isNotEmpty()) {
            Result.success(localPage)
        } else {
            // Chỉ báo lỗi khi cả API và DB đều không có gì
            Result.failure(Exception("No data available offline or online"))
        }
    }
}