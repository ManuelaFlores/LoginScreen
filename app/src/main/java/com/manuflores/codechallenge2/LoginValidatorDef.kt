package com.manuflores.codechallenge2

import android.util.Patterns

interface ILoginValidator {

    suspend fun validateEmail(email: String): Boolean
    suspend fun validatePassword(password: String): Boolean
}

class LoginValidator : ILoginValidator {

    override suspend fun validateEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && email == mockEmail
    }

    override suspend fun validatePassword(password: String): Boolean {
        return password.isNotBlank() && password == mockPassword
    }

    //Hardcoded credentials
    companion object {

        private const val mockEmail = "manu.flores@gmail.com"
        private const val mockPassword = "959777"
    }
}