package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import by.radioegor146.nativeobfuscator.Native
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.color.HUD
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.MiLiBlueUtil
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import java.awt.Color
import java.text.DecimalFormat


/**
 * @author ChengFeng
 * CurrentSession
 */
@ElementInfo(name = "SessionInfo", blur = true)
@Native
 class SessionInfo : Element(200.0, 100.0, 1F, Side(Side.Horizontal.RIGHT, Side.Vertical.UP)) {
    private val resetMode = ListValue("KillsResetMode", arrayOf("None", "GameEnd", "Respawn"), "GameEnd")
    private val shadow = BoolValue("Shadow", true)
    private var addX = 0f
    private var addY = 0f

    override fun drawElement(partialTicks: Float): Border {

        val font = Fonts.font35
        val icon = Fonts.fontIcon
        val color = Color.WHITE.rgb
        val fontHeight = Fonts.font40.FONT_HEIGHT
        val format = DecimalFormat("#.##")

        if (shadow.get()) {
            MiLiBlueUtil.drawShadow(-25, -25, 200F, 3F + fontHeight + font.FONT_HEIGHT * 3 + 80F)
        }

        RenderUtils.drawRect(0F, 0F, 150F, 3F + fontHeight + font.FONT_HEIGHT * 3 + 30F, ColorUtils.black(100))
        if (LiquidBounce.theme == "Custom") {
            RenderUtils.drawRect(0f, 0f, 150f, 1f, HUD.getClientCustomColor().rgb)
        } else RenderUtils.drawGradientSideways(0F, 0F, 150F, 1F, ColorUtils.hslRainbow(10, indexOffset = 30).rgb, ColorUtils.hslRainbow(10, indexOffset = 60).rgb)

        // title
        Fonts.font40.drawString("Session Info", 5F, 3F, color)

        icon.drawString("c", 5F, 3F + fontHeight + 6F, color)
        icon.drawString("b", 5F, 3F + fontHeight + font.FONT_HEIGHT + 11F, color)
        icon.drawString("d", 5F, 3F + fontHeight + font.FONT_HEIGHT * 2 + 16F, color)
        icon.drawString("a", 5F, 3F + fontHeight + font.FONT_HEIGHT * 3 + 21F, color)

        font.drawString("Played Time", 7F + icon.getStringWidth("c"), 3F + fontHeight + 5F, color)
        font.drawString("Speed", 7F + icon.getStringWidth("b"), 3F + fontHeight + font.FONT_HEIGHT + 10F, color)
        font.drawString("Ping", 7F + icon.getStringWidth("d"), 3F + fontHeight + font.FONT_HEIGHT * 2 + 15F, color)
        font.drawString("Kills", 7F + icon.getStringWidth("a"), 3F + fontHeight + font.FONT_HEIGHT * 3 + 20F, color)

        // info
        font.drawString(
            LiquidBounce.combatManager.playedTime,
            150 - font.getStringWidth(LiquidBounce.combatManager.playedTime) - 5F,
            3F + fontHeight + 5F,
            color
        )
        font.drawString(
            format.format(MovementUtils.bps),
            150 - font.getStringWidth(format.format(MovementUtils.bps)) - 5F,
            3F + fontHeight + font.FONT_HEIGHT + 10F,
            color
        )
        if (mc.isSingleplayer) {
            font.drawString(
                "0ms (Singleplayer)",
                150 - font.getStringWidth("0ms (Singleplayer)") - 5F,
                3F + fontHeight + font.FONT_HEIGHT * 2 + 15F,
                color
            )
        } else font.drawString(
            mc.netHandler.getPlayerInfo(mc.thePlayer.uniqueID).responseTime.toString(),
            150 - font.getStringWidth(mc.netHandler.getPlayerInfo(mc.thePlayer.uniqueID).responseTime.toString()) - 5F,
            3F + fontHeight + font.FONT_HEIGHT * 2 + 15F,
            color
        )
        font.drawString(
            if (resetMode.get() != "None") LiquidBounce.combatManager.killedEntities.toString() else LiquidBounce.combatManager.allKilledEntities.toString(),
            150 - (if (resetMode.get() != "None") font.getStringWidth(LiquidBounce.combatManager.killedEntities.toString()) else font.getStringWidth(LiquidBounce.combatManager.allKilledEntities.toString())) - 5F,
            3F + fontHeight + font.FONT_HEIGHT * 3 + 20F,
            color
        )


        when (resetMode.get().lowercase()) {
            "none" -> LiquidBounce.combatManager.resetKillsMode = 0
            "gameend" -> LiquidBounce.combatManager.resetKillsMode = 1
            "respawn" -> LiquidBounce.combatManager.resetKillsMode = 2
        }

        return Border(0F, 0F, 150F, 3F + fontHeight + font.FONT_HEIGHT * 3 + 30F)
    }

    override fun drawElement(): Border {
        return drawElement()
    }
}