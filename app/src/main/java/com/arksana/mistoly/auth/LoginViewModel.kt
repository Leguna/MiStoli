package com.arksana.mistoly.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arksana.mistoly.model.UserModel
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPreference: UserPreference, private val apiService: ApiService
) : ViewModel() {

    private val user = MutableLiveData<UserModel>()

    fun getUser(): LiveData<UserModel> {
        return user
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (result: String?) -> Unit
    ) {
        user.value = UserModel(email = email, password = password)
        apiService.login(user.value!!, onSuccess = {
            viewModelScope.launch {
                userPreference.saveUser(it)
                user.postValue(it)
                onSuccess()
            }
        }, onFailure = {
            onFailure(it)
        })
    }
}