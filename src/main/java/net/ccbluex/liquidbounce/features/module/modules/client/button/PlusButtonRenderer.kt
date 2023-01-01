package net.ccbluex.liquidbounce.features.module.modules.client.button

import net.ccbluex.liquidbounce.utils.AnimationUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import java.awt.Color


class PlusButtonRenderer(button: GuiButton) : AbstractButtonRenderer(button) {
    override fun render(mouseX: Int, mouseY: Int, mc: Minecraft) {
        val delta = RenderUtils.deltaTime
        val speedDelta = 0.01f * delta
        var moveX = 0f

        moveX = if (button.hovered) {
            AnimationUtils.animate(button.width - 2.4F, moveX, speedDelta)
        } else {
            AnimationUtils.animate(0F, moveX, speedDelta)
        }

        val roundCorner = 0f.coerceAtLeast(2.4f + moveX - (button.width - 2.4f))

        RenderUtils.drawRoundedRect(
            button.xPosition.toFloat(),
            button.yPosition.toFloat(),
            button.xPosition + button.width.toFloat(),
            button.yPosition.toFloat() + button.height,
            2.4f,
            Color(0, 0, 0, 150).rgb
        )
        RenderUtils.customRounded(
            button.xPosition.toFloat(),
            button.yPosition.toFloat(),
            button.xPosition + 2.4f + moveX,
            button.yPosition.toFloat() + button.height,
            2.4f,
            roundCorner,
            roundCorner,
            2.4f,
            (if (button.enabled) Color(0, 111, 255) else Color(71, 71, 71)).rgb
        )
    }
}