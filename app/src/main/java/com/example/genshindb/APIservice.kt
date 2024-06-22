package com.example.genshindb

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIservice {
    @GET
    suspend fun getCharacters(@Url url:String):Response<Collection<CharactersResponse>>
    @GET
    suspend fun getWeapons(@Url url:String):Response<Collection<WeaponsResponse>>
    @GET
    suspend fun getArtifacts(@Url url:String):Response<Collection<ArtifactsResponse>>
}