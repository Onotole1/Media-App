package ru.netology.mediaapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import ru.netology.mediaapp.model.Album

const val BASE_URL =
    "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface AlbumApi {
    @GET("album.json")
    suspend fun getAllTracks(): Album

    object AlbumApiService {
        val api: AlbumApi by lazy {
            retrofit.create()
        }
    }
}