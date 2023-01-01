package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.FloatValue

class GroundStrafeHop : SpeedMode("GroundStrafeHop") {
    private val strafeValue = FloatValue("AddStrafe", 0.0f, 0.0f, 1.0f)
    private val speedValue = FloatValue("SetSpeed", 0.42f, 0.42f, 1.0f)

    override fun onPreMotion() {
        if (MovementUtils.isMoving()) {
            if (mc.thePlayer.onGround) {
                MovementUtils.getSpeed() + speedValue.get()
                mc.thePlayer.jump()
                MovementUtils.strafe(strafeValue.get())
            }
        } else {
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
        }
    }
}