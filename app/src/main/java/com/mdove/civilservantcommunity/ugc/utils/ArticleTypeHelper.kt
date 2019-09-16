package com.mdove.civilservantcommunity.ugc.utils

import com.mdove.civilservantcommunity.base.bean.ArticleType

/**
 * Created by MDove on 2019-09-15.
 */
object ArticleTypeHelper {
    val typeTitles = listOf("行测", "申论")

    private val typesMap = mutableMapOf<String, ArticleType>().apply {
        put("行测", ArticleType("行测", "A000"))
        put("申论", ArticleType("申论", "B000"))
    }

    fun getTypeId(title: String): ArticleType {
        return typesMap[title] ?: ArticleType("行测", "A000")
    }
}