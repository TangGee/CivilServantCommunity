package com.mdove.civilservantcommunity.ugc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.civilservantcommunity.ugc.bean.UGCPostParams
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