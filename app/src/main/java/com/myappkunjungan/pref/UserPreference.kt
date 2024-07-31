package com.myappkunjungan.pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.myappkunjungan.data.response.User

class UserPreference(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val SESSION_TOKEN = "session_token"
        private const val USER_KEY = "user_key"
    }

    private val gson = Gson()

    fun saveSessionToken(token: String) {
        sharedPreferences.edit().putString(SESSION_TOKEN, token).apply()
    }

    fun getSessionToken(): String? {
        return sharedPreferences.getString(SESSION_TOKEN, null)
    }

    fun clearSession() {
        sharedPreferences.edit().remove(SESSION_TOKEN).apply()
        sharedPreferences.edit().remove(USER_KEY).apply()
    }

    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString(USER_KEY, userJson).apply()
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(USER_KEY, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }
}
