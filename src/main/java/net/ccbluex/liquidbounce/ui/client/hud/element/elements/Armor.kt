package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.features.module.modules.color.Rainbow.rainbowSpeed
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.Palette
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils9
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * CustomHUD Armor element
 *
 * Shows a horizontal display of current armor
 */
@ElementInfo(name = "Armor")
 class Armor(x: Double = -8.0, y: Double = 57.0, scale: Float = 1F,
                     side: Side = Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN)) : Element(x, y, scale, side) {
    private val colorModeValue = ListValue("Text-Color", arrayOf("Custom", "Rainbow", "Fade", "Astolfo", "NewRainbow","Gident"), "Custom")
    private val brightnessValue = FloatValue("Brightness", 1f, 0f, 1f)
    private val redValue = IntegerValue("Text-R", 255, 0, 255)
    private val greenValue = IntegerValue("Text-G", 255, 0, 255)
    private val blueValue = IntegerValue("Text-B", 255, 0, 255)
    private val colorRedValue2 = IntegerValue("Text-R2", 0, 0, 255)
    private val colorGreenValue2 = IntegerValue("Text-G2", 111, 0, 255)
    private val colorBlueValue2 = IntegerValue("Text-B2", 255, 0, 255)
    private val gidentspeed = IntegerValue("GidentSpeed", 100, 1, 1000)
    private val newRainbowIndex = IntegerValue("NewRainbowOffset", 1, 1, 50)
    private val astolfoRainbowOffset = IntegerValue("AstolfoOffset", 5, 1, 20)
    private val astolfoclient = IntegerValue("AstolfoRange", 109, 1, 765)
    private val astolfoRainbowIndex = IntegerValue("AstolfoIndex", 109, 1, 300)
    private val saturationValue = FloatValue("Saturation", 0.9f, 0f, 1f)
    private val speed = IntegerValue("AllSpeed", 0, 0, 400)

    /**
     * Draw element
     */
    override fun drawElement(partialTicks: Float): Border? {
        var x2 = 0
        if (mc.playerController.isNotCreative) {
            GL11.glPushMatrix()

            val renderItem = mc.renderItem
            val isInsideWater = mc.thePlayer.isInsideOfMaterial(Material.water)
            val stack1 = mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem]

            var x = 1
            var i = 0
            var y = if (isInsideWater) -10 else 0
            val colorMode = colorModeValue.get()
            val color = Color(redValue.get(), greenValue.get(), blueValue.get()).rgb
            val rainbow = colorMode.equals("Rainbow", ignoreCase = true)
            for (index in 0..3) {
                if(mc.thePlayer.inventory.armorInventory[index] != null)
                x2 += 20
            }
            if (stack1 != null) {
                if (stack1.isItemStackDamageable) {
                    x2 += 20
                }
            }
                RenderUtils9.drawRect(-2f, -4f, 2f + x2, 29f, Color(50, 50, 50, 60))

            for (index in 3 downTo 0) {
                val colorall = when {
                    rainbow -> 0
                    colorMode.equals("Fade", ignoreCase = true) -> Palette.fade2(Color(redValue.get(), greenValue.get(), blueValue.get()), (index + 1) * speed.get(),45).rgb
                    colorMode.equals("Astolfo", ignoreCase = true) -> RenderUtils9.Astolfo((index + 1) * speed.get(), saturationValue.get(), brightnessValue.get(), astolfoRainbowOffset.get(), astolfoRainbowIndex.get(), astolfoclient.get().toFloat())
                    colorMode.equals("Gident", ignoreCase = true) -> RenderUtils9.getGradientOffset(Color(redValue.get(), greenValue.get(), blueValue.get()), Color(colorRedValue2.get(), colorGreenValue2.get(), colorBlueValue2.get(),1), (Math.abs(System.currentTimeMillis() / gidentspeed.get().toDouble() + (index + 1) * speed.get()) / 10)).rgb
                    colorMode.equals("NewRainbow", ignoreCase = true) -> RenderUtils9.getRainbow((index + 1) * speed.get(),newRainbowIndex.get(), saturationValue.get(), brightnessValue.get())
                    else -> color
                }
                val stack = mc.thePlayer.inventory.armorInventory[index] ?: continue
                RenderUtils9.drawGradientSidewaysV(x.toDouble(), 0.0,x.toDouble() + 18 ,17.0,colorall,Color(140,140,140,40).rgb)
                Fonts.font26.drawStringWithShadow(((stack.maxDamage - stack.itemDamage)).toString(),x.toFloat() + 5f,20f,colorall)
                RenderUtils9.drawRect(x.toFloat(),25f,x.toFloat() + 18f,26f,Color(140,140,140,220).rgb)
                RenderUtils9.drawRect(x.toFloat(),25f,x.toFloat() + (18f * (stack.maxDamage - stack.itemDamage) / stack.maxDamage),26f,colorall)
                renderItem.renderItemIntoGUI(stack, x + 1, y)
                x += 20
            }
            if (stack1 != null) {
                if (stack1.isItemStackDamageable) {
                    val colorall = when {
                        rainbow -> 0
                        colorMode.equals("Fade", ignoreCase = true) -> Palette.fade2(Color(redValue.get(), greenValue.get(), blueValue.get()), i * speed.get(), 45).rgb
                        colorMode.equals("Astolfo", ignoreCase = true) -> RenderUtils9.Astolfo(i * speed.get(), saturationValue.get(), brightnessValue.get(), astolfoRainbowOffset.get(), astolfoRainbowIndex.get(), astolfoclient.get().toFloat())
                        colorMode.equals("Gident", ignoreCase = true) -> RenderUtils9.getGradientOffset(Color(redValue.get(), greenValue.get(), blueValue.get()), Color(colorRedValue2.get(), colorGreenValue2.get(), colorBlueValue2.get(), 1), (Math.abs(System.currentTimeMillis() / gidentspeed.get().toDouble() + i * speed.get()) / 10)).rgb
                        colorMode.equals("NewRainbow", ignoreCase = true) -> RenderUtils9.getRainbow(i * speed.get(), newRainbowIndex.get(), saturationValue.get(), brightnessValue.get())

                        else -> color
                    }
                    val stack = mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem]
                    RenderUtils9.drawGradientSidewaysV(x.toDouble(), 0.0, x.toDouble() + 18, 17.0, colorall, Color(140, 140, 140, 40).rgb)
                    Fonts.font26.drawStringWithShadow(((stack.maxDamage - stack.itemDamage)).toString(), x.toFloat() + 4f, 20f, colorall)
                    RenderUtils9.drawRect(x.toFloat(), 25f, x.toFloat() + 18f, 26f, Color(140, 140, 140, 220).rgb)
                    RenderUtils9.drawRect(x.toFloat(), 25f, x.toFloat() + (18f * (stack.maxDamage - stack.itemDamage) / stack.maxDamage), 26f, colorall)
                    renderItem.renderItemIntoGUI(stack, x + 1, y)
                    x += 20
                }
            }
            GlStateManager.enableAlpha()
            GlStateManager.disableBlend()
            GlStateManager.disableLighting()
            GlStateManager.disableCull()
            GL11.glPopMatrix()
        }
        return Border(-2f, -4f, 2f + 100, 29f)

    }

    override fun drawElement(): Border? {
        return drawElement()
    }
}