package com.arksana.mistoly.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arksana.mistoly.model.BaseResponse
import com.arksana.mistoly.model.LoginResponse
import com.arksana.mistoly.model.UserModel
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val userPreference: UserPreference, private val apiService: ApiService,
) : ViewModel() {

    private val user = MutableLiveData<UserModel>()

    fun getUser(): LiveData<UserModel> {
        return user
    }

    fun login(
        email: String,
        password: String,
        callback: (isSuccess: Boolean, message: String) -> Unit,
    ) {
        user.value = UserModel(email = email, password = password)
        apiService.login(user.value!!).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>,
            ) {
                if (response.isSuccessful) {
                    viewModelScope.launch {
                        val userResponse = response.body()?.userModel ?: UserModel()
                        userPreference.saveUser(userResponse)
                        user.postValue(userResponse)
                        callback(true, response.body()?.message ?: "")
                    }
                } else {
                    val jObjError = JSONObject(response.errorBody()!!.string()).getString("message")
                    callback(false, jObjError ?: "")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(false, "")
            }
        })
    }
}