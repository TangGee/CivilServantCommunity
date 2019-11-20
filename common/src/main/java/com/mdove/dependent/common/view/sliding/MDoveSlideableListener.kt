package  com.mdove.dependent.common.view.sliding

/**
 * slidebackactivity slidecloseactivity 实现
 */
interface MDoveSlideableListener {
    fun canSlideNow(x: Int, y: Int): Boolean
}