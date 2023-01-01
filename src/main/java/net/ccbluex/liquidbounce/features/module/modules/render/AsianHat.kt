package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.color.ColorMixer.getMixedColor
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils.fade
import net.ccbluex.liquidbounce.utils.render.ColorUtils.getRainbowOpaque
import net.ccbluex.liquidbounce.utils.render.ColorUtils.skyRainbow
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11
import java.awt.Color

@ModuleInfo(
    name = "AsianHat",
    description = "Yep. China Hat.",
    category = ModuleCategory.RENDER
)
class AsianHat : Module() {
    private val colorModeValue =
        ListValue("Color", arrayOf("Custom", "Rainbow", "Sky", "LiquidSlowly", "Fade", "Mixer"), "Custom")
    private val colorRedValue = IntegerValue("Red", 255, 0, 255)
    private val colorGreenValue = IntegerValue("Green", 255, 0, 255)
    private val colorBlueValue = IntegerValue("Blue", 255, 0, 255)
    private val colorAlphaValue = IntegerValue("Alpha", 255, 0, 255)
    private val saturationValue = FloatValue("Saturation", 1f, 0f, 1f)
    private val brightnessValue = FloatValue("Brightness", 1f, 0f, 1f)
    private val mixerSecondsValue = IntegerValue("Seconds", 2, 1, 10)
    private val spaceValue = IntegerValue("Color-Space", 0, 0, 200)
    private val noFirstPerson = BoolValue("NoFirstPerson", true)

    @EventTarget
    fun onRender3D(event: Render3DEvent?) {
        val entity: EntityLivingBase? = mc.thePlayer
        if (entity == null || noFirstPerson.get() && mc.gameSettings.thirdPersonView == 0) return
        val bb = entity.entityBoundingBox
        val radius = bb.maxX - bb.minX
        val height = bb.maxY - bb.minY
        val posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks
        val posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks
        val posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks
        val colour = getColor(entity, 0)
        val r = colour.red / 255.0f
        val g = colour.green / 255.0f
        val b = colour.blue / 255.0f
        val al = colorAlphaValue.get() / 255.0f
        var realIndex = 0
        pre3D()
        GL11.glTranslated(-mc.renderManager.viewerPosX, -mc.renderManager.viewerPosY, -mc.renderManager.viewerPosZ)
        GL11.glBegin(GL11.GL_POLYGON)
        GL11.glColor4f(r, g, b, al)
        GL11.glVertex3d(posX, posY + height + 0.3f, posZ)
        var i = 0
        while (i <= 360) {
            val posX2 = posX - Math.sin(i * Math.PI / 180) * radius
            val posZ2 = posZ + Math.cos(i * Math.PI / 180) * radius
            GL11.glVertex3d(posX2, posY + height, posZ2)
            if (spaceValue.get() > 0) {
                val colour2 = getColor(entity, realIndex * spaceValue.get())
                val r2 = colour2.red / 255.0f
                val g2 = colour2.green / 255.0f
                val b2 = colour2.blue / 255.0f
                GL11.glColor4f(r2, g2, b2, al)
            }
            realIndex++
            i += 1
        }
        GL11.glVertex3d(posX, posY + height + 0.3f, posZ)
        GL11.glEnd()
        post3D()
    }

    fun getColor(ent: Entity?, index: Int): Color {
        return when (colorModeValue.get()) {
            "Custom" -> Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get())
            "Rainbow" -> Color(
                getRainbowOpaque(
                    mixerSecondsValue.get(),
                    saturationValue.get(),
                    brightnessValue.get(),
                    index
                )
            )
            "Sky" -> skyRainbow(index, saturationValue.get(), brightnessValue.get(), 1.0)
            "LiquidSlowly" -> ColorUtils.slowlyRainbow(
                System.nanoTime(),
                index,
                saturationValue.get(),
                brightnessValue.get()
            )
            "Mixer" -> getMixedColor(index, mixerSecondsValue.get())
            else -> fade(Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get()), index, 100)
        }
    }

    companion object {
        fun pre3D() {
            GL11.glPushMatrix()
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glShadeModel(GL11.GL_SMOOTH)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            //GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_LIGHTING)
            GL11.glDepthMask(false)
            //GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        }

        fun post3D() {
            GL11.glDepthMask(true)
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            //GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glPopMatrix()
            GL11.glColor4f(1f, 1f, 1f, 1f)
        }
    }
}