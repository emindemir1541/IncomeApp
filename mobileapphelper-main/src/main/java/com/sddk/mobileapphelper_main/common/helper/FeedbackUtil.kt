package com.sddk.mobileapphelper_main.common.helper

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.sddk.mobileapphelper_feedback.common.helper.SystemInfo
import com.sddk.mobileapphelper_main.common.model.Comment
import com.sddk.mobileapphelper_main.common.model.Error
import com.sddk.mobileapphelper_main.common.model.Resource
import com.sddk.mobileapphelper_main.data.feedbackrepository.FeedBackRepository
import kotlin.system.exitProcess

object FeedbackUtil {

    @SuppressLint("HardwareIds")
    fun Context.startupProcess() {
        Helper.deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        SystemInfo.PACKAGE_NAME = packageName
        catchError()
    }

    private fun catchError() {
        Thread.setDefaultUncaughtExceptionHandler { _, paramThrowable ->
            setError("ErrorCatcher", java.lang.Exception(paramThrowable), "FeedbackUtil/catchError", description = paramThrowable.message)
            exitProcess(2)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun setError(title: String, exception: Exception, errorLocation: String, userMessage: String? = null, description: String? = null, contextForUserMessage: Context? = null): Exception {
        val error = Error(title, exception.localizedMessage ?: "", exception.cause.toString(), errorLocation, DateUtil.currentTime, description)
        FeedBackRepository().setError(error) {
            when (it) {
                is Resource.Success -> {}
                is Resource.Error -> {
                    addLog("Response", it.message, "Response post error", "FeedbackUtil/setError()")
                }
                is Resource.Loading -> {}
            }
        }
        Log.e("ERROR ${error.time}", "Message -->  ${error.localizedMessage} \n" +
                "Description -->  ${error.description.toString()} \n" +
                "Location -->  ${error.errorLocation} \n")

        if (userMessage != null && contextForUserMessage != null)
            Toast.makeText(contextForUserMessage, userMessage, Toast.LENGTH_SHORT).show()

        return exception

    }

    fun setComment(nameSurname: String, title: String, commentData: String) {
        val comment = Comment(nameSurname, title, commentData, DateUtil.currentTime)
        FeedBackRepository().setComment(comment) {
            when (it) {
                is Resource.Success -> {
                    addLog("Response", it.data, "Comment Send Successfully", "FeedbackUtil/setComment()")
                }
                is Resource.Error -> {
                    addLog("Response", it.message, "Response post error", "FeedbackUtil()/setComment()")
                }
                is Resource.Loading -> {}
            }
        }

    }
}



