package com.arksana.mistoly.services

import android.content.Context
import com.arksana.mistoly.model.*
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import kotlinx.coroutines.Job
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiService(var token: String = "", var context: Context) {
    private var storyAPI: StoryApiInterface

    init {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        clientBuilder.addInterceptor(loggingInterceptor)
        clientBuilder.addInterceptor(ChuckerInterceptor.Builder(context = context)
            .collector(ChuckerCollector(context)).maxContentLength(250000L)
            .redactHeaders(emptySet()).alwaysReadResponseBody(false).build())
        val retrofit = Retrofit.Builder().baseUrl("https://story-api.dicoding.dev/v1/")
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        storyAPI = retrofit.create(StoryApiInterface::class.java)
    }

    fun login(
        user: UserModel,
    ): Call<LoginResponse> {
        return storyAPI.login(user)
    }

    fun register(userModel: UserModel): Call<BaseResponse> {
        return storyAPI.register(userModel)
    }

    fun addNewStory(
        description: RequestBody,
        file: MultipartBody.Part,
    ): Call<BaseResponse> {
        return storyAPI.addNewStory(description, file, "Bearer $token")
    }

    fun getAllStories(
        token: String? = "", page: Int? = null, size: Int? = null, location: Int = 0,
    ): Call<GetAllStoryResponse> {
        return storyAPI.getAllStories("Bearer $token", page, size, location)
    }


}