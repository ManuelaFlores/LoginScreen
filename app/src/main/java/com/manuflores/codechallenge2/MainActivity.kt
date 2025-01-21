package com.manuflores.codechallenge2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.manuflores.codechallenge2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewmodel by lazy {
        val loginValidator = LoginValidator()
        val mainViewModelFactory = MainViewModelFactory(loginValidator)
        ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        observeUiData()
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            loginButton.setOnClickListener {
                viewmodel.validate(
                    email = emailTextInputEditText.text.toString(),
                    password = passwordTextInputEditText.text.toString()
                )
            }
        }
    }

    private fun observeUiData() {
        viewmodel.uiState.observe(this) { state ->
            when (state) {
                is UIState.InitialUIState -> showInitialUI()
                is UIState.InvalidCredentials -> showToast(message = state.error)
                is UIState.SuccessUIState -> showSuccessUI()
                is UIState.InvalidFieldState -> showErrorUI(state.type, state.error)
                is UIState.EmptyFormErrorState -> setFormError(state.emailError, state.passwordError)
            }
        }
    }

    private fun showInitialUI() {
        binding.apply {
            emailTextInputLayout.error = ""
            passwordTextInputLayout.error = ""
        }
    }

    private fun showSuccessUI() {
        showToast("Welcome to the app")
        showInitialUI()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun showErrorUI(fieldType: UIState.FieldType, error: String) {
        binding.apply {
            when (fieldType) {
                UIState.FieldType.EMAIL -> emailTextInputLayout.error = error
                UIState.FieldType.PASSWORD -> passwordTextInputLayout.error = error
            }
        }
    }

    private fun setFormError(emailError: String, passwordError: String) {
        binding.apply {
            emailTextInputLayout.error = emailError
            passwordTextInputLayout.error = passwordError
        }
    }

    private fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}