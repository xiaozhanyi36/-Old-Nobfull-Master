package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.client.C16PacketClientStatus
import java.util.*

@ModuleInfo(name = "AutoLeave", category = ModuleCategory.COMBAT, description = "idk.")
class AutoLeave : Module() {
    var check = true
    private val healthValue = FloatValue("Health", 8f, 0f, 20f)
    private val modeValue = ListValue("Mode", arrayOf("Quit", "InvalidPacket", "SelfHurt", "IllegalChat","PitNew"), "Quit")
    private val leavemessageValue = TextValue("LeaveMessage", "[NobleFull] Bye! You have a L client,Get good get NobleFull Client.")

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        if (mc.thePlayer.health <= healthValue.get() ) {
            when (modeValue.get().toLowerCase()) {
                "quit" -> {
                    mc.theWorld.sendQuittingDisconnectingPacket()
                    state = false
                }
                "invalidpacket" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, !mc.thePlayer.onGround))
                    state = false
                }
                "selfhurt" -> {
                    mc.netHandler.addToSendQueue(C02PacketUseEntity(mc.thePlayer, C02PacketUseEntity.Action.ATTACK))
                    state = false
                }
                "illegalchat" -> {
                    mc.thePlayer.sendChatMessage(Random().nextInt().toString() + "§§§" + Random().nextInt())
                    state = false
                }
                "pitnew" -> {

                    LiquidBounce.moduleManager[Fly::class.java]!!.state = false
                    LiquidBounce.moduleManager[Speed::class.java]!!.state = false
                    for (i in 0..3) {
                        val armorSlot = 3 - i
                        move(8 - armorSlot, true)
                    }
                    mc.thePlayer.sendChatMessage(leavemessageValue.get())
                    mc.thePlayer.sendChatMessage("/hub")
                    LiquidBounce.moduleManager[KillAura::class.java]!!.state = false
                    LiquidBounce.moduleManager[Criticals::class.java]!!.state = false
                    LiquidBounce.moduleManager[Velocity::class.java]!!.state = false
                }
            }
        }
    }
    private fun move(item: Int, isArmorSlot: Boolean) {
        if (item != -1) {
            val openInventory = mc.currentScreen !is GuiInventory
            if (openInventory) mc.netHandler.addToSendQueue(C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, if (isArmorSlot) item else if (item < 9) item + 36 else item, 0, 1, mc.thePlayer)
            if (openInventory) mc.netHandler.addToSendQueue(C0DPacketCloseWindow())
        }
    }
    @EventTarget
    fun onWorld(event: WorldEvent?) {
        check = true
    }

    override val tag: String
        get() = modeValue.get()
}
