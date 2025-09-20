package com.aspiring_creators.aichopaicho

import android.content.Context
import androidx.core.content.edit

object CurrencyUtils {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_CURRENCY = "currency_code"

    fun getCurrencyCode(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CURRENCY, "NPR") ?: "NPR"  // Default is NPR
    }

    fun setCurrencyCode(context: Context, code: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_CURRENCY, code) }
    }
}
