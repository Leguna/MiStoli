package com.arksana.mistoly.ui.auth

import androidx.lifecycle.*
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
    private val apiService: ApiService,
    private val userPreference: UserPreference,
) : ViewModel() {

    private val _user = MutableLiveData<UserModel>()

    fun getUser(): LiveData<UserModel> {
        userPreference.getUser().asLiveData().observeForever {
            _user.value = it
        }
        return _user
    }

    fun setEmail(email: String) {
        _user.value?.email = email
    }

    fun setPassword(password: String) {
        _user.value?.password = password
    }

    fun login(
        callback: (isSuccess: Boolean, message: String) -> Unit,
    ) {
        _user.value = UserModel(_user.value?.email, _user.value?.password)
        apiService.login(_user.value!!).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>,
            ) {
                if (response.isSuccessful) {
                    viewModelScope.launch {
                        val userResponse = response.body()?.userModel ?: UserModel()
                        userPreference.saveUser(userResponse)
                        _user.postValue(userResponse)
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