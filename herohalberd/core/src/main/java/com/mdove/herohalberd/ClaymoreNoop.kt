package com.mdove.herohalberd

import kotlin.reflect.KClass

@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class ClaymoreNoop(val value:KClass<*>)
