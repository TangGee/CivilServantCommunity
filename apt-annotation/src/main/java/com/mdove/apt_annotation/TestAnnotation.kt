package com.mdove.apt_annotation


/**
 * Created by MDove on 2019-11-30.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class TestAnnotation(val value: Int)