package com.mdove.civilservantcommunity.question.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.question.bean.*
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

    val inputStatusLiveData = MutableLiveData<InputStatus>().apply {
        value = InputStatus.INPUT_STATUS_HIDE
    }

    fun trySendComment(content: String): LiveData<Resource<NormalResp<String>>>? {
        return params?.let {parmas->
            parmas.child?.let {
                val fatherId = it.father.info?.commentId
                val fatherName = it.father.info?.userName
                val fatherUid = it.father.info?.uid
                val anid = it.father.anid
                val commentInfo = AppConfig.getUserInfo()?.let {
                    CommentInfo(it.uid, it.username)
                }
                if (commentInfo != null && anid != null && fatherId != null && fatherName != null && fatherUid != null) {
                    val toInfo = ToInfo(fatherId,fatherUid , fatherName)
                    repository.saveToComment(CommentToReqParams(anid, fatherId,commentInfo,toInfo, content))
                }else{
                    ToastUtil.toast("参数异常")
                    null
                }
            } ?: let{
                val anid = parmas.father?.anid
                val fatherId = parmas.father?.info?.commentId
                val commentInfo = AppConfig.getUserInfo()?.let {
                    CommentInfo(it.uid, it.username)
                }
                if (anid != null && fatherId != null && commentInfo != null) {
                    repository.saveComment(CommentReqParams(anid, fatherId, commentInfo, content))
                } else {
                    ToastUtil.toast("参数异常")
                    null
                }
            }
        }
    }
}

enum class InputStatus {
    INPUT_STATUS_SHOW,
    INPUT_STATUS_HIDE,
}