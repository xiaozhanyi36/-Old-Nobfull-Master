package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.utils.RenderWings
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(
    name = "Wings",
    description = "Show the wings for player.",
    category = ModuleCategory.RENDER
)
class Wings : Module() {
    private val wings = RenderWings()
    @EventTarget
    fun onRender3D(event : Render3DEvent) {
        if(!mc.thePlayer.isInvisible)
            LiquidBounce.moduleManager[PlayerEdit::class.java]?.size?.let { wings.renderWings(mc.thePlayer,event.partialTicks, it.get()) }
    }
}