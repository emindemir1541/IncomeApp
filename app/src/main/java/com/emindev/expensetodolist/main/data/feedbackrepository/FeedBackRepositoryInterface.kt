package com.sddk.schoolgadgets.library.data.feedbackrepository

import com.emindev.expensetodolist.main.common.model.Comment
import com.emindev.expensetodolist.main.common.model.Error
import com.emindev.expensetodolist.main.common.model.Resource

interface FeedBackRepositoryInterface {

    fun setError(error: Error, result: (Resource<Error>) -> Unit = {})

    fun setComment(comment: Comment, result: (Resource<Comment>) -> Unit = {})

}