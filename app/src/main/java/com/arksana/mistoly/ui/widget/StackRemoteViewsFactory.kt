package com.arksana.mistoly.ui.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.arksana.mistoly.R
import com.arksana.mistoly.model.Story
import com.google.gson.Gson

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()

    private val mItemJson = """
           [{
            "id": "story-pNbQxnAg8WFOpt3W",
            "name": "fyn",
            "description": "hello there",
            "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664698695230_SpIuR_6L.jpg",
            "createdAt": "2022-10-02T08:18:15.231Z",
            "lat": 37.421997,
            "lon": -122.084
        },
        {
            "id": "story-Dc3u-A2f9QUVT9f-",
            "name": "Muhammad Abdusy Syukur",
            "description": "tes kamera",
            "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664698081087_mWR72iVe.jpg",
            "createdAt": "2022-10-02T08:08:01.088Z",
            "lat": -7.2848215,
            "lon": 110.104744
        },
        {
            "id": "story-uS_e6lTMksw89uuX",
            "name": "Muhammad Abdusy Syukur",
            "description": "tes kamera",
            "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664697989628_41zJdsu9.jpg",
            "createdAt": "2022-10-02T08:06:29.629Z",
            "lat": -7.3031516,
            "lon": 110.18794
        },
        {
            "id": "story-uIPrLXcdlBQBedcX",
            "name": "Naruto3",
            "description": "tes 3",
            "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664697744921_vgTwy47q.jpg",
            "createdAt": "2022-10-02T08:02:24.922Z",
            "lat": 38.33,
            "lon": 32.33
        },
        {
            "id": "story-SZQ2mVcn63_I0VWJ",
            "name": "Naruto3",
            "description": "tes 2",
            "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664697602865_DafH6G3i.jpg",
            "createdAt": "2022-10-02T08:00:02.866Z",
            "lat": 38.33,
            "lon": 32.33
        }]
    """.trimIndent()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.sample1))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources,
            R.drawable.sample2))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.sample3))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.sample4))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.sample5))
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val listStory = Gson().fromJson(mItemJson, Array<Story>::class.java).toList()

        val extras = bundleOf(
            StoryImageStack.EXTRA_ITEM to Gson().toJson(
                listStory[position]
            )
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}