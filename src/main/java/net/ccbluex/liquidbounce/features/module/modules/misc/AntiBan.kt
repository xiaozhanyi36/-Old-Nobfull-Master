
package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.misc.HttpUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.network.play.server.*
import kotlin.concurrent.thread

@ModuleInfo(
    name = "AntiBan",
    description = "Anti staff on BlocksMC. Automatically leaves a map if detected known staffs.",
    category = ModuleCategory.MISC
)
class AntiBan : Module() {

    private var obStaffs = "_"
    private var detected = false
    private var totalCount = 0
    private var finishedCheck = false

    private var updater = MSTimer()

    private var staffMain: String = "https://add-my-brain.exit-scammed.repl.co/staff/"
    private var staffFallback: String = "https://wysi-foundation.github.io/LiquidCloud/LiquidBounce/staffs.txt"

    override fun onInitialize() {
        thread {
            try {
                while (!finishedCheck) {
                    obStaffs = HttpUtils.getHttps(staffMain)

                    if (obStaffs.contains("checking", true)) {
                        println("am waiting for a while, the server is checking")
                        updater.reset()
                        while (!updater.hasTimePassed(30000L)) {
                        }
                    } else {
                        totalCount = obStaffs.count { it.isWhitespace() }
                        println("[Staff/main] $obStaffs")
                        finishedCheck = true
                    }
                }
                println("finished checking, closing thread...")
            } catch (e: Exception) {
                e.printStackTrace()
                println("switching to local staff source instead...")

                obStaffs = HttpUtils.get(staffFallback)
                totalCount = obStaffs.count { it.isWhitespace() }
                println("[Staff/fallback] $obStaffs")
            }
        }
    }

    override fun onEnable() {
        detected = false
    }

    @EventTarget
    fun onWorld(e: WorldEvent) {
        detected = false
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (mc.theWorld == null || mc.thePlayer == null) return

        val packet = event.packet // smart convert
        if (packet is S1DPacketEntityEffect) {
            val entity = mc.theWorld.getEntityByID(packet.entityId)
            if (entity != null && (obStaffs.contains(entity.name) || obStaffs.contains(entity.displayName.unformattedText))) {
                if (!detected) {
                    LiquidBounce.hud.addNotification(
                        Notification(
                            name,
                            "${entity.name} detected through effect packet!",
                            NotifyType.ERROR
                        )
                    )
                    mc.thePlayer.sendChatMessage("/leave")
                    detected = true
                }
            }
        }
        if (packet is S18PacketEntityTeleport) {
            val entity = mc.theWorld.getEntityByID(packet.entityId)
            if (entity != null && (obStaffs.contains(entity.name) || obStaffs.contains(entity.displayName.unformattedText))) {
                if (!detected) {
                    LiquidBounce.hud.addNotification(
                        Notification(
                            name,
                            "${entity.name} detected through teleportation packet!",
                            NotifyType.ERROR
                        )
                    )
                    mc.thePlayer.sendChatMessage("/leave")
                    detected = true
                }
            }
        }
        if (packet is S20PacketEntityProperties) {
            val entity = mc.theWorld.getEntityByID(packet.entityId)
            if (entity != null && (obStaffs.contains(entity.name) || obStaffs.contains(entity.displayName.unformattedText))) {
                if (!detected) {
                    LiquidBounce.hud.addNotification(
                        Notification(
                            name,
                            "${entity.name} detected through properties packet!",
                            NotifyType.ERROR
                        )
                    )
                    mc.thePlayer.sendChatMessage("/leave")
                    detected = true
                }
            }
        }
        if (packet is S0BPacketAnimation) {
            val entity = mc.theWorld.getEntityByID(packet.entityID)
            if (entity != null && (obStaffs.contains(entity.name) || obStaffs.contains(entity.displayName.unformattedText))) {
                if (!detected) {
                    LiquidBounce.hud.addNotification(
                        Notification(
                            name,
                            "${entity.name} detected through animation packet!",
                            NotifyType.ERROR
                        )
                    )
                    mc.thePlayer.sendChatMessage("/leave")
                    detected = true
                }
            }
        }
        if (packet is S14PacketEntity) {
            val entity = packet.getEntity(mc.theWorld)

            if (entity != null && (obStaffs.contains(entity.name) || obStaffs.contains(entity.displayName.unformattedText))) {
                if (!detected) {
                    LiquidBounce.hud.addNotification(
                        Notification(
                            name,
                            "${entity.name} detected through update packet!",
                            NotifyType.ERROR
                        )
                    )
                    mc.thePlayer.sendChatMessage("/leave")
                    detected = true
                }
            }
        }
        if (packet is S19PacketEntityStatus) {
            val entity = packet.getEntity(mc.theWorld)

            if (entity != null && (obStaffs.contains(entity.name) || obStaffs.contains(entity.displayName.unformattedText))) {
                if (!detected) {
                    LiquidBounce.hud.addNotification(
                        Notification(
                            name,
                            "${entity.name} detected through status packet!",
                            NotifyType.ERROR
                        )
                    )
                    mc.thePlayer.sendChatMessage("/leave")
                    detected = true
                }
            }
        }
        if (packet is S19PacketEntityHeadLook) {
            val entity = packet.getEntity(mc.theWorld)

            if (entity != null && (obStaffs.contains(entity.name) || obStaffs.contains(entity.displayName.unformattedText))) {
                if (!detected) {
                    LiquidBounce.hud.addNotification(
                        Notification(
                            name,
                            "${entity.name} detected through head packet!",
                            NotifyType.ERROR
                        )
                    )
                    mc.thePlayer.sendChatMessage("/leave")
                    detected = true
                }
            }
        }
        if (packet is S49PacketUpdateEntityNBT) {
            val entity = packet.getEntity(mc.theWorld)

            if (entity != null && (obStaffs.contains(entity.name) || obStaffs.contains(entity.displayName.unformattedText))) {
                if (!detected) {
                    LiquidBounce.hud.addNotification(
                        Notification(
                            name,
                            "${entity.name} detected through nbt packet!",
                            NotifyType.ERROR
                        )
                    )
                    mc.thePlayer.sendChatMessage("/leave")
                    detected = true
                }
            }
        }
    }

    override val tag: String
        get() = "Staffs:${totalCount}"
}