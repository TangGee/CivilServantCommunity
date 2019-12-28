package com.mdove.civilservantcommunity.feed.viewmodel

import androidx.lifecycle.*
import com.mdove.civilservantcommunity.account.bean.*
import com.mdove.civilservantcommunity.account.repository.AccountRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource
import com.mdove.dependent.common.networkenhance.valueobj.Status

/**
 * Created by MDove on 2019-09-11.
 */
class MePageViewModel : ViewModel() {
    private val accountRepository = AccountRepository()

    private val loadType = MutableLiveData<String>()
    private val meArticle = Transformations.switchMap(loadType) {
        accountRepository.mePage(it)
    }

    val paramsLiveData = MediatorLiveData<UserInfoParams>().apply {
        addSource(meArticle) {
            it.data?.data?.let {
                value = it.toUserInfoParams()
            }
        }
    }

    val data = MediatorLiveData<Resource<List<BaseMePageDetailInfo>>>().apply {
        addSource(meArticle) { res ->
            val data: MutableList<BaseMePageDetailInfo> = res.data?.data?.let {
                val temp = mutableListOf<BaseMePageDetailInfo>()
                it.answerList?.let {
                    temp.addAll(it)
                }
                it.articleList?.let {
                    temp.addAll(it)
                }
                it.questionList?.let {
                    temp.addAll(it)
                }
                if(temp.isEmpty()){
                    temp.add(MePageArticleAddArticleInfo())
                }
                temp
            } ?: mutableListOf<BaseMePageDetailInfo>(MePageArticleAddArticleInfo())
            if (res.status == Status.ERROR) {
                data.add(MePageArticleErrorIconInfo())
                data.add(MePageArticleErrorTitleInfo())
            }
            value = Resource(res.status, data, res.exception)
        }
    }

    fun reqMePage(uid: String) {
        loadType.value = uid
    }
}