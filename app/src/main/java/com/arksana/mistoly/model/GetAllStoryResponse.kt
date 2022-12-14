package com.arksana.mistoly.model


import com.google.gson.annotations.SerializedName

data class GetAllStoryResponse(
    @SerializedName("error")
    val error: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("listStory")
    val listStory: List<Story> = listOf(),
)