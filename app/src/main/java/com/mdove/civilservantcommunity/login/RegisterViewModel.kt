package com.mdove.civilservantcommunity.login

import androidx.lifecycle.ViewModel

/**
 * Created by zhaojing on 2019-09-02.
 */
class RegisterViewModel :ViewModel() {
    // u001在职,u002在校生，u003专职备考，u004已上岸，u005宝妈奶爸
    private val identityKeys = mutableListOf("u001","u002","u003","u004","u005")
    var selectIdentity: String? = null

    fun onClickIdentity(index: Int) {
        selectIdentity = identityKeys[index]
    }
}