package com.mdove.civilservantcommunity.ugc.viewmodel

import androidx.lifecycle.*
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.ugc.bean.UGCPostParams
import com.mdove.civilservantcommunity.ugc.bean.UGCRlvTopicBean
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

    private val postType = MutableLiveData<UGCPostParams>()

    val clickTopicLiveData = MutableLiveData<UGCRlvTopicBean>()

    val topics = MediatorLiveData<List<UGCRlvTopicBean>>().apply {
        addSource(clickTopicLiveData) { click ->
            value = value?.map {
                if (it.id == click.id) {
                    click
                } else
                    it.copy(selectStatus = false)
            }
        }

        value = mutableListOf(
            UGCRlvTopicBean("padding", "0", 0, false),
            UGCRlvTopicBean("求公务员计划表", "1", 1, true),
            UGCRlvTopicBean("求公务员攻略", "2", 1, false),
            UGCRlvTopicBean("求四六级攻略", "3", 1, false)
        )
    }

    val postResp: LiveData<Resource<NormalResp<String>>> = Transformations.switchMap(postType) {
        repository.post(it)
    }

    fun post(userInfo: UserInfo, title: String, content: String) {
        postType.value = UGCPostParams(userInfo, selectTypes.map {
            it.value
        }, title, content, "1")
    }

    fun onSelectType(title: String) {
        selectTypes[title]?.let {
            selectTypes.remove(title)
        } ?: also {
            selectTypes[title] = ArticleTypeHelper.getTypeId(title)
        }
    }
}