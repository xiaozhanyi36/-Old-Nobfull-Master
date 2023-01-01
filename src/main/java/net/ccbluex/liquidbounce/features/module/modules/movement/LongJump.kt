package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.EnumAutoDisableType
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "LongJump", category = ModuleCategory.MOVEMENT, autoDisable = EnumAutoDisableType.FLAG)
class LongJump : Module() {
    private val modeValue = ListValue("Mode", arrayOf("NCP", "NewBlocksmc", "HYT4V4"), "HYT4V4")
    private val ncpBoostValue = FloatValue("NCPBoost", 4.25f, 1f, 10f)

    // settings
    private val autoJumpValue = BoolValue("AutoJump", true)
    private val autoDisableValue = BoolValue("AutoDisable", true)
    private var jumped = false
    private var hasJumped = false
    private var canBoost = false
    private var teleported = false
    private var timer = MSTimer()
    private var airTicks = 0
    private var balance = 0
    private var damageStat = false

    override fun onEnable() {
        airTicks = 0
        balance = 0
        hasJumped = false
        damageStat = false
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1f
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        mc.thePlayer ?: return

        if (jumped) {
            val mode = modeValue.get()

            if (!mc.thePlayer.onGround) {
                airTicks++
            } else {
                airTicks = 0
            }

            if (mc.thePlayer.onGround || mc.thePlayer.capabilities.isFlying) {
                jumped = false

                if (mode.equals("NCP", ignoreCase = true)) {
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                }
                return
            }
            run {
                when (mode.lowercase()) {
                    "ncp" -> {
                        MovementUtils.strafe(MovementUtils.getSpeed() * if (canBoost) ncpBoostValue.get() else 0.5f)
                        canBoost = false
                    }

                    "newblocksmc" -> {
                        MovementUtils.strafe(0.8999114514f)
                        mc.thePlayer.motionY += 0.02194514
                        mc.timer.timerSpeed *= 0.98f
                    }
                    "hyt4v4" -> {
                        mc.thePlayer.motionY += 0.034470000997
                        MovementUtils.strafe(MovementUtils.getSpeed() * 1.0114514f)
                        mc.timer.timerSpeed = 1.0114514f
                    }
                }
            }
        }

        if (autoJumpValue.get() && mc.thePlayer.onGround || mc.thePlayer.isInWater || mc.thePlayer.isInLava || mc.thePlayer.isRiding || mc.thePlayer.isOnLadder) {
            jumped = true
            if (hasJumped && autoDisableValue.get()) {
                state = false
                return
            }
            mc.thePlayer.jump()
            hasJumped = true
        }
    }

    @EventTarget
    fun onMove(event: MoveEvent) {
        mc.thePlayer ?: return
        val mode = modeValue.get()

        if (mode.equals("ncp", ignoreCase = true) && !MovementUtils.isMoving() && jumped) {
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
            event.zeroXZ()
        }
    }

    @EventTarget(ignoreCondition = true)
    fun onJump(event: JumpEvent) {
        jumped = true
        canBoost = true
        teleported = false
        timer.reset()
    }

    override val tag: String
        get() = modeValue.get()
}