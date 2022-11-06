package com.arksana.mistoly.ui.stoly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arksana.mistoly.model.BaseResponse
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private var storyRepo: ApiService,
    pref: UserPreference,
) :
    ViewModel() {
    private var token: String? = null

    init {
        pref.getUser().asLiveData().observeForever {
            token = it.token
        }
    }

    fun addNewStory(
        description: RequestBody,
        file: MultipartBody.Part,
        callback: (Boolean) -> Unit,
    ) {
        storyRepo.token = token.toString()
        storyRepo.addNewStory(description, file)
            .enqueue(object : retrofit2.Callback<BaseResponse> {
                override fun onResponse(
                    call: retrofit2.Call<BaseResponse>,
                    response: retrofit2.Response<BaseResponse>,
                ) {
                    if (response.isSuccessful) {
                        callback(true)
                    } else {
                        callback(false)
                    }
                }

                override fun onFailure(call: retrofit2.Call<BaseResponse>, t: Throwable) {
                    callback(false)
                }
            })
    }
}
