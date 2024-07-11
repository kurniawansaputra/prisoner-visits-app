package com.myappkunjungan.pref

import android.content.Context
import android.content.SharedPreferences

class UserPreference(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val SESSION_TOKEN = "session_token"
    }

    fun saveSessionToken(token: String) {
        sharedPreferences.edit().putString(SESSION_TOKEN, token).apply()
    }

    fun getSessionToken(): String? {
        return sharedPreferences.getString(SESSION_TOKEN, null)
    }

    fun clearSession() {
        sharedPreferences.edit().remove(SESSION_TOKEN).apply()
    }
}