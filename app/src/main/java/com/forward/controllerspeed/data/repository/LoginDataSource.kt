package com.forward.controllerspeed.data.repository

import com.forward.controllerspeed.data.Result
import com.forward.controllerspeed.data.model.AuthenticationResponse
import com.forward.controllerspeed.data.model.LoggedInUser
import com.forward.controllerspeed.data.model.Login
import com.forward.controllerspeed.data.network.LoginService
import retrofit2.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Response<AuthenticationResponse> {
            return LoginService.makeLoginService().loginWithCredentials(Login(username,password))
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

