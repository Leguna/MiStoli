package com.arksana.mistoly.ui.stoly

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.arksana.mistoly.R
import com.arksana.mistoly.databinding.ActivityDetailStoryBinding
import com.arksana.mistoly.model.Story
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import com.arksana.mistoly.ui.ViewModelFactory
import com.arksana.mistoly.ui.auth.dataStore
import com.arksana.mistoly.utils.parseTimeAgo
import com.bumptech.glide.Glide
import com.google.gson.Gson

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var detailStoryViewModel: DetailStoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelSetup()
        setupView()
        setupAction()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        supportActionBar?.hide()
        val story = intent.getStringExtra("story")
        Gson().fromJson(story, Story::class.java).let {
            binding.tvDetailName.text = it.name
            binding.tvDetailDate.text =
                "${parseTimeAgo(it.createdAt)}"
            binding.tvDetailDescription.text = it.description
            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.ivDetailPhoto)
        }
    }

    private fun setupAction() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun viewModelSetup() {
        detailStoryViewModel = ViewModelProvider(this,
            ViewModelFactory(
                ApiService(context = baseContext),
                UserPreference.getInstance(dataStore),
            ))[DetailStoryViewModel::class.java]
    }


}