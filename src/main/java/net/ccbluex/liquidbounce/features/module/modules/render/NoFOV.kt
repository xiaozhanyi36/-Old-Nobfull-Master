// Destiny made by ChengFeng
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(name = "NoFOV", category = ModuleCategory.RENDER)
class NoFOV : Module() {
    val fovValue = FloatValue("FOV", 1f, 0f, 1.5f)
}
