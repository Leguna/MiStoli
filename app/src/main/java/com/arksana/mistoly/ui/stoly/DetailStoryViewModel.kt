package com.arksana.mistoly.ui.stoly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arksana.mistoly.model.UserPreference

class DetailStoryViewModel(
    pref: UserPreference,
) :
    ViewModel() {
    private var token: String? = null

    init {
        pref.getUser().asLiveData().observeForever {
            token = it.token
        }
    }

}
