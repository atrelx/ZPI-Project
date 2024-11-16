package com.example.amoz.helpers

import android.content.Context
import android.content.SharedPreferences


class TokenManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String?, refreshToken: String?) {
        with(sharedPreferences.edit()) {
            if (accessToken != null) {
                putString("ACCESS_TOKEN", accessToken)
            }
            if (refreshToken != null) {
                putString("REFRESH_TOKEN", refreshToken)
            }
            apply()
        }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("ACCESS_TOKEN", null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("REFRESH_TOKEN", null)
    }

    fun clearTokens() {
        with(sharedPreferences.edit()) {
            remove("ACCESS_TOKEN")
            remove("REFRESH_TOKEN")
            apply()
        }
    }
}