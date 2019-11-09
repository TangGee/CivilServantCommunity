package com.mdove.civilservantcommunity.plan.utils

import com.mdove.dependent.common.utils.TimeUtils

/**
 * Created by MDove on 2019-11-09.
 */
object TimeScheduleHelper {
    private val timesMapByIndex = mutableMapOf<Int, Pair<Long, Long>>().apply {
        this[0] = Pair(TimeUtils.getTimes(0, 0), TimeUtils.getTimes(1, 0))
        this[1] = Pair(TimeUtils.getTimes(1, 0), TimeUtils.getTimes(2, 0))
        this[2] = Pair(TimeUtils.getTimes(2, 0), TimeUtils.getTimes(3, 0))
        this[3] = Pair(TimeUtils.getTimes(3, 0), TimeUtils.getTimes(4, 0))
        this[4] = Pair(TimeUtils.getTimes(4, 0), TimeUtils.getTimes(5, 0))
        this[5] = Pair(TimeUtils.getTimes(5, 0), TimeUtils.getTimes(6, 0))
        this[6] = Pair(TimeUtils.getTimes(6, 0), TimeUtils.getTimes(7, 0))
        this[7] = Pair(TimeUtils.getTimes(7, 0), TimeUtils.getTimes(8, 0))
        this[8] = Pair(TimeUtils.getTimes(8, 0), TimeUtils.getTimes(9, 0))
        this[9] = Pair(TimeUtils.getTimes(9, 0), TimeUtils.getTimes(10, 0))
        this[10] = Pair(TimeUtils.getTimes(10, 0), TimeUtils.getTimes(11, 0))
        this[11] = Pair(TimeUtils.getTimes(11, 0), TimeUtils.getTimes(12, 0))
        this[12] = Pair(TimeUtils.getTimes(12, 0), TimeUtils.getTimes(13, 0))
        this[13] = Pair(TimeUtils.getTimes(13, 0), TimeUtils.getTimes(14, 0))
        this[14] = Pair(TimeUtils.getTimes(14, 0), TimeUtils.getTimes(15, 0))
        this[15] = Pair(TimeUtils.getTimes(15, 0), TimeUtils.getTimes(16, 0))
        this[16] = Pair(TimeUtils.getTimes(16, 0), TimeUtils.getTimes(17, 0))
        this[17] = Pair(TimeUtils.getTimes(17, 0), TimeUtils.getTimes(18, 0))
        this[18] = Pair(TimeUtils.getTimes(18, 0), TimeUtils.getTimes(19, 0))
        this[19] = Pair(TimeUtils.getTimes(19, 0), TimeUtils.getTimes(20, 0))
        this[20] = Pair(TimeUtils.getTimes(20, 0), TimeUtils.getTimes(21, 0))
        this[21] = Pair(TimeUtils.getTimes(21, 0), TimeUtils.getTimes(22, 0))
        this[22] = Pair(TimeUtils.getTimes(22, 0), TimeUtils.getTimes(23, 0))
        this[23] = Pair(TimeUtils.getTimes(23, 0), TimeUtils.getTimes(24, 0))
    }

    private val timesMapByPair = mutableMapOf<Long, Int>().apply {
        this[TimeUtils.getTimes(0, 0)] = 0
        this[TimeUtils.getTimes(1, 0)] = 1
        this[TimeUtils.getTimes(2, 0)] = 2
        this[TimeUtils.getTimes(3, 0)] = 3
        this[TimeUtils.getTimes(4, 0)] = 4
        this[TimeUtils.getTimes(5, 0)] = 5
        this[TimeUtils.getTimes(6, 0)] = 6
        this[TimeUtils.getTimes(7, 0)] = 7
        this[TimeUtils.getTimes(8, 0)] = 8
        this[TimeUtils.getTimes(9, 0)] = 9
        this[TimeUtils.getTimes(10, 0)] = 10
        this[TimeUtils.getTimes(11, 0)] = 11
        this[TimeUtils.getTimes(12, 0)] = 12
        this[TimeUtils.getTimes(13, 0)] = 13
        this[TimeUtils.getTimes(14, 0)] = 14
        this[TimeUtils.getTimes(15, 0)] = 15
        this[TimeUtils.getTimes(16, 0)] = 16
        this[TimeUtils.getTimes(17, 0)] = 17
        this[TimeUtils.getTimes(18, 0)] = 18
        this[TimeUtils.getTimes(19, 0)] = 19
        this[TimeUtils.getTimes(20, 0)] = 20
        this[TimeUtils.getTimes(21, 0)] = 21
        this[TimeUtils.getTimes(22, 0)] = 22
        this[TimeUtils.getTimes(23, 0)] = 23
    }

    fun getTimePairByIndex(index: Int): Pair<Long, Long> {
        return timesMapByIndex[index] ?: Pair(
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )
    }

    fun getIndexByTimePair(pairFirst: Long): Int {
        return timesMapByPair[pairFirst] ?: -1
    }
}