package com.mdove.civilservantcommunity.setting.utils

import com.mdove.civilservantcommunity.config.AppConfig

/**
 * Created by MDove on 2020-01-06.
 */
object HideRecorder {
    private val observers = mutableListOf<IHideRecorderObserver>()

    fun addHideRecord(type: Int) {
        val preTypes = getHideRecordTypes()
        val preConfig = AppConfig.getMainFeedHideBtns()
        val realConfig = if (preTypes != null) {
            val hasType = preTypes.find {
                it == type
            } != null
            if (hasType) {
                null
            } else {
                preConfig.plus(if (preConfig.endsWith(",")) "$type" else ",$type")
            }
        } else {
            "$type"
        }
        realConfig?.let {
            AppConfig.setMainFeedHideBtns(it)
            // 通知观察者
            getHideRecordTypes(it)?.let { types ->
                notifyObserver(types, type)
            }
        } ?: also {
            notifyObserver(null, null)
        }
    }

    fun getHideRecordTypes(config: String? = null): List<Int>? {
        val realConfig = config ?: AppConfig.getMainFeedHideBtns()
        return realConfig.split(",").filter {
            it.isNotBlank()
        }.takeIf {
            it.isNotEmpty()
        }?.mapNotNull {
            try {
                it.toInt()
            } catch (e: Exception) {
                null
            }
        }?.takeIf {
            it.isNotEmpty()
        }
    }

    fun removeHideRecord(type: Int) {
        getHideRecordTypes()?.let {
            it.filter {
                it != type
            }.let {
                val config = it.joinToString(",") {
                    it.toString()
                }
                AppConfig.setMainFeedHideBtns(config)
                notifyObserver(it, type)
            }
        }
    }

    private fun notifyObserver(hides: List<Int>?, hide: Int?) {
        observers.forEach {
            it.onChange(hides, hide)
        }
    }

    fun registerObserver(observer: IHideRecorderObserver) {
        observers.find {
            it == observer
        } ?: also {
            observers.add(observer)
        }
    }

    fun unregisterObserver(observer: IHideRecorderObserver) {
        observers.find {
            it == observer
        }?.let {
            observers.remove(it)
        }
    }
}

interface IHideRecorderObserver {
    fun onChange(hides: List<Int>?, hide: Int?)
}