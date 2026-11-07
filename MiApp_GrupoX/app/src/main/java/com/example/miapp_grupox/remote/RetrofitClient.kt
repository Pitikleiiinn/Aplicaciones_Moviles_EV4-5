package com.example.miapp_grupox.remote

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** [RETROFIT] Fábrica de clientes HTTP: una instancia por servicio (cada microservicio usa un puerto distinto). */
object RetrofitClient {

    /** Cliente OkHttp compartido: tiempos de espera y log de peticiones (visible en Logcat) */
    private val http: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()
    }

    private inline fun <reified T> crear(baseUrl: String): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(http)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(T::class.java)

    val lineasApi: LineasApi by lazy { crear(ApiConfig.URL_TEMPERATURAS) }
    val flushingApi: FlushingApi by lazy { crear(ApiConfig.URL_FLUSHING) }
    val usuariosApi: UsuariosApi by lazy { crear(ApiConfig.URL_USUARIOS) }
    val climaApi: ClimaApi by lazy { crear(ApiConfig.URL_CLIMA) }
}
