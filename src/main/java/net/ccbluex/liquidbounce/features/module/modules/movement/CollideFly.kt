package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.network.Packet
import net.minecraft.network.play.client.*
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import java.util.*

@ModuleInfo(name = "CollideFly", description = "Collide fly.", category = ModuleCategory.MOVEMENT)
class CollideFly : Module() {
    private val speed = FloatValue("Speed", 2.5f, 0f, 10f)
    private val motionY = FloatValue("MotionY", 1.8f, 0f, 5f)
    private val UP = FloatValue("Up", 1f, 0f, 5f)
    private val Down = FloatValue("Down", 1.2f, 0f, 5f)
    private val NormalY = FloatValue("NormalY", 0.05f, 0.01f, 0.1f)
    private val delay = IntegerValue("Delay", 2, 0, 10)
    var doAsFly = false
    private val packets: MutableList<Packet<*>> = ArrayList()
    var stage = 0.0
    var timer = 0.0
    var y = 0.0
    override fun onEnable() {
        y = mc.thePlayer!!.posY.toInt().toDouble()
        timer = 999.0
    }

    override fun onDisable() {
        packets.clear()
        mc.timer.timerSpeed = 1f
        doAsFly = false
        if (packets.size > 0) {
            for (packet in packets) {
                mc.thePlayer!!.sendQueue.addToSendQueue(packet)
            }
            packets.clear()
        }

    }

    fun move() {
        if (MovementUtils.isMoving()) {
            if (mc.thePlayer!!.posY <= y) {
                mc.thePlayer!!.motionY = motionY.get().toDouble()
            }
            if (mc.gameSettings.keyBindJump.isKeyDown) {
                mc.thePlayer!!.motionY = UP.get().toDouble()
            }
            var dir = mc.thePlayer!!.rotationYaw / 180 * Math.PI
            if (mc.thePlayer!!.motionY < 0) mc.thePlayer!!.motionY = (0 - NormalY.get().toDouble())
            mc.thePlayer!!.motionX = -Math.sin(dir) * speed.get().toDouble()
            mc.thePlayer!!.motionZ = Math.cos(dir) * speed.get().toDouble()
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (!doAsFly) timer++
        if (timer > delay.get()) {
            timer = 0.0
            doAsFly = true
            stage = 0.0
            move()
        }
        if (stage >= 1) {
            doAsFly = false
            if (packets.size > 0) {
                for (packet in packets) {
                    mc.thePlayer!!.sendQueue.addToSendQueue(packet)
                }
                packets.clear()
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (!doAsFly) return
        val packet = event.packet
        if (packet is C04PacketPlayerPosition || packet is C06PacketPlayerPosLook ||
            packet is C08PacketPlayerBlockPlacement ||
            packet is C0APacketAnimation ||
            packet is C0BPacketEntityAction || packet is C02PacketUseEntity
        ) {
            event.cancelEvent()
            packets.add(packet)
            stage++
            if (packet is C03PacketPlayer) {
                packet.onGround = true
            }
        }
    }
}