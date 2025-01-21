package com.manuflores.codechallenge2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(
    private val loginValidator: ILoginValidator
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(loginValidator) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}