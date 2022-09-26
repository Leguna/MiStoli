package com.arksana.mistoly.model

import com.google.gson.annotations.SerializedName

data class Story(
    @SerializedName("id") var id: String = "",
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("photoUrl") var photoUrl: String? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("lat") var lat: Double? = null,
    @SerializedName("lon") var lon: Double? = null
)