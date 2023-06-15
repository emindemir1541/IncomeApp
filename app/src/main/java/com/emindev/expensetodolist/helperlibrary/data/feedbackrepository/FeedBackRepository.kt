package com.emindev.expensetodolist.helperlibrary.data.feedbackrepository

import com.google.gson.Gson
import com.emindev.expensetodolist.helperlibrary.common.util.ResponseFeedbackModule.ERRORS
import com.emindev.expensetodolist.helperlibrary.common.util.ResponseFeedbackModule.baseUrl
import com.emindev.expensetodolist.helperlibrary.common.util.ResponseFeedbackModule.div
import com.emindev.expensetodolist.helperlibrary.common.util.ResponseFeedbackModule.jsonStr
import com.emindev.expensetodolist.helperlibrary.common.helper.Helper
import com.emindev.expensetodolist.helperlibrary.common.helper.addLog
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.IOException
import com.emindev.expensetodolist.helperlibrary.common.helper.SystemInfo
import com.emindev.expensetodolist.helperlibrary.common.model.Comment
import com.emindev.expensetodolist.helperlibrary.common.model.Error
import com.emindev.expensetodolist.helperlibrary.common.model.Resource
import com.emindev.expensetodolist.helperlibrary.common.util.ResponseFeedbackModule.COMMENTS
import com.sddk.schoolgadgets.library.data.feedbackrepository.FeedBackRepositoryInterface
import kotlinx.coroutines.*
import okhttp3.RequestBody.Companion.toRequestBody

class FeedBackRepository : FeedBackRepositoryInterface {

    private val urlBase = baseUrl + SystemInfo.packageNameWithoutDat() + div
    private val errorUrl = urlBase + ERRORS + div + Helper.deviceId + div
    private val commentUrl = urlBase + COMMENTS + div + Helper.deviceId + div

    override fun setError(error: Error, result: (Resource<Error>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = error.json.toRequestBody("application/jsonStr".toMediaTypeOrNull())
            val request = Request.Builder().url(errorUrl + error.time + jsonStr).put(requestBody).build()
            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    result.invoke(Resource.Error(e.localizedMessage))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.body != null) {
                        val responseBody: String = response.body!!.string()
                        result.invoke(Resource.Success(Gson().fromJson(responseBody, Error::class.java)))
                        addLog("FirebaseFeedBack", response.body.toString(), "Data sent to server successfully", "FeedBackRepository/setError()")
                    }
                    else {
                        result.invoke(Resource.Error("null"))
                    }
                }
            })
        }
    }

    override fun setComment(comment: Comment, result: (Resource<Comment>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = comment.json.toRequestBody("application/jsonStr".toMediaTypeOrNull())
            val request = Request.Builder().url(commentUrl + comment.time + jsonStr).put(requestBody).build()
            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    result.invoke(Resource.Error(e.localizedMessage))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.body != null) {
                        val responseBody: String = response.body!!.string()
                        result.invoke(Resource.Success(Gson().fromJson(responseBody, Comment::class.java)))
                        addLog("FirebaseFeedBack", response.body.toString(), "Data sent to server successfully", "FeedBackRepository/setComment()")
                    }
                    else {
                        result.invoke(Resource.Error("null"))
                    }
                }
            })
        }
    }

}