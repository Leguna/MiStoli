package com.arksana.mistoly.stoly.placeholder

import com.arksana.mistoly.model.Story

object PlaceholderContent {

    val ITEMS: MutableList<Story> = ArrayList()
    private val ITEM_MAP: MutableMap<String, Story> = HashMap()

    private const val COUNT = 25

    init {
        for (i in 1..COUNT) {
            addItem(createStoly(i))
        }
    }

    private fun addItem(item: Story) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    private fun createStoly(position: Int): Story {
        return Story(
            position.toString(),
            "Item $position",
            makeDetails(position),
            "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png"
        )
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

}