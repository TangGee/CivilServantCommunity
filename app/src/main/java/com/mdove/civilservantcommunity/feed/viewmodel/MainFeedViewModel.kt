package com.mdove.civilservantcommunity.feed.viewmodel

import androidx.lifecycle.*
import com.mdove.civilservantcommunity.feed.bean.*
import com.mdove.civilservantcommunity.feed.repository.MainFeedRepository
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.networkenhance.valueobj.Resource

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedViewModel : ViewModel() {
    private val loadType = MutableLiveData<LoadType>()
    private val repository = MainFeedRepository()

    private val feedData: LiveData<Resource<NormalResp<List<ArticleResp>>>> =
            Transformations.switchMap(loadType) {
                repository.reqFeed()
            }

    val mData: LiveData<Resource<List<BaseFeedResp>>> = MediatorLiveData<Resource<List<BaseFeedResp>>>().apply {
        addSource(feedData) {
            val temp = mutableListOf<BaseFeedResp>()
            temp.add(FeedPunchResp())
            temp.add(FeedUGCResp())
            temp.addAll(it.data?.data?.map { article ->
                FeedArticleResp(article)
            } ?: mutableListOf<BaseFeedResp>())
            value = Resource(it.status, temp, it.exception)
        }
    }


    fun reqFeed() {
        loadType.value = LoadType.NORMAL
    }
}

enum class LoadType {
    NORMAL,
    REFRESH,
    LOAD_MORE
}