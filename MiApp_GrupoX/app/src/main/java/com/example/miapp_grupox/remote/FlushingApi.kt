package com.example.miapp_grupox.remote

import com.example.miapp_grupox.model.FlusingEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/** [RETROFIT] Microservicio ms-flushing (puerto 8083): CRUD completo. */
interface FlushingApi {
    @GET("api/flushing")
    suspend fun listar(): List<FlusingEntity>

    @POST("api/flushing")
    suspend fun crear(@Body request: FlushingRequest): FlusingEntity

    @PUT("api/flushing/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body request: FlushingRequest): FlusingEntity

    @DELETE("api/flushing/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
