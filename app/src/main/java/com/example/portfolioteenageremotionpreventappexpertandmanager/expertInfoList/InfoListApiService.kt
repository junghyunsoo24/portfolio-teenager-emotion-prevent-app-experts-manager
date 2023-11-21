package com.example.portfolioteenageremotionpreventappexpertandmanager.expertInfoList

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private val mHttpLoggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY) // BASIC) // check constants

//private val mOkHttpClient = OkHttpClient
//    .Builder()
//    .addInterceptor(mHttpLoggingInterceptor)
//    .build()

private val moshi = Moshi.Builder()//더 편하게 하기 위해서 사용
    .add(KotlinJsonAdapterFactory())
    .build()

private fun createRetrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl)
        .build()
}

interface InfoListApiService {
    @Headers("Content-Type: application/json")

    @POST("/v1/auth/expert/info")
    suspend fun sendsMessage(@Body message: InfoListData): Response<InfoListDataResponse>

}


object InfoListApi {
    fun retrofitService(baseUrl: String): InfoListApiService {
        val retrofit = createRetrofit(baseUrl)
        return retrofit.create(InfoListApiService::class.java)
    }
}