package com.example.gardeningcsisapp.ui.authentication

import android.content.Context

class AuthRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String){
        prefs.edit().putString("token", token).apply()
    }

    fun getToken(): String?{
        return prefs.getString("token", null)
    }
}