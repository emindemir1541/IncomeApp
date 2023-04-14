package com.sddk.schoolgadgets.library.data.feedbackrepository

import com.example.gelirgideruygulamas.helperlibrary.common.model.Comment
import com.example.gelirgideruygulamas.helperlibrary.common.model.Error
import com.example.gelirgideruygulamas.helperlibrary.common.model.Resource

interface FeedBackRepositoryInterface {

    fun setError(error: Error, result: (Resource<Error>) -> Unit = {})

    fun setComment(comment: Comment, result: (Resource<Comment>) -> Unit = {})

}