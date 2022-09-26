package com.arksana.mistoly.stoly

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arksana.mistoly.databinding.FragmentItemBinding
import com.arksana.mistoly.model.Story
import com.bumptech.glide.Glide

class MyStoliRecyclerViewAdapter(
    private val values: List<Story>
) : RecyclerView.Adapter<MyStoliRecyclerViewAdapter.ViewHolder>() {
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
        val item = values[position]
        holder.name.text = item.name
        holder.desc.text = item.description
        Glide.with(mContext)
            .load(item.photoUrl)
            .into(holder.image)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.ivItemPhoto
        val name: TextView = binding.tvItemName
        val desc: TextView = binding.tvItemDesc

        override fun toString(): String {
            return super.toString() + " '" + name.text + "'"
        }
    }

}