package com.mdove.civilservantcommunity.test

/**
 * Created by zhaojing on 2019-11-21.
 */
class FunctionTest {
    var func: (FunctionTest.(Int) -> Boolean)? = null
}

class Ha {
    fun aaaa() {
        FunctionTest().apply {
            this.func = object : (FunctionTest, Int) -> Boolean {
                override fun invoke(p1: FunctionTest, p2: Int): Boolean {
                    return true
                }
            }
        }
    }
}