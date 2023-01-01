package net.ccbluex.liquidbounce.ui.i18n

import java.io.InputStreamReader
import java.util.*

class Language() {
    private val translateMap = HashMap<String, String>()

    init {
        val prop = Properties()

        prop.load(
            InputStreamReader(
                LanguageManager::class.java.classLoader.getResourceAsStream("assets/minecraft/destiny/locale/en_us.lang"),
                Charsets.UTF_8
            )
        )

        for ((key, value) in prop.entries) {
            if (key is String && value is String) {
                translateMap[key] = value
            }
        }
    }

    fun get(key: String): String {
        return translateMap[key] ?: key
    }
}