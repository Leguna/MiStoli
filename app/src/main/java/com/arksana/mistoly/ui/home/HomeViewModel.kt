package com.arksana.mistoly.ui.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arksana.mistoly.data.StoryRepository
import com.arksana.mistoly.model.Story
import com.arksana.mistoly.model.UserModel
import com.arksana.mistoly.model.UserPreference
import kotlinx.coroutines.launch

class HomeViewModel(
    private var storyRepo: StoryRepository,
    private val pref: UserPreference,
) :
    ViewModel() {

    private val _user = MutableLiveData<UserModel>()

    private val _stories = MutableLiveData<PagingData<Story>>()

    fun getAllStories(): LiveData<PagingData<Story>> {
        return storyRepo.getStory(_user.value?.token.toString()).cachedIn(viewModelScope)
    }

    fun getUserPref(): LiveData<UserModel> {
        pref.getUser().asLiveData().observeForever {
            _user.value = it
        }
        return _user
    }


    fun logout() {
        viewModelScope.launch {
            pref.removeUser()
        }
    }

}
