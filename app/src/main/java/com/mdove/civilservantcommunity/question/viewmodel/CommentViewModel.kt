package com.mdove.civilservantcommunity.question.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.question.bean.CommentInfo
import com.mdove.civilservantcommunity.question.bean.CommentReqParams
import com.mdove.civilservantcommunity.question.bean.QuestionCommentSendParams
import com.mdove.civilservantcommunity.question.repository.CommentRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource
import com.mdove.dependent.common.toast.ToastUtil

/**
 * Created by MDove on 2019-11-24.
 */
class CommentViewModel : ViewModel() {
    private val repository = CommentRepository()
    var params: QuestionCommentSendParams? = null

    fun sendComment(content: String): LiveData<Resource<NormalResp<String>>>? {
        return params?.let {
            val anid = it.father?.anid
            val fatherId = it.father?.info?.commentId
            val commentInfo = AppConfig.getUserInfo()?.let {
                CommentInfo(it.uid, it.username)
            }
            if (anid != null && fatherId != null && commentInfo != null) {
                CommentReqParams(anid, fatherId, commentInfo, content)
            } else {
                ToastUtil.toast("参数异常")
                null
            }
        }?.let {
            repository.saveComment(it)
        }
    }
}