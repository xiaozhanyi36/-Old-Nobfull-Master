package net.ccbluex.liquidbounce.features.module.modules.color

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.render.Animation
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "Hotbar", category = ModuleCategory.COLOR, canEnable = false, description = "Hotbar settings")
object Hotbar: Module() {
    val betterHotbarValue = BoolValue("Better", true)
    val hotbarAlphaValue = IntegerValue("HotbarAlpha", 70, 0, 255).displayable { betterHotbarValue.get() }
    val infoAlphaValue = IntegerValue("InfoAlpha", 70, 0, 255).displayable { betterHotbarValue.get() }
    private val hotbarEaseValue = BoolValue("HotbarEase", true).displayable { betterHotbarValue.get() }
    private val hotbarAnimSpeedValue =
        IntegerValue("AnimSpeed", 10, 5, 20).displayable { hotbarEaseValue.get() && betterHotbarValue.get() }
    private val hotbarAnimTypeValue =
        EaseUtils.getEnumEasingList("AnimType").displayable { hotbarEaseValue.get() && betterHotbarValue.get() }
    private val hotbarAnimOrderValue = EaseUtils.getEnumEasingOrderList("AnimOrder")
        .displayable { hotbarEaseValue.get() && betterHotbarValue.get() }


    private var easeAnimation: Animation? = null
    private var easingValue = 0
        get() {
            if (easeAnimation != null) {
                field = easeAnimation!!.value.toInt()
                if (easeAnimation!!.state == Animation.EnumAnimationState.STOPPED) {
                    easeAnimation = null
                }
            }
            return field
        }
        set(value) {
            if (easeAnimation == null || (easeAnimation != null && easeAnimation!!.to != value.toDouble())) {
                easeAnimation = Animation(
                    EaseUtils.EnumEasingType.valueOf(hotbarAnimTypeValue.get()),
                    EaseUtils.EnumEasingOrder.valueOf(hotbarAnimOrderValue.get()),
                    field.toDouble(),
                    value.toDouble(),
                    hotbarAnimSpeedValue.get() * 30L
                ).start()
            }
        }



    fun getHotbarEasePos(x: Int): Int {
        if (!hotbarEaseValue.get()) return x
        easingValue = x
        return easingValue
    }

}