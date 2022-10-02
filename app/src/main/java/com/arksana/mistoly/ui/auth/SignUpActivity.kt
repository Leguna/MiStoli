package com.arksana.mistoly.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import com.arksana.mistoly.R
import com.arksana.mistoly.databinding.ActivitySignupBinding
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import com.arksana.mistoly.utils.Validator
import com.arksana.mistoly.utils.keyboardHide
import com.bumptech.glide.Glide

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var userPreference: UserPreference
    private lateinit var apiService: ApiService
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        setContentView(binding.root)
        setCallbacks()
        setView()
    }

    private fun setView() {
        Glide.with(this)
            .load(R.drawable.sign_up_form)
            .into(binding.ivSignIn)
    }

    private fun setCallbacks() {
        binding.btnSignup.setOnClickListener { onRegisterButtonClick() }
        binding.edRegisterEmail.validator = { Validator.email(resources, it) }
        binding.edRegisterPassword.afterTextChanged = {
            binding.edRegisterPasswordRepeat.validateText()
        }
        binding.edRegisterPasswordRepeat.validator = {
            if (it != binding.edRegisterPassword.text.toString()) "Password ${getString(R.string.notMatch)}"
            else ""
        }
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun onRegisterButtonClick() {
        keyboardHide()
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        with(binding) {
            edRegisterEmail.validateText()
            edRegisterName.validateText()
            edRegisterPassword.validateText()
            edRegisterPasswordRepeat.validateText()
            val isValid =
                edRegisterEmail.isNotEmpty() && edRegisterName.isNotEmpty() && edRegisterPassword.isNotEmpty() && edRegisterPasswordRepeat.isNotEmpty()
            if (isValid) {
                binding.loadingView.group.visibility = View.VISIBLE
                apiService =
                    ApiService(context = applicationContext)
                signUpViewModel = SignUpViewModel(apiService)
                signUpViewModel.register(
                    name = name,
                    email = email,
                    password = password,
                    callback = { isSuccess, message ->
                        run {
                            if (isSuccess) {
                                binding.loadingView.group.visibility = View.GONE
                                Toast.makeText(
                                    applicationContext,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    message.ifEmpty { getString(R.string.failed) },
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            binding.loadingView.group.visibility = View.GONE
                        }
                    })

            }
        }
    }
}