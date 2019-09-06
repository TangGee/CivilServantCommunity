package com.mdove.civilservantcommunity.base

import androidx.fragment.app.Fragment
import com.mdove.dependent.common.threadpool.FastMainScope
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Created by MDove on 2019-09-03.
 */
open class BaseFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = FastMainScope()
}