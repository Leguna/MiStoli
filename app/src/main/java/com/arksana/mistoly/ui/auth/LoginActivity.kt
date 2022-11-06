package com.arksana.mistoly.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.arksana.mistoly.R
import com.arksana.mistoly.data.StoryRepository
import com.arksana.mistoly.databinding.ActivityLoginBinding
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import com.arksana.mistoly.ui.ViewModelFactory
import com.arksana.mistoly.ui.home.HomeActivity
import com.arksana.mistoly.utils.Validator
import com.arksana.mistoly.utils.keyboardHide
import com.bumptech.glide.Glide


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "stoly_preferences")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initialize()
        setView()
    }

    private fun initialize() {
        loginViewModel = ViewModelProvider(this,
            ViewModelFactory(UserPreference.getInstance(dataStore),
                StoryRepository.getInstance(this),
                ApiService.getInstance(this)))[LoginViewModel::class.java]
        loginViewModel.getUser().observe(this) { user ->
            if (!user?.token.isNullOrEmpty()) {
                startActivity(
                    Intent(this, HomeActivity::class.java),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle(),
                )
                finish()
            }else{
                loginViewModel.setEmail(binding.edLoginEmail.text.toString())
                loginViewModel.setPassword(binding.edLoginPassword.text.toString())
            }
        }
    }

    private fun setView() {
        binding.btnLogin.setOnClickListener { login() }
        binding.registerText.setOnClickListener { goToRegisterPage() }
        binding.edLoginEmail.validator = { Validator.email(resources, it) }
        Glide.with(this).load(R.drawable.welcome_sign).into(binding.ivSignIn)
    }

    private fun goToRegisterPage() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
    }

    private fun login() {
        keyboardHide()
        binding.edLoginEmail.validateText()
        binding.edLoginPassword.validateText()

        binding.loadingView.group.visibility = View.VISIBLE
        loginViewModel.login(callback = { isSuccess: Boolean, message: String ->
            binding.loadingView.group.visibility = View.GONE
            if (isSuccess) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(applicationContext,
                    message.ifEmpty { getString(R.string.failed) },
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}