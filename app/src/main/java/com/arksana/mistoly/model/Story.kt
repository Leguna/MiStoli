package com.arksana.mistoly.model

import com.google.gson.annotations.SerializedName

data class Story(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("photoUrl") var photoUrl: String = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fid.m.wikipedia.org%2Fwiki%2FBerkas%3ANo_image_available.svg&psig=AOvVaw2nym5LAGLQwMjcu3l4Juym&ust=1664625904820000&source=images&cd=vfe&ved=0CAwQjRxqFwoTCLDqle28vPoCFQAAAAAdAAAAABAD",
    @SerializedName("createdAt") var createdAt: String = "",
    @SerializedName("lat") var lat: Double = 0.0,
    @SerializedName("lon") var lon: Double = 0.0,
)