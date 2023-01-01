// Destiny made by ChengFeng
package net.ccbluex.liquidbounce.utils.timer

class MSTimer {
    private var time = -1L
    var lastMS = System.currentTimeMillis()


    fun hasTimePassed(MS: Long): Boolean {
        return System.currentTimeMillis() >= time + MS
    }

    fun isDelayComplete(delay: Float): Boolean {
        return System.currentTimeMillis() - lastMS > delay
    }

    fun hasTimeLeft(MS: Long): Long {
        return MS + time - System.currentTimeMillis()
    }

    fun reset() {
        time = System.currentTimeMillis()
    }

    fun hasTimeElapsed(time: Long, reset: Boolean): Boolean {
        if (System.currentTimeMillis() - lastMS > time) {
            if (reset) reset()
            return true
        }
        return false
    }

}