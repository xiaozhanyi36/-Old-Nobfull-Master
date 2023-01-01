package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.extensions.drawCenteredString
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.extensions.hurtPercent
import net.ccbluex.liquidbounce.utils.extensions.skin
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.render.*
import net.ccbluex.liquidbounce.utils.render.RenderUtils.quickDrawHead
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.FontValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.DecimalFormat
import kotlin.math.roundToInt

@ElementInfo(name = "TargetHud", blur = true)
class TargetHud : Element(-46.0, -40.0, 1F, Side(Side.Horizontal.MIDDLE, Side.Vertical.MIDDLE)) {
    private val modeValue = ListValue(
        "Mode",
        arrayOf(
            "Hanabi",
            "Astolfo",
            "NovolineWTF",
            "NovolineIntent",
            "Destiny",
            "Rise",
            "Exhibition",
            "Tenacity",
            "Moon",
            "NewWTF"
        ),
        "Destiny"
    )

    // Animation
    private val animSpeedValue = IntegerValue("AnimSpeed", 10, 5, 20)
    private val hpAnimTypeValue = EaseUtils.getEnumEasingList("HpAnimType")
    private val hpAnimOrderValue = EaseUtils.getEnumEasingOrderList("HpAnimOrder")
    private val switchModeValue = ListValue("SwitchMode", arrayOf("Slide", "Zoom", "None"), "Slide")
    private val switchAnimTypeValue = EaseUtils.getEnumEasingList("SwitchAnimType")
    private val switchAnimOrderValue = EaseUtils.getEnumEasingOrderList("SwitchAnimOrder")
    private val switchAnimSpeedValue = IntegerValue("SwitchAnimSpeed", 20, 5, 40)

    // Tenacity
    private val tenacityHeightValue = IntegerValue("Tenacity-Height", 40, 40, 60)

    // Rise
    private val riseCountValue = IntegerValue("Rise-Count", 5, 1, 20)
    private val riseSizeValue = FloatValue("Rise-Size", 1f, 0.5f, 3f)
    private val riseAlphaValue = FloatValue("Rise-Alpha", 0.7f, 0.1f, 1f)
    private val riseDistanceValue = FloatValue("Rise-Distance", 1f, 0.5f, 2f)
    private val riseMoveTimeValue = IntegerValue("Rise-MoveTime", 20, 5, 40)
    private val riseFadeTimeValue = IntegerValue("Rise-FadeTime", 20, 5, 40)

    // Font
    private val fontValue = FontValue("Font", Fonts.font40)

    private var prevTarget: EntityLivingBase? = null
    private var displayPercent = 0f
    private var lastUpdate = System.currentTimeMillis()
    private val decimalFormat = DecimalFormat("0.0")
    private val decimalFormat2 = DecimalFormat("##0.0")

    private var hpEaseAnimation: Animation? = null
    private var easingHP = 0f
        get() {
            if (hpEaseAnimation != null) {
                field = hpEaseAnimation!!.value.toFloat()
                if (hpEaseAnimation!!.state == Animation.EnumAnimationState.STOPPED) {
                    hpEaseAnimation = null
                }
            }
            return field
        }
        set(value) {
            if (hpEaseAnimation == null || (hpEaseAnimation != null && hpEaseAnimation!!.to != value.toDouble())) {
                hpEaseAnimation = Animation(
                    EaseUtils.EnumEasingType.valueOf(hpAnimTypeValue.get()),
                    EaseUtils.EnumEasingOrder.valueOf(hpAnimOrderValue.get()),
                    field.toDouble(),
                    value.toDouble(),
                    animSpeedValue.get() * 100L
                ).start()
            }
        }

    private fun getHealth(entity: EntityLivingBase?): Float {
        return entity?.health ?: 0f
    }

    override fun drawElement(partialTicks: Float): Border? {
        var target = LiquidBounce.combatManager.target
        val time = System.currentTimeMillis()
        val pct = (time - lastUpdate) / (switchAnimSpeedValue.get() * 50f)
        lastUpdate = System.currentTimeMillis()

        if (mc.currentScreen is GuiHudDesigner) {
            target = mc.thePlayer
        } else if (target != null) {
            prevTarget = target
        }
        prevTarget ?: return getTBorder()

        if (target != null) {
            if (displayPercent < 1) {
                displayPercent += pct
            }
            if (displayPercent > 1) {
                displayPercent = 1f
            }
        } else {
            if (displayPercent > 0) {
                displayPercent -= pct
            }
            if (displayPercent < 0) {
                displayPercent = 0f
                prevTarget = null
                return getTBorder()
            }
        }

        easingHP = getHealth(target)

        val easedPersent = EaseUtils.apply(
            EaseUtils.EnumEasingType.valueOf(switchAnimTypeValue.get()),
            EaseUtils.EnumEasingOrder.valueOf(switchAnimOrderValue.get()),
            displayPercent.toDouble()
        ).toFloat()
        when (switchModeValue.get().lowercase()) {
            "zoom" -> {
                val border = getTBorder() ?: return null

                val percent = EaseUtils.easeInQuint(1.0 - easedPersent)
                val xAxis = ScaledResolution(mc).scaledWidth - renderX
                val yAxis = ScaledResolution(mc).scaledHeight - renderY
                GL11.glScalef(easedPersent, easedPersent, easedPersent)
                GL11.glTranslatef(
                    ((border.x2 * 0.5f * (1 - easedPersent)) / easedPersent),
                    ((border.y2 * 0.5f * (1 - easedPersent)) / easedPersent),
                    0f
                )
                GL11.glTranslated(xAxis * percent, yAxis * percent, 0.0)
            }
            "slide" -> {
                val percent = EaseUtils.easeInQuint(1.0 - easedPersent)
                val xAxis = ScaledResolution(mc).scaledWidth - renderX
                GL11.glTranslated(xAxis * percent, 0.0, 0.0)
            }
        }

        when (modeValue.get().lowercase()) {
            "novolinewtf" -> drawNovo(prevTarget!!)
            "novolineintent" -> drawNovo2(prevTarget!!)
            "astolfo" -> drawAstolfo(prevTarget!!)
            "destiny" -> drawNis(prevTarget!!)
            "rise" -> drawRise(prevTarget!!)
            "tenacity" -> drawTenacity(prevTarget!!)
            "exhibition" -> drawExhi(prevTarget!!)
            "hanabi" -> drawHanabi(prevTarget!!)
            "moon" -> drawMoon(prevTarget!!)
            "newwtf" -> drawNewWTF(prevTarget!!)
        }

        return getTBorder()
    }

    override fun drawElement(): Border {
        return drawElement()
    }

    private fun drawExhi(target: EntityLivingBase) {
        val font = fontValue.get()
        val minWidth = 140F.coerceAtLeast(45F + font.getStringWidth(target.name))

        RenderUtils.drawExhiRect(0F, 0F, minWidth, 45F)

        RenderUtils.drawRect(2.5F, 2.5F, 42.5F, 42.5F, Color(59, 59, 59).rgb)
        RenderUtils.drawRect(3F, 3F, 42F, 42F, Color(19, 19, 19).rgb)

        GL11.glColor4f(1f, 1f, 1f, 1f)
        RenderUtils.drawEntityOnScreen(22, 40, 15, target)

        font.drawString(target.name, 46, 4, -1)

        val barLength = 60F * (target.health / target.maxHealth).coerceIn(0F, 1F)
        RenderUtils.drawRect(
            45F,
            14F,
            45F + 60F,
            17F,
            BlendUtils.getHealthColor(target.health, target.maxHealth).darker().darker().darker().rgb
        )
        RenderUtils.drawRect(
            45F,
            14F,
            45F + barLength,
            17F,
            BlendUtils.getHealthColor(target.health, target.maxHealth).rgb
        )

        for (i in 0..9) {
            RenderUtils.drawBorder(45F + i * 6F, 14F, 45F + (i + 1F) * 6F, 17F, 0.25F, Color.black.rgb)
        }

        GL11.glPushMatrix()
        GL11.glTranslatef(46F, 20F, 0F)
        GL11.glScalef(0.5f, 0.5f, 0.5f)
        Fonts.minecraftFont.drawString(
            "HP: ${target.health.toInt()} | Dist: ${
                mc.thePlayer.getDistanceToEntityBox(
                    target
                ).toInt()
            }", 0, 0, -1
        )
        GL11.glPopMatrix()

        GlStateManager.resetColor()

        GL11.glPushMatrix()
        GL11.glColor4f(1f, 1f, 1f, 1f)
        RenderHelper.enableGUIStandardItemLighting()

        val renderItem = mc.renderItem

        val x = 45
        val y = 26

        for (index in 0..4) {
            if (target.getEquipmentInSlot(index) == null) {
                continue
            }

            mc.renderItem.zLevel = -147F
            mc.renderItem.renderItemAndEffectIntoGUI(
                target.getEquipmentInSlot(index),
                x + index * 20,
                y
            )
        }

        GlStateManager.enableAlpha()
        GlStateManager.disableBlend()
        GlStateManager.enableTexture2D()

        val mainStack = target.heldItem
        if (mainStack?.item != null) {
            renderItem.renderItemIntoGUI(mainStack, x, y)
            renderItem.renderItemOverlays(mc.fontRendererObj, mainStack, x, y)
        }

        RenderHelper.disableStandardItemLighting()
        GlStateManager.enableAlpha()
        GlStateManager.disableBlend()
        GlStateManager.disableLighting()
        GlStateManager.disableCull()
        GL11.glPopMatrix()
    }

    private fun drawNis(target: EntityLivingBase) {
        // Infos
        val info =
            "Dist >> ${decimalFormat.format(mc.thePlayer.getDistanceToEntityBox(target))}  Armor >> ${target.totalArmorValue}"
        val info2 =
            "Yaw >> ${decimalFormat.format(target.rotationYaw)}  Pitch >> ${decimalFormat.format(target.rotationPitch)}"
        val name = target.name
        val head = target.skin

        val font = Fonts.fontSmall
        val fontHeight = Fonts.font40.FONT_HEIGHT
        val color = Color.WHITE.rgb
        val percent = target.health / target.maxHealth * 100F

        // Box
        RenderUtils.drawCircleRect(0F, 0F, 150F, 37F, 2.5F, ColorUtils.black(150))

        // Head
        quickDrawHead(head, 1, 1, 35, 35)

        // Name
        Fonts.font40.drawString(name, 41F, 2.5F, color)

        // Health
        RenderUtils.drawCircleRect(
            40f,
            2f + fontHeight,
            40f + (easingHP / target.maxHealth) * 100,
            6f + fontHeight,
            2.5f,
            ColorUtils.rainbow().rgb
        )

        font.drawString(
            "${decimalFormat2.format(percent)}%",
            42f + (easingHP / target.maxHealth) * 130,
            12F,
            -1,
            true
        )

        // Info
        font.drawString(info, 41F, 9F + fontHeight, color)
        font.drawString(info2, 41F, 17F + fontHeight, color)

    }

    private fun drawNovo2(target: EntityLivingBase) {
        val font = Fonts.minecraftFont
        val mainColor = Color(0, 190, 70)
        val percent = target.health / target.maxHealth * 100F
        val nameLength = (font.getStringWidth(target.name)).coerceAtLeast(
            font.getStringWidth(
                "${
                    decimalFormat2.format(percent)
                }%"
            )
        ).toFloat() + 10F
        val barWidth = (target.health / target.maxHealth).coerceIn(
            0F,
            target.maxHealth
        ) * (nameLength - 2F)

        RenderUtils.drawRect(-2F, -2F, 3F + nameLength + 36F, 2F + 36F, Color(24, 24, 24, 255).rgb)
        RenderUtils.drawRect(-1F, -1F, 2F + nameLength + 36F, 1F + 36F, Color(31, 31, 31, 255).rgb)
        font.drawStringWithShadow(target.name, 2F + 36F + 1F, 2F, -1)
        RenderUtils.drawRect(2F + 36F, 15F, 36F + nameLength, 25F, Color(24, 24, 24, 255).rgb)


        val animateThingy = (easingHP.coerceIn(
            target.health,
            target.maxHealth
        ) / target.maxHealth) * (nameLength - 2F)

        if (easingHP > target.health)
            RenderUtils.drawRect(2F + 36F, 15F, 2F + 36F + animateThingy, 25F, mainColor.darker().rgb)

        RenderUtils.drawRect(2F + 36F, 15F, 2F + 36F + barWidth, 25F, mainColor.rgb)

        font.drawStringWithShadow(
            "${decimalFormat2.format(percent)}%",
            2F + 36F + (nameLength - 2F) / 2F - font.getStringWidth("${decimalFormat2.format(percent)}%")
                .toFloat() / 2F,
            16F,
            -1
        )

        quickDrawHead(
            target.skin, 0, 0, 36, 36
        )
    }

    private fun drawNovo(target: EntityLivingBase) {
        val info = "Health: ${decimalFormat.format(target.health)}"
        val info2 = "Distance: ${decimalFormat.format(mc.thePlayer.getDistanceToEntityBox(target))}"

        val width = (38 + info2.let(Fonts.font35::getStringWidth))
            .coerceAtLeast(118)
            .toFloat()

        val font = Fonts.font35

        // Box
        RenderUtils.drawRect(0F, 0F, width, 36F, ColorUtils.black(170))

        // Health
        RenderUtils.drawRect(
            0F, 34F, (easingHP / target.maxHealth) * width,
            36F, Color(0, 190, 70).rgb
        )

        //info

        target.name.let { Fonts.font40.drawString(it, 36, 3, 0xffffff) }
        font.drawString(
            info,
            36,
            17,
            0xffffff
        )

        font.drawString(info2, 36, 26, 0xffffff)

        // Head
        quickDrawHead(target.skin, 2, 2, 30, 30)
    }

    private fun drawHanabi(target: EntityLivingBase) {
        val hurt: Boolean = target.hurtTime > 0
        val info = "XYZ: ${target.posX.toInt()} ${target.posY.toInt()} ${target.posZ.toInt()} | Hurt: $hurt"

        val width = (38 + info.let(Fonts.font35::getStringWidth))
            .coerceAtLeast(118)
            .toFloat()

        val font = Fonts.font35

        // Box
        RenderUtils.drawRect(0F, 0F, width, 36F, ColorUtils.black(150))

        // Damage
        if (easingHP > getHealth(target)) {
            RenderUtils.drawRect(
                0F, 34F, (easingHP / target.maxHealth) * width,
                36F, Color(252, 185, 65).rgb
            )
        }

        // Heal
        if (easingHP < getHealth(target)) {
            RenderUtils.drawRect(
                (easingHP / target.maxHealth) * width, 34F,
                (getHealth(target) / target.maxHealth) * width, 36F, Color(240, 20, 240).rgb
            )
        }

        // Health
        RenderUtils.drawRect(
            0F, 34F, (getHealth(target) / target.maxHealth) * width,
            36F, Color(0, 150, 255).rgb
        )

        //info

        target.name.let { Fonts.font40.drawString(it, 36, 3, 0xffffff) }
        font.drawString(
            info,
            36,
            17,
            0xffffff
        )
        font.drawString("Armor: ${target.totalArmorValue}", 36, 27, 0xffffff)
        font.drawString(
            "❤  §f" + decimalFormat.format(target.health),
            width - 5 - font.getStringWidth("❤  " + decimalFormat.format(target.health)),
            27F,
            Color.PINK.rgb
        )

        // Head
        quickDrawHead(target.skin, 2, 2, 30, 30)
    }

    private fun drawAstolfo(target: EntityLivingBase) {
        val font = fontValue.get()
        val color = ColorUtils.skyRainbow(1, 1F, 0.9F, 5.0)
        val hpPct = easingHP / target.maxHealth

        RenderUtils.drawRect(0F, 0F, 140F, 60F, Color(0, 0, 0, 110).rgb)

        // health rect
        RenderUtils.drawRect(3F, 55F, 137F, 58F, ColorUtils.reAlpha(color, 100).rgb)
        RenderUtils.drawRect(3F, 55F, 3 + (hpPct * 134F), 58F, color.rgb)
        GL11.glColor4f(1f, 1f, 1f, 1f)
        RenderUtils.drawEntityOnScreen(18, 46, 20, target)

        font.drawStringWithShadow(target.name, 37F, 6F, -1)
        GL11.glPushMatrix()
        GL11.glScalef(2F, 2F, 2F)
        font.drawString("${getHealth(target).roundToInt()} ❤", 19, 9, color.rgb)
        GL11.glPopMatrix()
    }

    private val riseParticleList = mutableListOf<RiseParticle>()

    private fun drawRise(target: EntityLivingBase) {
        val font = fontValue.get()

        RenderUtils.drawCircleRect(0f, 0f, 150f, 50f, 5f, Color(0, 0, 0, 130).rgb)

        val hurtPercent = target.hurtPercent
        val scale = if (hurtPercent == 0f) {
            1f
        } else if (hurtPercent < 0.5f) {
            1 - (0.2f * hurtPercent * 2)
        } else {
            0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
        }
        val size = 30

        GL11.glPushMatrix()
        GL11.glTranslatef(5f, 5f, 0f)
        // 受伤的缩放效果
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)
        // 受伤的红色效果
        GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, 1f)
        // 绘制头部图片
        quickDrawHead(target.skin, 0, 0, size, size)
        GL11.glPopMatrix()

        font.drawString("Name ${target.name}", 40, 11, Color.WHITE.rgb)
        font.drawString(
            "Distance ${decimalFormat.format(mc.thePlayer.getDistanceToEntityBox(target))} Hurt ${target.hurtTime}",
            40,
            11 + font.FONT_HEIGHT,
            Color.WHITE.rgb
        )

        // 渐变血量条
        GL11.glEnable(3042)
        GL11.glDisable(3553)
        GL11.glBlendFunc(770, 771)
        GL11.glEnable(2848)
        GL11.glShadeModel(7425)

        val stopPos =
            (5 + ((135 - font.getStringWidth(decimalFormat.format(target.maxHealth))) * (easingHP / target.maxHealth))).toInt()
        for (i in 5..stopPos step 5) {
            val x1 = (i + 5).coerceAtMost(stopPos).toDouble()
            RenderUtils.quickDrawGradientSideways(
                i.toDouble(), 39.0, x1, 45.0,
                ColorUtils.hslRainbow(i, indexOffset = 10).rgb, ColorUtils.hslRainbow(x1.toInt(), indexOffset = 10).rgb
            )
        }
        GL11.glEnable(3553)
        GL11.glDisable(3042)
        GL11.glDisable(2848)
        GL11.glShadeModel(7424)
        GL11.glColor4f(1f, 1f, 1f, 1f)

        font.drawString(decimalFormat.format(easingHP), stopPos + 5, 43 - font.FONT_HEIGHT / 2, Color.WHITE.rgb)

        if (target.hurtTime >= 9) {
            for (i in 0 until riseCountValue.get()) {
                riseParticleList.add(RiseParticle())
            }
        }

        val curTime = System.currentTimeMillis()
        riseParticleList.map { it }.forEach { rp ->
            if ((curTime - rp.time) > ((riseMoveTimeValue.get() + riseFadeTimeValue.get()) * 50)) {
                riseParticleList.remove(rp)
            }
            val movePercent = if ((curTime - rp.time) < riseMoveTimeValue.get() * 50) {
                (curTime - rp.time) / (riseMoveTimeValue.get() * 50f)
            } else {
                1f
            }
            val x = (movePercent * rp.x * 0.5f * riseDistanceValue.get()) + 20
            val y = (movePercent * rp.y * 0.5f * riseDistanceValue.get()) + 20
            val alpha = if ((curTime - rp.time) > riseMoveTimeValue.get() * 50) {
                1f - ((curTime - rp.time - riseMoveTimeValue.get() * 50) / (riseFadeTimeValue.get() * 50f)).coerceAtMost(
                    1f
                )
            } else {
                1f
            } * riseAlphaValue.get()
            RenderUtils.drawCircle(
                x,
                y,
                riseSizeValue.get() * 2,
                Color(rp.color.red, rp.color.green, rp.color.blue, (alpha * 255).toInt()).rgb
            )
        }
    }

    class RiseParticle {
        val color = ColorUtils.rainbow(RandomUtils.nextInt(0, 30))
        val alpha = RandomUtils.nextInt(150, 255)
        val time = System.currentTimeMillis()
        val x = RandomUtils.nextInt(-50, 50)
        val y = RandomUtils.nextInt(-50, 50)
    }

    private fun drawTenacity(target: EntityLivingBase) {
        val font = fontValue.get()

        val additionalWidth = font.getStringWidth(target.name).coerceAtLeast(75)
        RenderUtils.drawCircleRect(
            0f,
            0f,
            45f + additionalWidth,
            tenacityHeightValue.get().toFloat(),
            7f,
            ColorUtils.black(110)
        )

        // circle player avatar
        mc.textureManager.bindTexture(target.skin)
        RenderUtils.drawScaledCustomSizeModalCircle(5, 5, 8f, 8f, 8, 8, 30, 30, 64f, 64f)
        RenderUtils.drawScaledCustomSizeModalCircle(5, 5, 40f, 8f, 8, 8, 30, 30, 64f, 64f)

        // info text
        font.drawCenteredString(target.name, 40 + (additionalWidth / 2f), 5f, Color.WHITE.rgb, false)
        "${decimalFormat.format((easingHP / target.maxHealth) * 100)}%".also {
            font.drawString(
                it,
                (40f + (easingHP / target.maxHealth) * additionalWidth - font.getStringWidth(it)).coerceAtLeast(40f),
                28f - font.FONT_HEIGHT,
                Color.WHITE.rgb,
                false
            )
        }

        // hp bar
        RenderUtils.drawCircleRect(40f, 28f, 40f + additionalWidth, 33f, 2.5f, Color(0, 0, 0, 70).rgb)
        RenderUtils.drawCircleRect(
            40f,
            28f,
            40f + (easingHP / target.maxHealth) * additionalWidth,
            33f,
            2.5f,
            ColorUtils.rainbow().rgb
        )
    }

    private fun drawMoon(target: EntityLivingBase) {
        val name = target.name
        val head = target.skin

        val color = Color.WHITE.rgb
        val fontHeight = Fonts.font35.FONT_HEIGHT
        val percent = target.health / target.maxHealth * 70F

        //box
        RenderUtils.drawRect(0F, 0F, 110F, 40F, ColorUtils.black(150))
        //rect
        RenderUtils.drawRect(0F, 0F, 110F, 1.5F, ColorUtils.rainbow().rgb)
        //name
        Fonts.font35.drawString(name, 41F, 6.5F, color)
        //healthrect
        RenderUtils.drawRect(40f, 20f, 105f, 21f, ColorUtils.black(160))
        //Armor
        RenderUtils.drawRect(40f, 30f, 40f + (target.totalArmorValue / 20) * 65F, 31f, Color(0, 80, 200).rgb)
        //healthrect
        RenderUtils.drawRect(40f, 30f, 105f, 31f, ColorUtils.black(160))
        // Health
        RenderUtils.drawRect(
            40f,
            20f,
            40f + (easingHP / target.maxHealth) * 65,
            21f,
            Color.green.rgb
        )
        //head
        RenderUtils.drawHead(head, 5, 5, 30, 30)

    }

    private fun drawNewWTF(target: EntityLivingBase) {
        val width = (50 + target.name.let(Fonts.font40::getStringWidth))
            .coerceAtLeast(70)
            .toFloat()

        //BackGround
        RenderUtils.drawRect(0f, 0f, width, 34f, Color(40, 40, 40, 0).rgb)
        RenderUtils.drawCircleRect(0f, 0f, width, 34f, 2f, Color(0, 0, 0, 100).rgb)
        RenderUtils.drawRect(2f, 28f, width - 2f, 29f, ColorUtils.black(130))
        RenderUtils.drawRect(2f, 31f, width - 2f, 32f, ColorUtils.black(130))

        //Bars
        GL11.glEnable(3042)
        GL11.glDisable(3553)
        GL11.glBlendFunc(770, 771)
        GL11.glEnable(2848)
        GL11.glShadeModel(7425)
        val yPos = 5 + Fonts.font40.FONT_HEIGHT + 3f

        GL11.glEnable(3553)
        GL11.glDisable(3042)
        GL11.glShadeModel(7424)
        GL11.glColor4f(0f, 10f, 0f, 1f)
//        RenderUtils.drawRect(2F, 28F, 2 + (easingHP / target.maxHealth) * (width - 4), 29F, ColorUtils.hslRainbow(10, indexOffset = 30).rgb)
//        RenderUtils.drawRect(2F, 28F, 2 + (getHealth(target) / target.maxHealth) * (width - 4), 29F, ColorUtils.hslRainbow(10, indexOffset = 30).rgb)

        RenderUtils.drawGradientSideways(
            2.0,
            28.0,
            2.0 + (easingHP / target.maxHealth) * (width - 4),
            29.0,
            ColorUtils.hslRainbow(10, indexOffset = 30).rgb,
            ColorUtils.hslRainbow(10, indexOffset = 60).rgb
        )
        RenderUtils.drawGradientSideways(
            2.0,
            28.0,
            2.0 + (getHealth(target) / target.maxHealth) * (width - 4),
            29.0,
            ColorUtils.hslRainbow(10, indexOffset = 30).rgb,
            ColorUtils.hslRainbow(10, indexOffset = 60).rgb
        )
        RenderUtils.drawRect(2F, 31F, 2 + (target.totalArmorValue / 20F) * (width - 4), 32F, Color(0, 255, 255).rgb)

        //draw text
        Fonts.font40.drawString(target.name, 25, 3, Color.WHITE.rgb)
        GL11.glPushMatrix()
        GL11.glScaled(0.7, 0.7, 0.7)
        Fonts.font35.drawString(
            "Health: ${decimalFormat.format(getHealth(target))}",
            25 / 0.7F,
            (2 + Fonts.font40.height) / 0.7F,
            Color(255, 255, 255, 130).rgb
        )
        Fonts.font35.drawString(
            "Distance: ${decimalFormat.format(mc.thePlayer.getDistanceToEntityBox(target))}" + "m",
            25 / 0.7F,
            (8 + Fonts.font40.height) / 0.7F,
            Color(255, 255, 255, 130).rgb
        )
        GL11.glPopMatrix()

        //draw head
        RenderUtils.drawHead(target.skin, 2, 2, 20, 21)

    }

    private fun getTBorder(): Border? {
        return when (modeValue.get().lowercase()) {
            "destiny" -> Border(0F, 0F, 150F, 37F)

            "novolinewtf" -> Border(
                0F,
                0F,
                (38 + mc.thePlayer.name.let(Fonts.fontSFUI40::getStringWidth)).coerceAtLeast(118).toFloat(),
                36F
            )

            "novolineintent" -> Border(0F, 0F, Fonts.minecraftFont.getStringWidth(mc.thePlayer.name) + 36F, 36F)

            "astolfo" -> Border(0F, 0F, 140F, 60F)

            "hanabi" -> Border(
                0F,
                0F,
                (38 + mc.thePlayer.name.let(Fonts.font40::getStringWidth)).coerceAtLeast(118).toFloat(),
                36F
            )

            "exhibition" -> Border(
                0F,
                0F,
                140F.coerceAtLeast(45F + fontValue.get().getStringWidth(mc.thePlayer.name)),
                46F
            )

            "rise" -> Border(0F, 0F, 150F, 50F)

            "tenacity" -> Border(0F, 0F, 120F, 40F)

            "moon" -> Border(0F, 0F, 110F, 40F)

            "newwtf" -> Border(0F, 0F, 50F, 34F)
            else -> null
        }
    }
}