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
    private val getTopicFlag = MutableLiveData<String>()
    private val allTopicsLiveData = Transformations.switchMap(getTopicFlag) {
        repository.getAllTopics()
    }
    private val defaultTopics = mutableListOf(
        UGCRlvTopicBean("求复习计划安排", "P001", true),
        UGCRlvTopicBean("求上岸经验", "A001", false)
    )

    val clickTopicLiveData = MutableLiveData<UGCRlvTopicBean>().apply {
        value = defaultTopics[0]
    }

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

        addSource(allTopicsLiveData) { res ->
            value = value?.let {
                res.data?.data?.mapIndexed { index, topics ->
                    UGCRlvTopicBean(
                        topics.typeName,
                        topics.typeId,
                        index == 0
                    )
                } ?: defaultTopics
            } ?: defaultTopics
        }
    }

    val postResp: LiveData<Resource<NormalResp<String>>> = Transformations.switchMap(postType) {
        repository.postShare(it)
    }

    fun getTopics() {
        getTopicFlag.value = ""
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