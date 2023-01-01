// Destiny made by ChengFeng
package net.ccbluex.liquidbounce.ui.client.clickgui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.azlips.AzlipsClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.chocolate.ChocolateClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.liquidbounce.ClickGui;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.liquidbounce.style.styles.*;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.novoline.DropdownGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.powerx.EsperanzaClickGui;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGUI", category = ModuleCategory.CLIENT, keyBind = Keyboard.KEY_RSHIFT, canEnable = false, description = "Opens the ClickGUI")
public class ClickGUIModule extends Module {
    public static final FloatValue scaleValue = new FloatValue("Scale", 0.5F, 0.2F, 2F);
    private static final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    public static final IntegerValue colorRedValue = (IntegerValue) new IntegerValue("R", 0, 0, 255).displayable(() -> !colorRainbow.get());
    public static final IntegerValue colorGreenValue = (IntegerValue) new IntegerValue("G", 160, 0, 255).displayable(() -> !colorRainbow.get());
    public static final IntegerValue colorBlueValue = (IntegerValue) new IntegerValue("B", 255, 0, 255).displayable(() -> !colorRainbow.get());
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);

    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    private void updateStyle() {
        switch (styleValue.get().toLowerCase()) {
            case "astolfo" :
                LiquidBounce.clickGui.style = new AstolfoStyle();
                break;
            case "liquidbounce":
                LiquidBounce.clickGui.style = new LiquidBounceStyle();
                break;
            case "null":
                LiquidBounce.clickGui.style = new NullStyle();
                break;
            case "normal":
                LiquidBounce.clickGui.style = new NormalStyle();
                break;
            case "black":
                LiquidBounce.clickGui.style = new BlackStyle();
                break;
            case "white":
                LiquidBounce.clickGui.style = new WhiteStyle();
                break;

        }
    }

    @Override
    public void onEnable() {
        updateStyle();
        switch (modeValue.get().toLowerCase()) {
            case "liquidbounce":
                mc.displayGuiScreen(LiquidBounce.clickGui);
                break;
            case "chocolate":
                mc.displayGuiScreen(new ChocolateClickGUI());
                break;
            case "azlips":
                mc.displayGuiScreen(new AzlipsClickGUI());
                break;
            case "novoline":
                mc.displayGuiScreen(new DropdownGUI());
                break;
            case "powerx":
                mc.displayGuiScreen(new EsperanzaClickGui());
                break;

        }
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();

        if (packet instanceof S2EPacketCloseWindow && mc.currentScreen instanceof ClickGui) {
            event.cancelEvent();
        }
    }

    private final ListValue styleValue = new ListValue("Style", new String[]{"Normal", "LiquidBounce", "Null", "Black","White","Astolfo"}, "Null") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };


    private final ListValue modeValue = new ListValue("Mode", new String[]{"LiquidBounce",  "Chocolate", "Azlips",  "Novoline","PowerX"}, "LiquidBounce") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            onEnable();
        }
    };


}