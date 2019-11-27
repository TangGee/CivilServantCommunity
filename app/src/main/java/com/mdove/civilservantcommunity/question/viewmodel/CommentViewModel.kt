package com.mdove.civilservantcommunity.question.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.question.bean.*
import com.mdove.civilservantcommunity.question.repository.CommentRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource
import com.mdove.dependent.common.toast.ToastUtil

/**
 * Created by MDove on 2019-11-24.
 */
class CommentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CommentRepository()
    val params = MutableLiveData<BaseCommentSendParams>()
    val titleLiveData: LiveData<CommentSendDialogTitle> = Transformations.map(params) {
        when (it) {
            is OneCommentSendParams -> {
                val name = it.commentInfo?.userName ?: application.getString(R.string.string_no_name)
                val content = "回复${name}的评论"
                CommentSendDialogTitle(content, 2, name.length + 2)
            }
            is TwoCommentSendParams -> {
                val name = it.commentInfo?.userName ?: application.getString(R.string.string_no_name)
                val content = "回复${name}的评论"
                CommentSendDialogTitle(content, 2, name.length + 2)

            }
            is AnswerCommentSendParams -> {
                val name = it.questionUserName ?: application.getString(R.string.string_no_name)
                val content = "回答${name}的提问"
                CommentSendDialogTitle(content, 2, name.length + 2)
            }
            else -> null
        }
    }

    val inputStatusLiveData = MutableLiveData<InputStatus>().apply {
        value = InputStatus.INPUT_STATUS_HIDE
    }

    fun trySendComment(content: String): LiveData<Resource<NormalResp<String>>>? {
        return params.value?.let {
            when (it) {
                is OneCommentSendParams -> {
                    val anid = it.anid
                    val commentInfo = AppConfig.getUserInfo()?.let {
                        CommentInfo(it.uid, it.username)
                    }
                    if (anid != null && commentInfo != null) {
                        repository.saveComment(CommentReqParams(anid, commentInfo, content))
                    } else {
                        ToastUtil.toast("参数异常")
                        null
                    }
                }
                is TwoCommentSendParams -> {
                    // 此时的二级评论，是对child说的
                    val fatherId = it.fatherId
                    val toId = it.commentInfo?.commentId
                    val toName = it.commentInfo?.userName
                    val toUid = it.commentInfo?.uid
                    val anid = it.anid
                    val commentInfo = AppConfig.getUserInfo()?.let {
                        CommentInfo(it.uid, it.username)
                    }
                    if (commentInfo != null && anid != null && fatherId != null && toId != null && toUid != null && toName != null) {
                        val toInfo = ToInfo(toId, toUid, toName)
                        repository.saveToComment(
                            CommentToReqParams(
                                anid,
                                fatherId,
                                commentInfo,
                                toInfo,
                                content
                            )
                        )
                    } else {
                        ToastUtil.toast("参数异常")
                        null
                    }
                }
                is AnswerCommentSendParams -> {
                    val qid = it.qid
                    val userInfo = AppConfig.getUserInfo()
                    if (qid != null && userInfo != null) {
                        repository.saveAnswer(AnswerReqParams(userInfo, qid, content, "1"))
                    } else {
                        ToastUtil.toast("参数异常")
                        null
                    }
                }
                else -> null
            }
        }
    }
}

data class CommentSendDialogTitle(val content: String, val spanStart: Int, val spanEnd: Int)

enum class InputStatus {
    INPUT_STATUS_SHOW,
    INPUT_STATUS_HIDE,
}