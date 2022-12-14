package com.arksana.mistoly.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arksana.mistoly.R
import com.arksana.mistoly.databinding.FragmentItemBinding
import com.arksana.mistoly.model.Story
import com.arksana.mistoly.ui.stoly.DetailStoryActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson

class MyStoliRecyclerViewAdapter() :
    PagingDataAdapter<Story, MyStoliRecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            if (position == itemCount - 1) {
                (holder.card.layoutParams as MarginLayoutParams).bottomMargin =
                    mContext.resources.getDimension(R.dimen.normal).toInt()
            }
        }
    }

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image: ImageView = binding.ivItemPhoto
        private val name: TextView = binding.tvItemName
        val card: CardView = binding.card

        override fun toString(): String {
            return super.toString() + " '" + name.text + "'"
        }

        @SuppressLint("SetTextI18n")
        fun bind(story: Story) {
            name.text = story.name

            Glide.with(mContext)
                .load(story.photoUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(image)

            itemView.setOnClickListener {

                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra("story", Gson().toJson(story))

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(image, "profile"),
                        Pair(name, "name"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}