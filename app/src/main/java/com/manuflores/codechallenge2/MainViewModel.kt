package com.manuflores.codechallenge2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val loginValidator: ILoginValidator
) : ViewModel() {

    private val _uiState: MutableLiveData<UIState> = MutableLiveData(UIState.InitialUIState)
    val uiState: LiveData<UIState> = _uiState

    fun validate(email: String, password: String) {
        viewModelScope.launch {
            when {
                email.isBlank() && password.isBlank() -> {
                    _uiState.value = UIState.EmptyFormErrorState(
                        emailError = "Email must be filled",
                        passwordError = "Password must be filled"
                    )
                }

                !loginValidator.validateEmail(email) -> {
                    _uiState.value = UIState.InvalidFieldState(
                        type = UIState.FieldType.EMAIL,
                        error = "Invalid email address"
                    )
                }

                email.isBlank() -> {
                    _uiState.value = UIState.InvalidFieldState(
                        type = UIState.FieldType.EMAIL,
                        error = "Email must be filled"
                    )
                }

                password.isBlank() || !loginValidator.validatePassword(password) -> {
                    _uiState.value = UIState.InvalidFieldState(
                        type = UIState.FieldType.PASSWORD,
                        error = "Password must be filled"
                    )
                }

                loginValidator.validateEmail(email) && loginValidator.validatePassword(password) -> {
                    _uiState.value = UIState.SuccessUIState
                }

                else -> {
                    _uiState.value = UIState.InvalidCredentials("Invalid credentials")
                }
            }
        }
    }
}

sealed interface UIState {
    data object InitialUIState : UIState

    data object SuccessUIState : UIState

    data class InvalidFieldState(val type: FieldType, val error: String) : UIState
    data class EmptyFormErrorState(val emailError: String, val passwordError: String) : UIState
    data class InvalidCredentials(val error: String) : UIState

    enum class FieldType {
        EMAIL, PASSWORD
    }
}

