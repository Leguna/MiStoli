package com.arksana.mistoly.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arksana.mistoly.model.Story

@Dao
interface StoryDao {

    @Query("SELECT * FROM stories")
    fun getAllStory(): PagingSource<Int, Story>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: List<Story>)

    @Query("DELETE FROM stories")
    suspend fun deleteAll()

}