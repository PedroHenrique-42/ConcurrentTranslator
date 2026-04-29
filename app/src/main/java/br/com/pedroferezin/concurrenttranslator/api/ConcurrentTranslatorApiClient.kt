package br.com.pedroferezin.concurrenttranslator.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ConcurrentTranslatorApiClient {
    private const val BASE_URL = "https://deep-translate1.p.rapidapi.com/"

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
        GsonConverterFactory.create()
    ).build()

    val service: ConcurrentTranslatorApiService =
        retrofit.create(ConcurrentTranslatorApiService::class.java)
}