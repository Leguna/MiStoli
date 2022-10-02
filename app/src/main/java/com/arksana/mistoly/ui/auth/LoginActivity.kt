package com.arksana.mistoly.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.arksana.mistoly.R
import com.arksana.mistoly.databinding.ActivityLoginBinding
import com.arksana.mistoly.model.UserModel
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import com.arksana.mistoly.ui.home.HomeActivity
import com.arksana.mistoly.utils.Validator
import com.arksana.mistoly.utils.keyboardHide
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "stoly_preferences")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userLogin: UserModel
    private lateinit var userPreference: UserPreference
    private lateinit var apiService: ApiService
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setContentView(binding.root)
        initialize()
        setView()
    }

    private fun initialize() {
        userPreference = UserPreference.getInstance(dataStore)
        lifecycleScope.launch(Dispatchers.Main) {
            userPreference.getUser().collect { userPref ->
                userLogin = userPref
                println("User: $userLogin")
                if (userLogin.token != "") {
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                } else {
                    apiService = ApiService(userLogin.token ?: "", context = applicationContext)
                    loginViewModel = LoginViewModel(userPreference, apiService)
                    loginViewModel.getUser()
                        .observe(this@LoginActivity) { userModel -> userLogin = userModel }
                }
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

        loginViewModel.login(binding.edLoginEmail.text.toString(),
            binding.edLoginPassword.text.toString(),
            callback = { isSuccess: Boolean, message: String ->
                binding.loadingView.group.visibility = View.GONE
                if (isSuccess) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        message.ifEmpty { getString(R.string.failed) },
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}