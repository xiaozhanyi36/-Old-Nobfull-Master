package net.ccbluex.liquidbounce.features.module.modules.color

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(name = "ArrayList", category = ModuleCategory.COLOR, canEnable = false, description = "ArrayList settings")
object ArrayList: Module() {
    val arraylistXAxisAnimTypeValue = EaseUtils.getEnumEasingList("XAnimType")
    val arraylistXAxisAnimOrderValue = EaseUtils.getEnumEasingOrderList("XAnimOrder")
    val arraylistYAxisAnimTypeValue = EaseUtils.getEnumEasingList("YAnimType")
    val arraylistYAxisAnimOrderValue = EaseUtils.getEnumEasingOrderList("YAnimOrder")
    val arraylistXAxisAnimSpeedValue = IntegerValue("XAnimSpeed", 10, 5, 20)
    val arraylistYAxisAnimSpeedValue = IntegerValue("YAnimSpeed", 10, 5, 20)
}