package expo.modules.datasyncnativekotlin.di.provider

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import expo.modules.datasyncnativekotlin.sdk.api.DataSyncConfig
import expo.modules.datasyncnativekotlin.sdk.data.mapper.AppJson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

fun provideOkHttpClient(config: DataSyncConfig): OkHttpClient {
    val builder =
        Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

    if (config.enableLogging) {
        builder.addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            },
        )
    }

    return builder.build()
}

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    config: DataSyncConfig,
): Retrofit =
    Retrofit
        .Builder()
        .baseUrl(config.baseUrl)
        .client(okHttpClient)
        .addConverterFactory(AppJson.instance.asConverterFactory("application/json".toMediaType()))
        .build()
