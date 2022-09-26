package com.arksana.mistoly.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arksana.mistoly.model.BaseResponse
import com.arksana.mistoly.model.UserModel
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel(
    private val userPreference: UserPreference, private val apiService: ApiService
) : ViewModel() {

    private val user = MutableLiveData<UserModel>()

    fun register(
        name: String,
        email: String,
        password: String,
        callback: (isSuccess: Boolean, message: String) -> Unit
    ) {
        viewModelScope.launch {
            val response = apiService.register(UserModel(name, email, password))
            response.enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>, response: Response<BaseResponse>
                ) {
                    if (response.isSuccessful) {
                        callback(true, response.body()?.message ?: "")
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    callback(false, t.message ?: "")
                }

            })
        }
    }
}