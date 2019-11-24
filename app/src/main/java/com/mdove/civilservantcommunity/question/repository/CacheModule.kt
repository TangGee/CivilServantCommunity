package com.mdove.civilservantcommunity.question.repository

import com.mdove.civilservantcommunity.question.bean.QuestionDetailResp
import com.mdove.dependent.common.network.NormalResp

/**
 * Created by MDove on 2019-11-21..
 */
class CacheModule {
    var mQuestionCacheResp: NormalResp<String>? = null
    var mAnswerCacheResp: NormalResp<String>? = null
    var mQuestionDetailCacheResp : NormalResp<QuestionDetailResp>? = null
}