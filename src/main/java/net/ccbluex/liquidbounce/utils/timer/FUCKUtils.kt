package net.ccbluex.liquidbounce.utils.timer

class FUCKUtils {
    var lastMS: Long = 0
    private val time: Long = 0
    private val currentMS: Long
        private get() = System.nanoTime() / 1000000L
    private val prevTime: Long = 0
    fun hasReached(milliseconds: Double): Boolean {
        return if ((currentMS - lastMS).toDouble() >= milliseconds) {
            true
        } else false
    }

    fun hasReached(delay: Long): Boolean {
        return System.currentTimeMillis() - lastMS >= delay
    }

    fun setTime(time: Long) {
        lastMS = time
    }

    fun hasTimeElapsed(time: Long): Boolean {
        return System.currentTimeMillis() - lastMS > time
    }

    fun hasPassed(milli: Double): Boolean {
        return System.currentTimeMillis() - prevTime >= milli
    }

    fun sleep(time: Long): Boolean {
        if (time() >= time) {
            reset()
            return true
        }
        return false
    }

    fun time(): Long {
        return System.nanoTime() / 1000000L - time
    }

    val elapsedTime: Long
        get() = currentMS - lastMS

    fun reset() {
        lastMS = currentMS
    }

    fun delay(milliSec: Float): Boolean {
        return if ((getTime() - lastMS).toFloat() >= milliSec) {
            true
        } else false
    }

    fun getTime(): Long {
        return System.nanoTime() / 1000000L
    }

    fun isDelayComplete(delay: Long): Boolean {
        return if (System.currentTimeMillis() - lastMS > delay) true else false
    }
}