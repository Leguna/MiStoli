package com.arksana.mistoly.services

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.arksana.mistoly.model.BaseResponse
import com.arksana.mistoly.model.GetAllStoryResponse
import com.arksana.mistoly.model.LoginResponse
import com.arksana.mistoly.model.UserModel
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiService(var context: Context? = null, var token: String = "") {

    companion object {
        private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ApiService? = null

        fun getInstance(context: Context): ApiService {
            return INSTANCE ?: synchronized(this) {
                val instance = ApiService(context, "")
                INSTANCE = instance
                instance
            }
        }
    }

    private var storyAPI: StoryApiInterface

    init {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        clientBuilder.addInterceptor(loggingInterceptor)
        if (context != null) clientBuilder.addInterceptor(ChuckerInterceptor.Builder(context = context!!)
            .collector(ChuckerCollector(context!!)).maxContentLength(250000L)
            .redactHeaders(emptySet()).alwaysReadResponseBody(false).build())
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create()).build()
        Log.d("CheckRetrofit", "retrofit: ${retrofit.hashCode()}")

        storyAPI = retrofit.create(StoryApiInterface::class.java)
    }

    fun login(userModel: UserModel): Call<LoginResponse> {
        return storyAPI.login(UserModel(email = userModel.email, password = userModel.password))
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

    suspend fun getAllStories(
        token: String? = "", page: Int? = null, size: Int? = null, location: Int = 0,
    ): GetAllStoryResponse {
        return storyAPI.getAllStories("Bearer $token", page, size, location)
    }


}