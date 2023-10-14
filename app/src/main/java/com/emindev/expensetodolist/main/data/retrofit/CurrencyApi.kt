package com.emindev.expensetodolist.main.data.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CurrencyApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: apikey 4RFL4OCD6s6Ds64bU70t6C:54jYm5oW59mlLzUhhe5yYA"
    )
    @GET("/economy/allCurrency")
    suspend fun getCurrency():Response<CurrencyResponseModel>

}