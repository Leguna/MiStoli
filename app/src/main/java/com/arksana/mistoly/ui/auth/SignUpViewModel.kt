package com.arksana.mistoly.ui.auth

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arksana.mistoly.model.BaseResponse
import com.arksana.mistoly.model.UserModel
import com.arksana.mistoly.services.ApiService
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpViewModel(
    private val apiService: ApiService,
) : ViewModel() {

    fun register(
        name: String,
        email: String,
        password: String,
        callback: (isSuccess: Boolean, message: String) -> Unit,
    ) {
        viewModelScope.launch {
            val response =
                apiService.register(UserModel(name = name, email = email, password = password))
            response.enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>, response: Response<BaseResponse>,
                ) {
                    if (response.isSuccessful) {
                        callback(true, response.body()?.message ?: "")
                    } else {
                        val jObjError =
                            JSONObject(response.errorBody()!!.string()).getString("message")
                        callback(false, jObjError ?: "")
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    callback(false, "")
                }

            })
        }
    }
}