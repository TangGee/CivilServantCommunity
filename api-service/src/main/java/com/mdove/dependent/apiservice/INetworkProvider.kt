package com.mdove.dependent.apiservice

import com.mdove.herohalberd.ClaymoreNoop


/**
 * Created by MDove on 2019-09-03.
 */
interface INetworkProvider {
    val networkClient: NetworkClient

    interface NetworkClient {
        @Throws(Exception::class)
        operator fun get(url: String): String

        @Throws(Exception::class)
        fun post(url: String, body: String): String

        @Throws(Exception::class)
        fun post(url: String, body: String, respHeaders: Map<String, String>): String
    }

//    @ClaymoreNoop(INetworkProvider::class)
//    class Stub : INetworkProvider {
//        override val networkClient: NetworkClient
//            get() = object : NetworkClient {
//                override fun get(url: String): String {
//                    return "ClaymoreNoop -> INetworkProvider"
//                }
//
//                override fun post(url: String, body: String): String {
//                    return "ClaymoreNoop -> INetworkProvider"
//                }
//
//                override fun post(
//                    url: String,
//                    body: String,
//                    respHeaders: Map<String, String>
//                ): String {
//                    return "ClaymoreNoop -> INetworkProvider"
//                }
//            }
//    }
}