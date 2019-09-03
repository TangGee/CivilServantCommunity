package com.mdove.civilservantcommunity.base

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.mdove.civilservantcommunity.base.threadpool.FastMainScope
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

val Fragment.contextJob: CoroutineContext
    get() = (this as? CoroutineScope)?.coroutineContext ?: FastMainScope()

val Activity.contextJob: CoroutineContext
    get() = (this as? CoroutineScope)?.coroutineContext ?: FastMainScope()

val Context?.contextJob: CoroutineContext
    get() = (this as? CoroutineScope)?.coroutineContext ?: FastMainScope()
