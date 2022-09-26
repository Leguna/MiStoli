package com.arksana.mistoly.services

import com.arksana.mistoly.model.BaseResponse
import com.arksana.mistoly.model.GetAllStoryResponse
import com.arksana.mistoly.model.LoginResponse
import com.arksana.mistoly.model.UserModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiService(var token: String = "") {
    private var storyAPI: DicodingStoryApi

    init {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        clientBuilder.addInterceptor(loggingInterceptor)

        val retrofit = Retrofit.Builder().baseUrl("https://story-api.dicoding.dev/v1/")
            .client(clientBuilder.build()).addConverterFactory(
                GsonConverterFactory.create()
            ).build()

        storyAPI = retrofit.create(DicodingStoryApi::class.java)
    }

    fun login(
        user: UserModel,
        onSuccess: (UserModel) -> Unit,
        onFailure: (String) -> Unit
    ) {
        storyAPI.login(user).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (!response.isSuccessful) {
                    val gson = Gson()
                    val type = object : TypeToken<BaseResponse>() {}.type
                    val errorResponse: BaseResponse? =
                        gson.fromJson(response.errorBody()!!.charStream(), type)
                    onFailure(errorResponse?.message ?: "Unknown error")
                    return
                }

                response.body()?.let {
                    val userModel = UserModel(
                        it.userModel?.userId,
                        it.userModel?.name,
                        it.userModel?.token,
                        user.email,
                        user.email
                    )
                    onSuccess(userModel)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onFailure(t.message ?: "Unknown error")
            }
        })
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
        token: String? = "", page: Int? = null, size: Int? = null, location: Int = 0
    ): GetAllStoryResponse {
        return storyAPI.getAllStories("Bearer $token", page, size, location)
    }
}