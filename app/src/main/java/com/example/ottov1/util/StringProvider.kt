package com.example.ottov1.util

import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
} 