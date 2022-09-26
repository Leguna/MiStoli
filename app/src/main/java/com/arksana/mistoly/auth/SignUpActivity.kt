package com.arksana.mistoly.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import com.arksana.mistoly.R
import com.arksana.mistoly.databinding.ActivitySignupBinding
import com.arksana.mistoly.model.BaseResponse
import com.arksana.mistoly.model.UserModel
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import com.arksana.mistoly.utils.Validator
import com.arksana.mistoly.utils.keyboardHide
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                ApiService().register(UserModel(name = name, email = email, password = password))
                    .enqueue(object : Callback<BaseResponse> {
                        override fun onResponse(
                            call: Call<BaseResponse>, response: Response<BaseResponse>
                        ) {
                            if (response.isSuccessful) {
                                binding.loadingView.group.visibility = View.GONE
                                Toast.makeText(
                                    applicationContext,
                                    response.body()?.message ?: "",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                val jsonObj =
                                    JSONObject(response.errorBody()!!.charStream().readText())
                                Toast.makeText(
                                    applicationContext,
                                    jsonObj.getString("message") ?: "",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            binding.loadingView.group.visibility = View.GONE
                        }

                        override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                            binding.loadingView.group.visibility = View.GONE
                        }

                    })
            }
        }
    }
}