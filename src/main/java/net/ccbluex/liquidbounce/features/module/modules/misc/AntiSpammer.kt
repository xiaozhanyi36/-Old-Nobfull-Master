package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.server.S02PacketChat


@ModuleInfo(name = "AntiSpammer", category = ModuleCategory.MISC, description = "Anti Spammer")
class AntiSpammer : Module() {

    private val mode = ListValue("DebugMode", arrayOf("Chat", "Notification"), "Chat")

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is S02PacketChat) {
            val message = packet.chatComponent.unformattedText.lowercase()

            if (message.contains(mc.thePlayer.name.lowercase())) return

            fun check(str: String): Boolean {
                return message.contains(str.lowercase())
            }

            if (check("加群") || check("获取") || check("购买") || check("buy") || check("get") || check("sb") || check("nmsl") || check(
                    "fuck"
                ) || check("qq")
            ) {
                event.cancelEvent()
                when (mode.get().lowercase()) {
                    "chat" -> alert("AntiSpammer canceled spammer.")

                    "notification" -> LiquidBounce.hud.addNotification(
                        Notification(
                            name,
                            "Canceled spammer.",
                            NotifyType.INFO
                        )
                    )
                }
            }
        }
    }
}