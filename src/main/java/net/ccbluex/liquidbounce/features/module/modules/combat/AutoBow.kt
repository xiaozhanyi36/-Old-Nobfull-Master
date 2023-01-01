// Destiny made by ChengFeng
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.init.Items
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing

@ModuleInfo(name = "AutoBow", category = ModuleCategory.COMBAT, description = "Automatically shoots an arrow whenever your bow is fully loaded")
class AutoBow : Module() {

    private val waitForBowAimbot = BoolValue("WaitForBowAimbot", true)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val bowAimbot = LiquidBounce.moduleManager[BowAimbot::class.java]!!

        if (mc.thePlayer.isUsingItem && mc.thePlayer.heldItem?.item == Items.bow &&
            mc.thePlayer.itemInUseDuration > 20 && (!waitForBowAimbot.get() || !bowAimbot.state || bowAimbot.hasTarget())
        ) {
            mc.thePlayer.stopUsingItem()
            mc.netHandler.addToSendQueue(
                C07PacketPlayerDigging(
                    Action.RELEASE_USE_ITEM,
                    BlockPos.ORIGIN,
                    EnumFacing.DOWN
                )
            )
        }
    }
}
