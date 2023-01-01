// Destiny made by ChengFeng
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGUIModule.*
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.extensions.ping
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.GLUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils.*
import net.ccbluex.liquidbounce.value.*
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemTool
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.roundToInt


@ModuleInfo(name = "NameTags", category = ModuleCategory.RENDER)
class NameTags : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Simple", "Liquid", "Jello", "New"), "Simple")
    private val healthValue = BoolValue("Health", true)
    private val pingValue = BoolValue("Ping", true)
    private val distanceValue = BoolValue("Distance", false)
    private val armorValue = BoolValue("Armor", true)
    private val clearNamesValue = BoolValue("ClearNames", true)
    private val fontValue = FontValue("Font", Fonts.font40)
    private val borderValue = BoolValue("Border", true)
    private val jelloColorValue = BoolValue("HPColor", true).displayable { modeValue.equals("Jello") }
    private val jelloAlphaValue = IntegerValue("Alpha", 170, 0, 255).displayable { modeValue.equals("Jello") }
    private val scaleValue = FloatValue("Scale", 1F, 1F, 4F)

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (modeValue.get() == "New") {
            for (o in mc.theWorld.playerEntities) {
                val renderManager = mc.renderManager
                val p = o as EntityPlayer
                if (p !== mc.thePlayer) {
                    val pX = (p.lastTickPosX + (p.posX - p.lastTickPosX) * mc.timer.renderPartialTicks
                            - renderManager.renderPosX)
                    val pY = (p.lastTickPosY + (p.posY - p.lastTickPosY) * mc.timer.renderPartialTicks
                            - renderManager.renderPosY)
                    val pZ = (p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * mc.timer.renderPartialTicks
                            - renderManager.renderPosZ)
                    renderNewNameTag(p, p.name, pX, pY, pZ)
                }
            }
        } else {
            for (entity in mc.theWorld.loadedEntityList) {
                if (EntityUtils.isSelected(entity, false)) {
                    renderNameTag(
                        entity as EntityLivingBase,
                        if (!modeValue.equals("Liquid") && AntiBot.isBot(entity)) {
                            "§e"
                        } else {
                            ""
                        } +
                                if (clearNamesValue.get()) {
                                    entity.name
                                } else {
                                    entity.getDisplayName().unformattedText
                                }
                    )
                }
            }
        }
    }

    private fun renderNameTag(entity: EntityLivingBase, tag: String) {
        // Set fontrenderer local
        val fontRenderer = fontValue.get()

        // Push
        glPushMatrix()

        // Translate to player position
        val renderManager = mc.renderManager
        val timer = mc.timer

        glTranslated( // Translate to player position with render pos and interpolate it
            entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX,
            entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY + entity.eyeHeight.toDouble() + 0.55,
            entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ
        )

        // Rotate view to player
        glRotatef(-mc.renderManager.playerViewY, 0F, 1F, 0F)
        glRotatef(mc.renderManager.playerViewX, 1F, 0F, 0F)

        // Scale
        var distance = mc.thePlayer.getDistanceToEntity(entity) / 4F

        if (distance < 1F) {
            distance = 1F
        }

        val scale = (distance / 150F) * scaleValue.get()

        // Disable lightning and depth test
        disableGlCap(GL_LIGHTING, GL_DEPTH_TEST)

        // Enable blend
        enableGlCap(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        // Draw nametag
        when (modeValue.get().lowercase()) {
            "simple" -> {
                val healthPercent = (entity.health / entity.maxHealth).coerceAtMost(1F)
                val width = fontRenderer.getStringWidth(tag).coerceAtLeast(30) / 2
                val maxWidth = width * 2 + 12F

                glScalef(-scale * 2, -scale * 2, scale * 2)
                drawRect(
                    -width - 6F,
                    -fontRenderer.FONT_HEIGHT * 1.7F,
                    width + 6F,
                    -2F,
                    Color(0, 0, 0, jelloAlphaValue.get())
                )
                drawRect(
                    -width - 6F,
                    -2F,
                    -width - 6F + (maxWidth * healthPercent),
                    0F,
                    ColorUtils.healthColor(entity.health, entity.maxHealth, jelloAlphaValue.get())
                )
                drawRect(
                    -width - 6F + (maxWidth * healthPercent),
                    -2F,
                    width + 6F,
                    0F,
                    Color(0, 0, 0, jelloAlphaValue.get())
                )
                fontRenderer.drawString(
                    tag,
                    (-fontRenderer.getStringWidth(tag) * 0.5F).toInt(),
                    (-fontRenderer.FONT_HEIGHT * 1.4F).toInt(),
                    Color.WHITE.rgb
                )
            }


            "liquid" -> {
                // Modify tag
                val bot = AntiBot.isBot(entity)
                val nameColor =
                    if (bot) "§3" else if (entity.isInvisible) "§6" else if (entity.isSneaking) "§4" else "§7"
                val ping = entity.ping

                val distanceText =
                    if (distanceValue.get()) "§7 [§a${mc.thePlayer.getDistanceToEntity(entity).roundToInt()}§7]" else ""
                val pingText =
                    if (pingValue.get() && entity is EntityPlayer) (if (ping > 200) "§c" else if (ping > 100) "§e" else "§a") + ping + "ms §7" else ""
                val healthText = if (healthValue.get()) "§7 [§f" + entity.health.toInt() + "§c❤§7]" else ""
                val botText = if (bot) " §7[§6§lBot§7]" else ""

                val text = "$distanceText$pingText$nameColor$tag$healthText$botText"

                glScalef(-scale, -scale, scale)
                val width = fontRenderer.getStringWidth(text) / 2
                if (borderValue.get()) {
                    drawBorderedRect(
                        -width - 2F,
                        -2F,
                        width + 4F,
                        fontRenderer.FONT_HEIGHT + 2F,
                        2F,
                        Color(255, 255, 255, 90).rgb,
                        Integer.MIN_VALUE
                    )
                } else {
                    drawRect(-width - 2F, -2F, width + 4F, fontRenderer.FONT_HEIGHT + 2F, Integer.MIN_VALUE)
                }

                fontRenderer.drawString(
                    text,
                    1F + -width,
                    if (fontRenderer == Fonts.minecraftFont) 1F else 1.5F,
                    0xFFFFFF,
                    true
                )

                if (armorValue.get() && entity is EntityPlayer) {
                    for (index in 0..4) {
                        if (entity.getEquipmentInSlot(index) == null) {
                            continue
                        }

                        mc.renderItem.zLevel = -147F
                        mc.renderItem.renderItemAndEffectIntoGUI(
                            entity.getEquipmentInSlot(index),
                            -50 + index * 20,
                            -22
                        )
                    }

                    enableAlpha()
                    disableBlend()
                    enableTexture2D()
                }
            }

            "jello" -> {
                // colors
                var hpBarColor = Color(255, 255, 255, jelloAlphaValue.get())
                val name = entity.displayName.unformattedText
                if (jelloColorValue.get() && name.startsWith("§")) {
                    hpBarColor = ColorUtils.colorCode(name.substring(1, 2), jelloAlphaValue.get())
                }
                val bgColor = Color(50, 50, 50, jelloAlphaValue.get())
                val width = fontRenderer.getStringWidth(tag) / 2
                val maxWidth = (width + 4F) - (-width - 4F)
                var healthPercent = entity.health / entity.maxHealth

                // render bg
                glScalef(-scale * 2, -scale * 2, scale * 2)
                drawRect(-width - 4F, -fontRenderer.FONT_HEIGHT * 3F, width + 4F, -3F, bgColor)

                // render hp bar
                if (healthPercent > 1) {
                    healthPercent = 1F
                }

                drawRect(-width - 4F, -3F, (-width - 4F) + (maxWidth * healthPercent), 1F, hpBarColor)
                drawRect((-width - 4F) + (maxWidth * healthPercent), -3F, width + 4F, 1F, bgColor)

                // string
                fontRenderer.drawString(tag, -width, -fontRenderer.FONT_HEIGHT * 2 - 4, Color.WHITE.rgb)
                glScalef(0.5F, 0.5F, 0.5F)
                fontRenderer.drawString(
                    "Health: " + entity.health.toInt(),
                    -width * 2,
                    -fontRenderer.FONT_HEIGHT * 2,
                    Color.WHITE.rgb
                )
            }
        }
        // Reset caps
        resetCaps()

        // Reset color
        resetColor()
        glColor4f(1F, 1F, 1F, 1F)

        // Pop
        glPopMatrix()
    }

    private fun renderNewNameTag(entity: EntityPlayer, tag: String, pX: Double, pY: Double, pZ: Double) {
        var tag = tag
        var pY = pY
        val fr: FontRenderer = Fonts.font40
        var size = mc.thePlayer.getDistanceToEntity(entity) / 6.0f
        if (size < 0.8f) {
            size = 0.8f
        }
        pY += if (entity.isSneaking) 0.5 else 0.7
        var scale = size * 2.0f
        scale /= 100f
        tag = entity.displayName.unformattedText
        val bot = ""
        var team: String
        val teams = LiquidBounce.moduleManager.getModule("Teams") as Teams?
        team = if (teams!!.isInYourTeam(entity) && teams.state) {
            "\u00a7b[TEAM]"
        } else {
            ""
        }
        val renderManager = mc.renderManager
        if (team + bot == "") team = "\u00a7a"
        val lol = team + bot + tag
        val hp = "\u00a77HP:" + entity.health.toInt()
        glPushMatrix()
        glTranslatef(pX.toFloat(), pY.toFloat() + 1.4f, pZ.toFloat())
        glNormal3f(0.0f, 1.0f, 0.0f)
        glRotatef(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        glRotatef(renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        glScalef(-scale, -scale, scale)
        GLUtils.setGLCap(2896, false)
        GLUtils.setGLCap(2929, false)
        val width = Fonts.font40.getStringWidth(lol) / 2
        GLUtils.setGLCap(3042, true)
        glBlendFunc(770, 771)
        drawBorderedRectNameTag(
            (-width - 2).toFloat(), -(Fonts.font40.FONT_HEIGHT + 9).toFloat(), (width + 2).toFloat(), 2.0f, 1.0f,
            ColorUtils.reAlpha(Color.BLACK.rgb, 0.3f), ColorUtils.reAlpha(Color.BLACK.rgb, 0.3f)
        )
        glColor3f(1f, 1f, 1f)
        fr.drawString(lol, -width, -(Fonts.font40.FONT_HEIGHT + 8), -1)
        fr.drawString(hp, -Fonts.font40.getStringWidth(hp) / 2, -(Fonts.font40.FONT_HEIGHT - 2), -1)
        var COLOR = Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get()).rgb
        if (entity.health > 20) {
            COLOR = -65292
        }
        val nowHealth = ceil((entity.health + entity.absorptionAmount).toDouble()).toFloat()
        val health = nowHealth / (entity.maxHealth + entity.absorptionAmount)
        drawRect(width + health * width * 2 - width * 2 + 2, 2f, (-width - 2).toFloat(), 0.9f, COLOR)
        glPushMatrix()
        var xOffset = 0
        for (armourStack in entity.inventory.armorInventory) {
            if (armourStack != null) xOffset -= 11
        }
        val renderStack: ItemStack
        if (entity.heldItem != null) {
            xOffset -= 8
            renderStack = entity.heldItem.copy()
            if (renderStack.hasEffect()
                && (renderStack.item is ItemTool
                        || renderStack.item is ItemArmor)
            ) renderStack.stackSize = 1
            renderItemStack1(renderStack, xOffset, -35)
            xOffset += 20
        }
        for (armourStack in entity.inventory.armorInventory) if (armourStack != null) {
            val renderStack1 = armourStack.copy()
            if (renderStack1.hasEffect() && (renderStack1.item is ItemTool
                        || renderStack1.item is ItemArmor)
            ) renderStack1.stackSize = 1
            renderItemStack1(renderStack1, xOffset, -35)
            xOffset += 20
        }
        glPopMatrix()
        GLUtils.revertAllCaps()
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        glPopMatrix()
    }

    fun renderItemStack1(stack: ItemStack?, x: Int, y: Int) {
        glPushMatrix()
        glDepthMask(true)
        clear(256)
        RenderHelper.enableStandardItemLighting()
        mc.renderItem.zLevel = -150.0f
        whatTheFuckOpenGLThisFixesItemGlint()
        mc.renderItem.renderItemAndEffectIntoGUI(stack, x, y)
        mc.renderItem.renderItemOverlays(Fonts.font40, stack, x, y)
        mc.renderItem.zLevel = 0.0f
        RenderHelper.disableStandardItemLighting()
        disableCull()
        enableAlpha()
        disableBlend()
        disableLighting()
        scale(0.5, 0.5, 0.5)
        disableDepth()
        enableDepth()
        scale(2.0f, 2.0f, 2.0f)
        glPopMatrix()
    }

    private fun whatTheFuckOpenGLThisFixesItemGlint() {
        disableLighting()
        disableDepth()
        disableBlend()
        enableLighting()
        enableDepth()
        disableLighting()
        disableDepth()
        disableTexture2D()
        disableAlpha()
        disableBlend()
        enableBlend()
        enableAlpha()
        enableTexture2D()
        enableLighting()
        enableDepth()
    }

    fun drawBorderedRectNameTag(x: Float, y: Float, x2: Float, y2: Float, l1: Float, col1: Int, col2: Int) {
        drawRect(x, y, x2, y2, col2)
        val f = (col1 shr 24 and 0xFF) / 255.0f
        val f2 = (col1 shr 16 and 0xFF) / 255.0f
        val f3 = (col1 shr 8 and 0xFF) / 255.0f
        val f4 = (col1 and 0xFF) / 255.0f
        glEnable(3042)
        glDisable(3553)
        glBlendFunc(770, 771)
        glEnable(2848)
        glPushMatrix()
        glColor4f(f2, f3, f4, f)
        glLineWidth(l1)
        glBegin(1)
        glVertex2d(x.toDouble(), y.toDouble())
        glVertex2d(x.toDouble(), y2.toDouble())
        glVertex2d(x2.toDouble(), y2.toDouble())
        glVertex2d(x2.toDouble(), y.toDouble())
        glVertex2d(x.toDouble(), y.toDouble())
        glVertex2d(x2.toDouble(), y.toDouble())
        glVertex2d(x.toDouble(), y2.toDouble())
        glVertex2d(x2.toDouble(), y2.toDouble())
        glEnd()
        glPopMatrix()
        glEnable(3553)
        glDisable(3042)
        glDisable(2848)
    }
}
