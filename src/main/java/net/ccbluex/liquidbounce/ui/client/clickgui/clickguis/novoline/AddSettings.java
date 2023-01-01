package net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.novoline;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.novoline.setting.Manager;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.novoline.setting.Setting;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.novoline.setting.SettingType;
import net.ccbluex.liquidbounce.value.*;

public class AddSettings {
    public static void add(Module module, Value<?> value) {
        if (value instanceof BoolValue) {
            Manager.put(new Setting(value.getName(), value.getName(), SettingType.CHECKBOX, module, (BoolValue) value, value::getDisplayable));
        } else if (value instanceof ListValue) {
            Manager.put(new Setting(value.getName(), value.getName(), SettingType.COMBOBOX, module, (ListValue) value, value::getDisplayable));
        } else if (value instanceof NumberValue) {
            Manager.put(new Setting(value.getName(), value.getName(), SettingType.SLIDER, module, (NumberValue<?>) value, 0.1, value::getDisplayable));
        } else if (value instanceof TextValue) {
            Manager.put(new Setting(value.getName(), value.getName(), SettingType.TEXTBOX, module, "", (TextValue) value, value::getDisplayable));
        }
    }
}
