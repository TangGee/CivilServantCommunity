package com.mdove.civilservantcommunity.ugc.viewmodel

import androidx.lifecycle.*
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.ugc.bean.UGCPostNormalParams
import com.mdove.civilservantcommunity.ugc.bean.UGCPostQuestionParams
import com.mdove.civilservantcommunity.ugc.bean.UGCRlvTopicBean
import com.mdove.civilservantcommunity.ugc.bean.UGCTopic
import com.mdove.civilservantcommunity.ugc.repository.UGCRepository
import com.mdove.civilservantcommunity.ugc.utils.ArticleTypeHelper
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-09-15.
 */
class MainUGCViewModel : ViewModel() {
    val typeTitles = ArticleTypeHelper.typeTitles
    private val selectTypes = mutableMapOf<String, ArticleType>()
    private val repository = UGCRepository()

    private val postType = MutableLiveData<UGCPostNormalParams>()

    val clickTopicLiveData = MutableLiveData<UGCRlvTopicBean>()

    val topics = MediatorLiveData<List<UGCRlvTopicBean>>().apply {
        addSource(clickTopicLiveData) { click ->
            value = value?.map {
                if (it.id == click.id) {
                    click
                } else {
                    it.copy(selectStatus = false)
                }
            }
        }

        value = mutableListOf(
            UGCRlvTopicBean("padding", "0", 0, false),
            UGCRlvTopicBean("求每日计划", "A000", 1, true),
            UGCRlvTopicBean("求上岸经验分享", "B000", 1, false)
        )
    }

    val postResp: LiveData<Resource<NormalResp<String>>> = Transformations.switchMap(postType) {
        repository.postShare(it)
    }

    fun post(userInfo: UserInfo, title: String, content: String) {
        postType.value = UGCPostNormalParams(userInfo, selectTypes.map {
            it.value
        }, title, content, "1")
    }

    fun postQuestion(
        userInfo: UserInfo,
        title: String,
        content: String
    ): LiveData<Resource<NormalResp<String>>>? {
        return clickTopicLiveData.value?.let {
            repository.postQuestion(
                UGCPostQuestionParams(
                    userInfo,
                    UGCTopic(it.name, it.id),
                    title,
                    content,
                    "1"
                )
            )
        }
    }

    fun onSelectType(title: String) {
        selectTypes[title]?.let {
            selectTypes.remove(title)
        } ?: also {
            selectTypes[title] = ArticleTypeHelper.getTypeId(title)
        }
    }
}