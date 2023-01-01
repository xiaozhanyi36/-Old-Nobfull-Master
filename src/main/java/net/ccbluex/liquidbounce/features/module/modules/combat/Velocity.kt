// Destiny made by ChengFeng
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.server.S12PacketEntityVelocity

@ModuleInfo(name = "Velocity", category = ModuleCategory.COMBAT)
class Velocity : Module() {
    /**
     * OPTIONS
     */
    private val horizontalValue = FloatValue("Horizontal", 0F, -2F, 2F)
    private val verticalValue = FloatValue("Vertical", 0F, -2F, 2F)
    private val modeValue = ListValue(
        "Mode", arrayOf(
            "Motion", "Cancel",
            "Reduce", "Clean", "MatrixReduce", "MatrixSimple", "MatrixGround",
            "Spoof","NewHyt","Simple2","AAC5Simple","Vulcan","HytTick","CancelS12"
        ), "Cancel"
    )

    private val reduceX = FloatValue("ReduceMotionX", 0.80f, 0.00f, 1.00f).displayable { modeValue.equals("Reduce") }
    private val reduceZ = FloatValue("ReduceMotionZ", 0.80f, 0.00f, 1.00f).displayable { modeValue.equals("Reduce") }
    private val onlyGroundValue = BoolValue("OnlyGround", false)
    private val onlyCombatValue = BoolValue("OnlyCombat", false)
    private val hyttickvalue = IntegerValue("HytTick", 2, 0, 100).displayable{modeValue.get().equals("HytTick", true)}
    private val noFireValue = BoolValue("noFire", false)

    /**
     * VALUES
     */
    private var velocityTimer = MSTimer()
    private var velocityInput = false
    private var velocityTick = 0

    private var redeCount = 24

    private var templateX = 0
    private var templateY = 0
    private var templateZ = 0

    private var isMatrixOnGround = false

    override val tag: String
        get() = "${modeValue.get()} —> H:${horizontalValue.get()} Y:${verticalValue.get()}"

    override fun onDisable() {
        mc.thePlayer?.speedInAir = 0.02F
    }

    @EventTarget
    fun onUpdate() {
        if (velocityInput) {
            velocityTick++
        } else velocityTick = 0

        if (redeCount < 24) redeCount++
        if (mc.thePlayer.isInWater || mc.thePlayer.isInLava || mc.thePlayer.isInWeb) {
            return
        }

        if ((onlyGroundValue.get() && !mc.thePlayer.onGround) || (onlyCombatValue.get() && !LiquidBounce.combatManager.inCombat)) {
            return
        }
        if (noFireValue.get() && mc.thePlayer.isBurning) return

        when (modeValue.get().lowercase()) {

            "hyttick" -> {
                if(velocityTick > hyttickvalue.get()) {
                    if(mc.thePlayer.motionY > 0) mc.thePlayer.motionY = 0.0
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                    mc.thePlayer.jumpMovementFactor = -0.00001f
                    velocityInput = false
                }
                if(mc.thePlayer.onGround && velocityTick > 1) {
                    velocityInput = false
                }
            }


            "reduce" -> {
                if (mc.thePlayer.hurtTime > 0 && velocityInput) {
                    mc.thePlayer.motionX *= reduceX.get()
                    mc.thePlayer.motionZ *= reduceZ.get()
                }

                velocityInput = false
            }



            "matrixreduce" -> {
                if (mc.thePlayer.hurtTime > 0) {
                    if (mc.thePlayer.onGround) {
                        if (mc.thePlayer.hurtTime <= 6) {
                            mc.thePlayer.motionX *= 0.70
                            mc.thePlayer.motionZ *= 0.70
                        }
                        if (mc.thePlayer.hurtTime <= 5) {
                            mc.thePlayer.motionX *= 0.80
                            mc.thePlayer.motionZ *= 0.80
                        }
                    } else if (mc.thePlayer.hurtTime <= 10) {
                        mc.thePlayer.motionX *= 0.60
                        mc.thePlayer.motionZ *= 0.60
                    }
                }
            }
            "vulcan" -> {
                if(velocityTick > 10) {
                    if(mc.thePlayer.motionY > 0) mc.thePlayer.motionY = 0.0
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                    mc.thePlayer.jumpMovementFactor = -0.00001f
                    velocityInput = false
                }
                if(mc.thePlayer.onGround && velocityTick > 1) {
                    velocityInput = false
                }
            }

            "matrixground" -> {
                isMatrixOnGround = mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown
                if (isMatrixOnGround) mc.thePlayer.onGround = false
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if ((onlyGroundValue.get() && !mc.thePlayer.onGround) || (onlyCombatValue.get() && !LiquidBounce.combatManager.inCombat)) {
            return
        }

        val packet = event.packet

        if (packet is C03PacketPlayer) {
            if (modeValue.equals("Clean") && mc.thePlayer.hurtTime > 0) {
                packet.y += 0.11451419198
            }
        }
        if(packet is S12PacketEntityVelocity && modeValue.equals("CancelS12")){
            event.cancelEvent()
        }

        if (packet is S12PacketEntityVelocity) {
            if (mc.thePlayer == null || (mc.theWorld?.getEntityByID(packet.entityID) ?: return) != mc.thePlayer) {
                return
            }

            if (noFireValue.get() && mc.thePlayer.isBurning) return
            velocityTimer.reset()
            velocityTick = 0

            when (modeValue.get().lowercase()) {
                "hyttick" -> event.cancelEvent()
                "vulcan" -> {
                    velocityInput = true
                    val horizontal = 0F
                    val vertical = 0F

                    if (horizontal == 0F && vertical == 0F) {
                        event.cancelEvent()
                    }

                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                    alert("§b[Debug-NobleFull]" + "§cX:" + packet.getMotionX() + "§cY:" + packet.getMotionY()+ "§cZ:" + packet.getMotionZ())
                }


                "aac5simple" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (horizontal == -0.0000001F && vertical == 0F) {
                        event.cancelEvent()
                    }
                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                    alert("§b[Debug-NobleFull]" + "§cX:" + packet.getMotionX() + "§cY:" + packet.getMotionY()+ "§cZ:" + packet.getMotionZ())
                }
                "simple2" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()
                    if (horizontal == 0F && vertical == 0F) {
                        event.cancelEvent()
                    }
                    if (mc.thePlayer.hurtTime == 9) {
                        packet.motionX = mc.thePlayer.motionX.toInt()
                        packet.motionY = mc.thePlayer.motionY.toInt()
                        packet.motionZ = mc.thePlayer.motionZ.toInt()
                    }
                    packet.motionX *= (horizontal / 100.0).toInt()
                    packet.motionY *= (vertical / 100.0).toInt()
                    packet.motionZ *= (horizontal / 100.0).toInt()
                    alert("§b[Debug-NobleFull]" + "§cX:" + packet.getMotionX() + "§cY:" + packet.getMotionY()+ "§cZ:" + packet.getMotionZ())
                }

                "newhyt" -> {
                    packet.motionX = (packet.getMotionX() * 0.1F / 100).toInt()
                    packet.motionY = (packet.getMotionY() * 0.1F / 100).toInt()
                    packet.motionZ = (packet.getMotionZ() * 0.1F / 100).toInt()
                    event.cancelEvent()
                    alert("§b[Debug-NobleFull]" + "§cX:" + packet.getMotionX() + "§cY:" + packet.getMotionY()+ "§cZ:" + packet.getMotionZ())
                }


                "clean" -> {
                    packet.motionY = 0
                    packet.motionZ = 0
                    packet.motionX = 0
                    alert("§b[Debug-NobleFull]" + "§cX:" + packet.getMotionX() + "§cY:" + packet.getMotionY()+ "§cZ:" + packet.getMotionZ())
                }

                "motion" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (horizontal == 0F && vertical == 0F) {
                        event.cancelEvent()
                        alert("§b[Debug-NobleFull]" + "§cX:" + packet.getMotionX() + "§cY:" + packet.getMotionY()+ "§cZ:" + packet.getMotionZ())
                    }

                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                }
                "cancel" -> {
                    event.cancelEvent()
                    alert("§b[Debug-NobleFull]" + "§cX:" + packet.getMotionX() + "§cY:" + packet.getMotionY()+ "§cZ:" + packet.getMotionZ())
                }
                "matrixsimple" -> {
                    packet.motionX = (packet.getMotionX() * 0.36).toInt()
                    packet.motionZ = (packet.getMotionZ() * 0.36).toInt()
                    if (mc.thePlayer.onGround) {
                        packet.motionX = (packet.getMotionX() * 0.9).toInt()
                        packet.motionZ = (packet.getMotionZ() * 0.9).toInt()
                        alert("§b[Debug-NobleFull]" + "§cX:" + packet.getMotionX() + "§cY:" + packet.getMotionY()+ "§cZ:" + packet.getMotionZ())}
                }

                "matrixground" -> {
                    packet.motionX = (packet.getMotionX() * 0.36).toInt()
                    packet.motionZ = (packet.getMotionZ() * 0.36).toInt()
                    if (isMatrixOnGround) {
                        packet.motionY = (-628.7).toInt()
                        packet.motionX = (packet.getMotionX() * 0.6).toInt()
                        packet.motionZ = (packet.getMotionZ() * 0.6).toInt()
                    }
                }



                "reduce" -> velocityInput = true



                "spoof" -> {
                    event.cancelEvent()
                    mc.netHandler.addToSendQueue(
                        C03PacketPlayer.C04PacketPlayerPosition(
                            mc.thePlayer.posX + packet.motionX / 8000.0,
                            mc.thePlayer.posY + packet.motionY / 8000.0,
                            mc.thePlayer.posZ + packet.motionZ / 8000.0,
                            false
                        )
                    )
                }
            }
        }
    }
}
