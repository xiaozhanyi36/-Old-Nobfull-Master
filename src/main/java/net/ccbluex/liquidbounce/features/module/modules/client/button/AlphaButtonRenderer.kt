package net.ccbluex.liquidbounce.features.module.modules.client.button

import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import java.awt.Color
import kotlin.math.abs

class AlphaButtonRenderer(button: GuiButton) : AbstractButtonRenderer(button) {

    override fun render(mouseX: Int, mouseY: Int, mc: Minecraft) {
        var alpha = 0

        alpha = if (button.hovered) 100 else 0

        RenderUtils.drawBorderedRect(
            button.xPosition.toFloat(),
            button.yPosition.toFloat(),
            button.xPosition + button.width.toFloat(),
            button.yPosition + button.height.toFloat(),
            2f,
            Color.WHITE.rgb,
            Color(
                255, 255, 255, alpha
            ).rgb
        )
    }
}