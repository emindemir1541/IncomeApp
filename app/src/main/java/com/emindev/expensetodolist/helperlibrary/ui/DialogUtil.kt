package com.emindev.expensetodolist.helperlibrary.ui

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.alertDialogClassic(title: String, content: String,cancelable: Boolean = true) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(content)
        .setCancelable(cancelable)
        .show()
}

fun Context.alertDialogClassic(title: String, content: String, buttonString: String, buttonListener: () -> Unit , cancelable: Boolean = true) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(content)
        .setPositiveButton(buttonString) { _, _ ->
            buttonListener()
        }
        .setCancelable(cancelable)
        .show()
}

fun Context.alertDialogClassic(title: String, content: String, positiveButtonTitle: String, negativeButtonTitle: String, positiveButton: () -> Unit = {}, negativeButton: () -> Unit = {}, cancelable: Boolean = true) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(content)
        .setPositiveButton(positiveButtonTitle) { _, _ ->
            positiveButton()
        }
        .setNegativeButton(negativeButtonTitle) { _, _ ->
            negativeButton()
        }
        .setCancelable(cancelable)
        .show()
}
