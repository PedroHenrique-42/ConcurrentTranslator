package br.com.pedroferezin.concurrenttranslator.api

import br.com.pedroferezin.concurrenttranslator.domain.LanguagesList
import br.com.pedroferezin.concurrenttranslator.domain.TranslationResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ConcurrentTranslatorApiService {
    @Headers(
        "x-rapidapi-host: deep-translate1.p.rapidapi.com",
        "x-rapidapi-key: f8d372a6f0msh14f81a14120937dp19f03bjsn6526a833dcb4"
    )
    @GET("language/translate/v2/languages")
    fun fetchLanguagues(): Call<LanguagesList>

    @Headers(
        "x-rapidapi-host: deep-translate1.p.rapidapi.com",
        "x-rapidapi-key: f8d372a6f0msh14f81a14120937dp19f03bjsn6526a833dcb4"
    )
    @POST("language/translate/v2")
    fun translate(
        @Query("q") text: String,
        @Query("source") originLanguague: String,
        @Query("target") destinyLanguague: String
    ): Call<TranslationResult>
}