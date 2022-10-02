package com.arksana.mistoly.ui.home

import androidx.lifecycle.*
import com.arksana.mistoly.model.GetAllStoryResponse
import com.arksana.mistoly.model.Story
import com.arksana.mistoly.model.UserModel
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call

class HomeViewModel(
    private var storyRepo: ApiService,
    private val pref: UserPreference,
) :
    ViewModel() {

    private val _user = MutableLiveData<UserModel>()

    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    fun getUserPref(): LiveData<UserModel> {
        pref.getUser().asLiveData().observeForever {
            _user.value = it
        }
        return _user
    }

    fun getStories(callback: (isError: Boolean, message: String) -> Unit) {
        storyRepo.getAllStories(_user.value?.token ?: "").let {
            it.enqueue(object : retrofit2.Callback<GetAllStoryResponse> {
                override fun onResponse(
                    call: Call<GetAllStoryResponse>,
                    response: retrofit2.Response<GetAllStoryResponse>,
                ) {
                    if (response.isSuccessful) {
                        _stories.value = response.body()?.listStory ?: ArrayList()
                        callback(false, "")
                    } else {
                        _stories.value = ArrayList()
                        callback(true, response.message())
                    }
                }

                override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                    _stories.value = ArrayList()
                    callback(true, "")
                }
            })
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.removeUser()
        }
    }

}
