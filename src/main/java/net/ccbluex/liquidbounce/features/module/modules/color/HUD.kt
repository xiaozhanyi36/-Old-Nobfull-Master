// Destiny made by ChengFeng
package net.ccbluex.liquidbounce.features.module.modules.color

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.client.button.*
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.SystemUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.Random


@ModuleInfo(name = "HUD", category = ModuleCategory.COLOR, array = false, defaultOn = true)
object HUD : Module() {
    private val buttonValue = ListValue("Button", arrayOf("Alpha", "Slide", "Rise", "Plus", "Vanilla"), "Rise")

    private val waterMarkValue = BoolValue("WaterMark", true)
    private val waterMarkMode = ListValue("WaterMarkColorMode", arrayOf("Normal", "Fade", "Rainbow"), "Normal").displayable { waterMarkValue.get() }
    val inventoryParticle = BoolValue("InvParticle", false)
    private val blurValue = BoolValue("InvBlur", false)
    private val noAchievementsValue = BoolValue("NoAchievements", true)
    private val noBobValue = BoolValue("NoBob", false)
    private val healValue = BoolValue("Health", true)
    private val WaterMarkText = TextValue("WaterMarkText","NobleFull")
    val clientColorMode = ListValue("ClientColorMode", arrayOf("Custom", "Rainbow"), "Custom")
     val displayString = TextValue("ScoreboardDisplayText", "NobleFull@2022")
    private val clientR = IntegerValue("ClientRed", 255, 0, 255).displayable { clientColorMode.equals("Custom") }
    private val clientG = IntegerValue("ClientGreen", 255, 0, 255).displayable { clientColorMode.equals("Custom") }
    private val clientB = IntegerValue("ClientBlue", 255, 0, 255).displayable { clientColorMode.equals("Custom") }
    private val clientA = IntegerValue("ClientAlpha", 255, 0, 255).displayable { clientColorMode.equals("Custom") }

    fun getClientCustomColor(): Color {
        return Color(clientR.get(), clientG.get(), clientB.get(), clientA.get())
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent) {

        if (mc.currentScreen is GuiHudDesigner) return

        LiquidBounce.hud.render(false, event.partialTicks)

        if (healValue.get()) {
            if (mc.thePlayer.health >= 0.0f && mc.thePlayer.health < 10.0f) {
                width = 3
            }
            if (mc.thePlayer.health >= 10.0f && mc.thePlayer.health < 100.0f) {
                width = 5
            }
            Fonts.font40.drawStringWithShadow(
                "" + MathHelper.ceiling_float_int(mc.thePlayer.health),
                (ScaledResolution(mc).scaledWidth / 2 - width).toFloat(),
                (ScaledResolution(mc).scaledHeight / 2 - 5).toFloat() - Fonts.font40.height - 2,
                if (mc.thePlayer.health <= 10.0f) Color(255, 0, 0).rgb else Color(0, 255, 0).rgb
            )
        }

        if (waterMarkValue.get()) {
            Fonts.fontIcon.drawString("q", 8f, 10f, Color(0, 200, 200).rgb, true)

            when (waterMarkMode.get().lowercase()) {
                "normal" -> Fonts.font40.drawString(WaterMarkText.get(), 10f + Fonts.fontIcon.getStringWidth("q"), 8f, getClientCustomColor().rgb, true)
                "fade" -> Fonts.font40.drawString(WaterMarkText.get(), 10f + Fonts.fontIcon.getStringWidth("q"), 8f, ColorUtils.fade(
                    getClientCustomColor(), 0, 1).rgb, true)
                "rainbow" -> Fonts.font40.drawRainbowString(WaterMarkText.get(), 10f + Fonts.fontIcon.getStringWidth("q"), 8f, 100, true)
            }
        }

        LiquidBounce.theme = clientColorMode.get()


    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        LiquidBounce.hud.update()

        if (noBobValue.get()) {
            mc.thePlayer.distanceWalkedModified = 0f
        }

//        if (widgetsValue.get()) {
//            val widths = ScaledResolution(mc).scaledWidth
//            val heights = ScaledResolution(mc).scaledHeight
//            val image = ResourceLocation("destiny/gui/widgets/Widget_" + RandomUtils.nextInt(1, 9) + ".png")
//            when (widgetsMode.get().lowercase()) {
//                "random" -> {
//                    drawImage(image, widths - 500, heights, 489, 512)
//                }
//
//                "custom" -> {
//                    drawImage(ResourceLocation("destiny/gui/widgets/Widget_" + widgetsType.get() + ".png"), widths - 500, heights, 489, 512)
//                }
//            }
//        }
    }

    @EventTarget
    fun onTick(event: TickEvent) {
        if (noAchievementsValue.get()) {
            mc.guiAchievement.clearAchievements()
        }
    }

    @EventTarget
    fun onScreen(event: ScreenEvent) {
        if (mc.theWorld == null || mc.thePlayer == null) {
            return
        }

        if (state && blurValue.get() && !mc.entityRenderer.isShaderActive && event.guiScreen != null && !(event.guiScreen is GuiChat || event.guiScreen is GuiHudDesigner)) {
            mc.entityRenderer.loadShader(ResourceLocation("destiny/blur.json"))
        } else if (mc.entityRenderer.shaderGroup != null && mc.entityRenderer.shaderGroup!!.shaderGroupName.contains("destiny/blur.json")) {
            mc.entityRenderer.stopUseShader()
        }
    }

    @EventTarget
    fun onKey(event: KeyEvent) {
        LiquidBounce.hud.handleKey('a', event.key)
    }

    fun getButtonRenderer(button: GuiButton): AbstractButtonRenderer? {
        return when (buttonValue.get().lowercase()) {
            "alpha" -> AlphaButtonRenderer(button)
            "slide" -> SlideButtonRenderer(button)
            "rise" -> RiseButtonRenderer(button)
            "plus" -> PlusButtonRenderer(button)
            else -> null // vanilla or unknown
        }
    }
}