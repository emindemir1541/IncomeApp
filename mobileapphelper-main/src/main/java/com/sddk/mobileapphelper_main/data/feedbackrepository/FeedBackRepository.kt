package com.sddk.mobileapphelper_main.data.feedbackrepository

import com.google.gson.Gson
import com.sddk.mobileapphelper_main.common.util.ResponseFeedbackModule.ERRORS
import com.sddk.mobileapphelper_main.common.util.ResponseFeedbackModule.baseUrl
import com.sddk.mobileapphelper_main.common.util.ResponseFeedbackModule.div
import com.sddk.mobileapphelper_main.common.util.ResponseFeedbackModule.jsonStr
import com.sddk.mobileapphelper_main.common.helper.Helper
import com.sddk.mobileapphelper_main.common.helper.addLog
import com.sddk.mobileapphelper_main.common.model.Comment
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.IOException
import com.sddk.mobileapphelper_feedback.common.helper.SystemInfo
import com.sddk.mobileapphelper_main.common.model.Error
import com.sddk.mobileapphelper_main.common.model.Resource
import com.sddk.mobileapphelper_main.common.util.ResponseFeedbackModule.COMMENTS
import com.sddk.schoolgadgets.library.data.feedbackrepository.FeedBackRepositoryInterface
import kotlinx.coroutines.*
import okhttp3.RequestBody.Companion.toRequestBody

class FeedBackRepository : FeedBackRepositoryInterface {

    private val urlBase = baseUrl + SystemInfo.packageNameWithoutDat() + div
    private val errorUrl = urlBase + ERRORS + div + com.sddk.mobileapphelper_main.common.helper.Helper.deviceId + div
    private val commentUrl = urlBase + COMMENTS + div + com.sddk.mobileapphelper_main.common.helper.Helper.deviceId + div

    override fun setError(error: com.sddk.mobileapphelper_main.common.model.Error, result: (com.sddk.mobileapphelper_main.common.model.Resource<com.sddk.mobileapphelper_main.common.model.Error>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = error.json.toRequestBody("application/jsonStr".toMediaTypeOrNull())
            val request = Request.Builder().url(errorUrl + error.time + jsonStr).put(requestBody).build()
            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    result.invoke(com.sddk.mobileapphelper_main.common.model.Resource.Error(e.localizedMessage))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.body != null) {
                        val responseBody: String = response.body!!.string()
                        result.invoke(com.sddk.mobileapphelper_main.common.model.Resource.Success(Gson().fromJson(responseBody, com.sddk.mobileapphelper_main.common.model.Error::class.java)))
                        com.sddk.mobileapphelper_main.common.helper.addLog("FirebaseFeedBack", response.body.toString(), "Data sent to server successfully", "FeedBackRepository/setError()")
                    }
                    else {
                        result.invoke(com.sddk.mobileapphelper_main.common.model.Resource.Error("null"))
                    }
                }
            })
        }
    }

    override fun setComment(comment: com.sddk.mobileapphelper_main.common.model.Comment, result: (com.sddk.mobileapphelper_main.common.model.Resource<com.sddk.mobileapphelper_main.common.model.Comment>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = comment.json.toRequestBody("application/jsonStr".toMediaTypeOrNull())
            val request = Request.Builder().url(commentUrl + comment.time + jsonStr).put(requestBody).build()
            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    result.invoke(com.sddk.mobileapphelper_main.common.model.Resource.Error(e.localizedMessage))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.body != null) {
                        val responseBody: String = response.body!!.string()
                        result.invoke(com.sddk.mobileapphelper_main.common.model.Resource.Success(Gson().fromJson(responseBody, com.sddk.mobileapphelper_main.common.model.Comment::class.java)))
                        com.sddk.mobileapphelper_main.common.helper.addLog("FirebaseFeedBack", response.body.toString(), "Data sent to server successfully", "FeedBackRepository/setComment()")
                    }
                    else {
                        result.invoke(com.sddk.mobileapphelper_main.common.model.Resource.Error("null"))
                    }
                }
            })
        }
    }

}