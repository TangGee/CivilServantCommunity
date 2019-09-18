package com.mdove.civilservantcommunity.account.utils

/**
 * Created by MDove on 2019-09-11.
 */
object IdentitysHelper {
    val identitys = arrayListOf("在校生", "在职人员", "专职备考", "宝妈奶爸", "已上岸")
    // u001在职,u002在校生，u003专职备考，u004已上岸，u005宝妈奶爸
    private val identityKeys = mutableListOf("u001", "u002", "u003", "u004", "u005")

    private val identitysMap = mutableMapOf<String, String>().apply {
        put("u001", "在校生")
        put("u002", "在职人员")
        put("u003", "专职备考")
        put("u004", "宝妈奶爸")
        put("u005", "已上岸")

    }

    fun getIdentity(typeId: String): String {
        return identitysMap[typeId] ?: "未知身份"
    }
}