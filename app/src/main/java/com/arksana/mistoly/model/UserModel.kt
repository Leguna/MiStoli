package com.arksana.mistoly.model


import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
)