package com.aspiring_creators.aichopaicho

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit



object CurrencyUtils {
    private const val PREFS_NAME = "app_preferences"
    private const val KEY_CURRENCY_CODE = "currency_code"
    private const val DEFAULT_CURRENCY = "NPR"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getCurrencyCode(context: Context): String {
        return getPrefs(context).getString(KEY_CURRENCY_CODE, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY
    }

    fun setCurrencyCode(context: Context, currencyCode: String) {
        getPrefs(context).edit {
            putString(KEY_CURRENCY_CODE, currencyCode)
        }
    }

    fun getCurrencySymbol(context: Context): String {
        val currencyCode = getCurrencyCode(context)
        return try {
            java.util.Currency.getInstance(currencyCode).symbol
        } catch (e: Exception) {
            "$"
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatAmount(context: Context, amount: Double): String {
        val symbol = getCurrencySymbol(context)
        return "$symbol${String.format("%.2f", amount)}"
    }
}