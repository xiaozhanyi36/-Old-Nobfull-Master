package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.utils.MovementUtils
class Autojump : SpeedMode("Autojump-timer") {
    private var recX = 0.0
    private var recZ = 0.0
    private val TimerBoostValue = FloatValue("TimerBoostValue", 1f, 0.1f, 3f)
    override fun onUpdate() {
        if (MovementUtils.isMoving() && mc.thePlayer.onGround) {
            mc.timer.timerSpeed=TimerBoostValue.get()
            mc.thePlayer.jump()
        }
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1f
    }
}