package com.forward.controllerspeed.ui.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.forward.controllerspeed.data.repository.LoginRepository
import com.forward.controllerspeed.data.Result

import com.forward.controllerspeed.R
import kotlinx.coroutines.*

class LoginViewModel(private val loginRepository: LoginRepository,
                     private val application: Application) : ViewModel() {

//    private val viewModelJob = Job()
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
//        viewModelScope(Dispatchers.IO + viewModelJob){
//
//        }
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) {
                loginRepository.login(username, password)
            }

            if (result is Result.Success) {
                    _loginResult.value =
                        LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
//                    saveOauthAccessToken(result.data.token, result.data.displayName)
                } else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    //Custom method to save Token
    private fun saveOauthAccessToken(token:String, user:String){
        val sharedPref: SharedPreferences = application.getSharedPreferences("credentials",
            Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("token",token)
        editor.putString("user",user)
        editor.apply()
    }
    override fun onCleared() {
        super.onCleared()
//        viewModelJob.cancel()
    }
}
