package com.arksana.mistoly.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arksana.mistoly.data.StoryRepository
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import com.arksana.mistoly.ui.auth.LoginViewModel
import com.arksana.mistoly.ui.auth.SignUpViewModel
import com.arksana.mistoly.ui.home.HomeViewModel
import com.arksana.mistoly.ui.stoly.AddStoryViewModel
import com.arksana.mistoly.ui.stoly.DetailStoryViewModel

class ViewModelFactory(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository,
    private val apiService: ApiService,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(storyRepository, pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(apiService, pref) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(apiService) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(apiService, pref) as T
            }
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}