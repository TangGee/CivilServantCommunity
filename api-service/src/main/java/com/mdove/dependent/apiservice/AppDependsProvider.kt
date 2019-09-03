package com.mdove.dependent.apiservice

import com.mdove.herohalberd.ClaymoreServiceLoader


/**
 * Created by MDove on 2019-09-03.
 */
object AppDependsProvider {
    val networkService = ClaymoreServiceLoader.loadFirst(INetworkProvider::class.java)
}