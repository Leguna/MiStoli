package com.arksana.mistoly.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stories")
data class Story(
    @PrimaryKey @field:SerializedName("id") val id: String = "",
    @field:SerializedName("name") val name: String = "",
    @field:SerializedName("description") var description: String = "",
    @field:SerializedName("photoUrl") var photoUrl: String = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fid.m.wikipedia.org%2Fwiki%2FBerkas%3ANo_image_available.svg&psig=AOvVaw2nym5LAGLQwMjcu3l4Juym&ust=1664625904820000&source=images&cd=vfe&ved=0CAwQjRxqFwoTCLDqle28vPoCFQAAAAAdAAAAABAD",
    @field:SerializedName("createdAt") var createdAt: String = "",
    @field:SerializedName("lat") var lat: Double = 0.0,
    @field:SerializedName("lon") var lon: Double = 0.0,
)