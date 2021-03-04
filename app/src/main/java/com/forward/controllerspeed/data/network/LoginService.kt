package com.forward.controllerspeed.data.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object LoginService {

    private const val BASE_URL = "http://applasbambas.gpsgoldcar.com/"

    fun makeLoginService(): LoginApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(LoginApi::class.java)
    }
}