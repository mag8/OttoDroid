package com.example.ottov1.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import androidx.fragment.app.FragmentActivity

@Composable
fun MaterialTimePickerDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    initialHour: Int,
    initialMinute: Int,
    useKeyboardInput: Boolean = false
) {
    val activity = LocalContext.current.findActivity()
    
    LaunchedEffect(activity) {
        if (activity is FragmentActivity) {
            val picker = MaterialTimePicker.Builder()
                .setTitleText(title)
                .setHour(initialHour)
                .setMinute(initialMinute)
                .setInputMode(if (useKeyboardInput) MaterialTimePicker.INPUT_MODE_KEYBOARD else MaterialTimePicker.INPUT_MODE_CLOCK)
                .build()

            picker.addOnPositiveButtonClickListener {
                onConfirm(picker.hour, picker.minute)
            }

            picker.addOnNegativeButtonClickListener {
                onDismiss()
            }

            picker.addOnCancelListener {
                onDismiss()
            }

            picker.addOnDismissListener {
                onDismiss()
            }

            picker.show(activity.supportFragmentManager, "time_picker")
        } else {
            onDismiss()
        }
    }
}

private fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
} 