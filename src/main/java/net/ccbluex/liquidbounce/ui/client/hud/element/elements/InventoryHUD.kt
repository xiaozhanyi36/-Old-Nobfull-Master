package net.ccbluex.liquidbounce.ui.client.hud.element.elements


import net.ccbluex.liquidbounce.features.module.modules.color.Rainbow.array
import net.ccbluex.liquidbounce.features.module.modules.color.Rainbow.rainbowSpeed
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.RenderU
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.MiLiBlueUtil
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.*
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.Render
import org.lwjgl.opengl.GL11
import java.awt.Color


@ElementInfo(name = "InventoryHUD", blur = true)
class InventoryHUD : Element(300.0, 50.0, 1F, Side(Side.Horizontal.RIGHT, Side.Vertical.UP)) {
    private val bgAlphaValue = IntegerValue("BackgroundAlpha", 60, 0, 255)
    private val rectAlpha = IntegerValue("RectAlpha",150,0,255)
    private val rainbowIndex = IntegerValue("RainbowIndex", 1, 1, 20)
    private val titlecolor = ListValue("TitleColor", arrayOf("Normal","Rainbow"),"Normal")
    private val shadowwidth = FloatValue("ShadowWidth", 226.26262F,0F,700F)
    private val shadowHeight = FloatValue("ShadowHeight",3F,-50F,50F)
    private val title = BoolValue("Title",true)
    private val rect = BoolValue("Rect",true)
    private val shadow = BoolValue("Shadow",true)
    private val fontValue = FontValue("Font", Fonts.fontBold40)

    override fun drawElement(partialTicks: Float): Border {
        val fontHeight = Fonts.fontBold40.FONT_HEIGHT
        val backgroundColor = ColorUtils.white(bgAlphaValue.get())
        val font = fontValue.get()
        val startY = if (title.get()) {
            -(4 + font.FONT_HEIGHT)
        } else {
            0
        }.toFloat()
        if (shadow.get()) {
            MiLiBlueUtil.drawShadow(-25, -25, shadowwidth.get(), shadowHeight.get() + fontHeight + font.FONT_HEIGHT * 3 + 80F)
        }
        // draw rect
        RenderUtils.drawRect(0F, startY, 174F, 66F, backgroundColor)
        if(rect.get()){

            when (titlecolor.get().lowercase()) {
            "normal" -> RenderUtils.drawRect(0f, startY, 174f, 0f, ColorUtils.black(rectAlpha.get()))
            "rainbow" -> RenderUtils.drawRect(0f, startY, 174f, 0f, ColorUtils.hslRainbow(rainbowIndex.get(), indexOffset = 100 * rainbowSpeed.get()).rgb) }
        }
      if(title.get()){
          font.drawString("Inventory List", 3, -(font.FONT_HEIGHT), Color.WHITE.rgb)
      }
        drawEmpty()

        // render item
        GL11.glPushMatrix()
        RenderHelper.enableGUIStandardItemLighting()
        renderInv(9, 17, 6, 6, font)
        renderInv(18, 26, 6, 24, font)
        renderInv(27, 35, 6, 42, font)
        RenderHelper.disableStandardItemLighting()
        GlStateManager.enableAlpha()
        GlStateManager.disableBlend()
        GlStateManager.disableLighting()
        GlStateManager.disableCull()
        GL11.glPopMatrix()

        return Border(0F, startY, 174F, 66F)
    }

    override fun drawElement(): Border {
        return drawElement()
    }

    /**
     * render single line of inventory
     * @param endSlot slot+9
     */
    private fun renderInv(slot: Int, endSlot: Int, x: Int, y: Int, font: FontRenderer) {
        var xOffset = x
        for (i in slot..endSlot) {
            xOffset += 18
            val stack = mc.thePlayer.inventoryContainer.getSlot(i).stack ?: continue

            mc.renderItem.renderItemAndEffectIntoGUI(stack, xOffset - 18, y)
            mc.renderItem.renderItemOverlays(font, stack, xOffset - 18, y)
        }
    }

    // Check Inv
    private fun drawEmpty() {
        var eee = true

        for (i in 9..35) {
            val stack = mc.thePlayer.inventoryContainer.getSlot(i).stack

            if (stack != null) {
                eee = false
            }
        }

        if (eee) {
            val font = fontValue.get()

            font.drawString(
                "Your Inventory is empty...",
                87f - (font.getStringWidth("Your Inventory is empty...") / 2),
                33F - (font.FONT_HEIGHT / 2),
                Color.WHITE.rgb,
                true,
            )
        }
    }
}