package com.arksana.mistoly.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.arksana.mistoly.R
import com.arksana.mistoly.databinding.ActivityHomeBinding
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import com.arksana.mistoly.ui.ViewModelFactory
import com.arksana.mistoly.ui.auth.LoginActivity
import com.arksana.mistoly.ui.auth.dataStore
import com.arksana.mistoly.ui.stoly.AddStoryActivity


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setContentView(binding.root)
        viewModelSetup()
        setListenerButton()
    }

    private fun viewModelSetup() {
        homeViewModel = ViewModelProvider(this,
            ViewModelFactory(
                ApiService(context = baseContext),
                UserPreference.getInstance(dataStore),
            ))[HomeViewModel::class.java]
        homeViewModel.getUserPref().observe(this) { user ->
            if (user?.token.isNullOrEmpty()) {
                startActivity(
                    Intent(this, LoginActivity::class.java),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle(),
                )
                finish()
            }
        }
    }

    private fun setListenerButton() {
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(applicationContext, AddStoryActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle(),
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                homeViewModel.logout()
                true
            }
            R.id.refresh -> {
                supportFragmentManager.findFragmentById(R.id.fragment_list)?.let {
                    (it as StolyFragment).refresh()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }
}