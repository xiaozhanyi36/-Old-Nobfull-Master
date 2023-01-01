// Destiny made by ChengFeng
package net.ccbluex.liquidbounce.features.module

import org.lwjgl.input.Keyboard

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ModuleInfo(
    val name: String,
    val category: ModuleCategory,
    val keyBind: Int = Keyboard.CHAR_NONE,
    val canEnable: Boolean = true,
    val array: Boolean = true,
    val autoDisable: EnumAutoDisableType = EnumAutoDisableType.NONE,
    val moduleCommand: Boolean = true,
    val defaultOn: Boolean = false,
    val triggerType: EnumTriggerType = EnumTriggerType.TOGGLE,
    val description: String = "Forgot add desc"
)

enum class EnumAutoDisableType {
    NONE,
    RESPAWN,
    FLAG,
    GAME_END
}

enum class EnumTriggerType {
    TOGGLE,
    PRESS
}