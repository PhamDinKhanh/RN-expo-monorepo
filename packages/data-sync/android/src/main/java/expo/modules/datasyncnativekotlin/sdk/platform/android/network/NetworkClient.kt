package expo.modules.datasyncnativekotlin.sdk.platform.android.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import expo.modules.datasyncnativekotlin.sdk.data.mapper.AppJson
import expo.modules.datasyncnativekotlin.sdk.data.remote.api.PokemonApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object NetworkClient {
    internal const val BASE_URL = "https://pokeapi.co/api/v2/"

    fun provideRetrofit(): Retrofit {
        // 2. Cấu hình OkHttp Engine
        val okHttpClient =
            OkHttpClient
                .Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                // Bạn có thể .addInterceptor() ở đây sau này
                .build()

        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(AppJson.instance.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    // Tạo instance của ApiService
    fun providePokeApi(retrofit: Retrofit): PokemonApiService = retrofit.create(PokemonApiService::class.java)
}
