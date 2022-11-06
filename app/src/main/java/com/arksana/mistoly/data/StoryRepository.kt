package com.arksana.mistoly.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.arksana.mistoly.db.StoryDatabase
import com.arksana.mistoly.model.BaseResponse
import com.arksana.mistoly.model.Story
import com.arksana.mistoly.services.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
) {
    fun getStory(token: String = ""): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class) return Pager(config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }).liveData
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(context: Context): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryRepository(StoryDatabase.getDatabase(context), ApiService())
                INSTANCE = instance
                instance
            }
        }
    }
}
