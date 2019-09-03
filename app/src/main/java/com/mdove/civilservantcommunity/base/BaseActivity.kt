package com.mdove.civilservantcommunity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mdove.civilservantcommunity.base.threadpool.FastMainScope
import com.mdove.civilservantcommunity.view.utils.StatusBarUtil
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Created by MDove on 2019-09-02.
 */
open class BaseActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = FastMainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTranslucent(this)
    }
}