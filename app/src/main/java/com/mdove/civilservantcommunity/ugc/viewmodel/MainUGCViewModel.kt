package com.mdove.civilservantcommunity.ugc.viewmodel

import androidx.lifecycle.ViewModel
import com.mdove.civilservantcommunity.base.bean.ArticleType
import com.mdove.civilservantcommunity.ugc.utils.ArticleTypeHelper

/**
 * Created by MDove on 2019-09-15.
 */
class MainUGCViewModel : ViewModel() {
    val typeTitles = ArticleTypeHelper.typeTitles
    val selectTypes = mutableMapOf<String, ArticleType>()

    fun onSelectType(title: String) {
        selectTypes.contains(title)
    }
}