package com.arksana.mistoly.services

import com.arksana.mistoly.model.BaseResponse
import com.arksana.mistoly.model.GetAllStoryResponse
import com.arksana.mistoly.model.LoginResponse
import com.arksana.mistoly.model.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryApiInterface {

    @POST("register")
    fun register(@Body user: UserModel): Call<BaseResponse>

    @POST("login")
    fun login(@Body user: UserModel): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String,
    ): Call<BaseResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0,
    ): GetAllStoryResponse

}
