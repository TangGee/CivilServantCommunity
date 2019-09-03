package com.mdove.dependent.networkprovider

import com.mdove.dependent.apiservice.INetworkProvider
import com.mdove.herohalberd.ClaymoreImpl
import com.mdove.dependent.networkprovider.base.FrameApiClient

/**
 * Created by MDove on 2019-09-03.
 */
@ClaymoreImpl(INetworkProvider::class)
class NetworkProviderImpl : INetworkProvider {

    override val networkClient: INetworkProvider.NetworkClient
        get() = object : INetworkProvider.NetworkClient {
            override fun get(url: String): String {
                return FrameApiClient.executeGet(url)
            }

            override fun post(url: String, body: String): String {
                return FrameApiClient.executePost(url, body)
            }

            override fun post(url: String, body: String, respHeaders: Map<String, String>): String {
                return FrameApiClient.executePost(url, body, respHeaders)
            }
        }
}