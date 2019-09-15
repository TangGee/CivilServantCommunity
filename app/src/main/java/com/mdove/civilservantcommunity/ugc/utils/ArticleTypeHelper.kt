package com.mdove.civilservantcommunity.ugc.utils

/**
 * Created by MDove on 2019-09-15.
 */
object ArticleTypeHelper {
    val typeTitles = listOf("行测", "申论")

    private val typesMap = mutableMapOf<String, String>().apply {
        put("行测", "A000")
        put("申论", "B000")
    }

    fun getTypeId(title: String): String {
        return typesMap[title] ?: "A000"
    }
}