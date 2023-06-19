package com.emindev.expensetodolist.main.data.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLContext
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager


object RetrofitInstance {
    val okHttpClient = OkHttpClient.Builder()
        .sslSocketFactory(SSLContext.getInstance("SSL").apply { init(null,TrustAllCerts.trustManagerArray(),null) }.socketFactory, TrustAllCerts())
        .hostnameVerifier { _, _ -> true }
        .build()

    val api :CurrencyApi by lazy {
        Retrofit.Builder()
            .baseUrl(RetrofitConstants.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)

    }
}


class TrustAllCerts : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

    companion object {
        fun trustManagerArray() = arrayOf(TrustAllCerts())
    }
}