package com.forward.controllerspeed.data.repository

import android.util.Log
import com.forward.controllerspeed.data.Result
import com.forward.controllerspeed.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val response = dataSource.login(username, password)
        if (response.isSuccessful) {
            Log.d("cuack","response: ${response.body()!!}")
            val result = response.body()!!
            val userLogged  = LoggedInUser(java.util.UUID.randomUUID().toString(),
                "UserTest", result.token)
            setLoggedInUser(userLogged)

            return Result.Success(userLogged)
        }
        Log.d("cuack","error :v")

        return Result.Error(IOException("Error logging in"))
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
