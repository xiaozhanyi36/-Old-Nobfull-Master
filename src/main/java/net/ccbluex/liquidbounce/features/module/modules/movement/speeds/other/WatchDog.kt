package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other


import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.MovementUtils.isMoving
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.potion.Potion
import java.util.*

class WatchDog : SpeedMode("WatchDog") {
    var lastDist: Double = 0.0
    var speed2: Double = 0.0
    var stage: Int = 1
    private var stage1 = false

    fun getBaseMoveSpeed(): Double {
        var baseSpeed = 0.30
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            MovementUtils.strafe(0.48F)
        return baseSpeed
    }
    fun getJumpEffect(): Int {
        return if (mc.thePlayer.isPotionActive(Potion.jump)) mc.thePlayer.getActivePotionEffect(Potion.jump).amplifier + 1 else 0
    }
    fun setMotion(e: MoveEvent, speed: Double) {
        var forward = mc.thePlayer.movementInput.moveForward.toDouble()
        var strafe = mc.thePlayer.movementInput.moveStrafe.toDouble()
        var rotationYaw = mc.thePlayer.rotationYaw
        if (mc.thePlayer.moveForward < 0f) rotationYaw += 180f
        if (mc.thePlayer.moveStrafing > 0f) rotationYaw -= (90f * forward).toFloat()
        if (mc.thePlayer.moveStrafing < 0f) rotationYaw += (90f * forward).toFloat()
        var yaw = mc.thePlayer.rotationYaw.toDouble()
        if (forward == 0.0 && strafe == 0.0) {
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (if (forward > 0.0) -44 else 44).toDouble()
                } else if (strafe < 0.0) {
                    yaw += (if (forward > 0.0) 44 else -44).toDouble()
                }
                strafe = 0.0
                if (forward > 0.0) {
                    forward = 1.0
                } else if (forward < 0.0) {
                    forward = -1.0
                }
            }
            e.x =
                forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f))
            e.z = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0f))
                    - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)))
        }
    }

    override fun onEnable() {
        stage = 2
        super.onEnable()
    }

    override fun onDisable() {
        speed2 = getBaseMoveSpeed()
        stage = 2
    }
    fun onMotion() {
        val thePlayer = mc.thePlayer ?: return
        val xDist: Double = thePlayer.posX - thePlayer.prevPosX
        val zDist: Double = thePlayer.posZ - thePlayer.prevPosZ
        lastDist = Math.sqrt(xDist * xDist + zDist * zDist)
    }

    override fun onUpdate() {
    }
    private val timer = MSTimer()
    override fun onMove(event: MoveEvent) {
        val thePlayer = mc.thePlayer ?: return
        val speed1 = LiquidBounce.moduleManager.getModule(Speed::class.java) as Speed
        if (stage1) {
            if (mc.thePlayer.fallDistance <= 0.1) {
                mc.timer.timerSpeed = speed.HypixelTimerValue.get()
            }
            if (timer.hasTimePassed(600)) {
                timer.reset()
                stage1 = !stage1
            }
        } else {
            if (mc.thePlayer.fallDistance > 0.1 && mc.thePlayer.fallDistance < 1.3) {
                mc.timer.timerSpeed = speed.HypixelDealyTimerValue.get()
            }
            if (timer.hasTimePassed(400)) {
                timer.reset()
                stage1 = !stage1
            }
        }
        if (mc.thePlayer.fallDistance >= 1.3f) {
            mc.timer.timerSpeed = 1f
        }
        if (stage > 0 && !thePlayer.isInWater) {
            val stoptick = 0
            if (stoptick > 0) {
                speed2 = getBaseMoveSpeed()
                return
            }

            if (stage == 1 && thePlayer.onGround && isMoving())
                stage += 1

            if (stage == 2 && thePlayer.onGround && isMoving()) {
                event.y = (0.399999986886975+ getJumpEffect() * .1).also {
                    mc.thePlayer!!.motionY = it
                }

            } else
                if (stage >= 3) {
                    if (mc.thePlayer!!.isCollidedVertically) {
                        speed2 = getBaseMoveSpeed()
                        lastDist = 0.0
                        stage = 0
                    }
                }
            getHypixelBest()
        } else {
            stage = 0
        }
        if (isMoving()) {
            setMotion(event, speed2)
        } else {
            setMotion(event, 0.0)
            stage = 0
        }
        ++stage

    }


    private fun getHypixelBest() {
        val thePlayer = mc.thePlayer ?: return
        var slowdown = false

        when (stage) {
            1 -> {
                stage = 2
            }
            2 -> {
                if (thePlayer.onGround && isMoving())
                    speed2 *= 1.7
            }
            3 -> {
                speed2 += Random().nextDouble() / 4799
                val difference: Double = 0.66 * (lastDist - getBaseMoveSpeed())
                speed2 = lastDist - difference
                speed2 -= Random().nextDouble() / 4799
            }
            else -> {
                slowdown = thePlayer.fallDistance > 0.0

                if (!mc.theWorld!!.getCollidingBoundingBoxes(
                        thePlayer,
                        thePlayer.entityBoundingBox.offset(0.0, thePlayer.motionY, 0.0)
                    ).isEmpty() || thePlayer.isCollidedVertically && thePlayer.onGround
                ) {
                    stage = 1
                }
                speed2 = lastDist - lastDist / 159
            }
        }
        speed2 = Math.max(speed2 - if (slowdown) Math.sqrt(lastDist * lastDist + speed2 * speed2) * 0.012 else 0.02 * lastDist, getBaseMoveSpeed())

        if (slowdown) {
            speed2 *= 1.0 - (lastDist / 50)
        }
    }
}