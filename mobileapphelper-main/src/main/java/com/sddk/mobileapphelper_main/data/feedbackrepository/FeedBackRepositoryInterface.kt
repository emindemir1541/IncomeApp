package com.sddk.schoolgadgets.library.data.feedbackrepository

import com.sddk.mobileapphelper_main.common.model.Comment
import com.sddk.mobileapphelper_main.common.model.Error
import com.sddk.mobileapphelper_main.common.model.Resource

interface FeedBackRepositoryInterface {

    fun setError(error: com.sddk.mobileapphelper_main.common.model.Error, result: (com.sddk.mobileapphelper_main.common.model.Resource<com.sddk.mobileapphelper_main.common.model.Error>) -> Unit = {})

    fun setComment(comment: com.sddk.mobileapphelper_main.common.model.Comment, result: (com.sddk.mobileapphelper_main.common.model.Resource<com.sddk.mobileapphelper_main.common.model.Comment>) -> Unit = {})

}