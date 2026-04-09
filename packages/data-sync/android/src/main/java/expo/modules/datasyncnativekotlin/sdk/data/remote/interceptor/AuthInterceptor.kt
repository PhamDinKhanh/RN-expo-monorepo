package expo.modules.datasyncnativekotlin.sdk.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: () -> String?,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest =
            request
                .newBuilder()
                .apply {
                    tokenProvider()?.let {
                        addHeader("Authorization", "Bearer $it")
                    }
                }.build()

        return chain.proceed(newRequest)
    }
}
