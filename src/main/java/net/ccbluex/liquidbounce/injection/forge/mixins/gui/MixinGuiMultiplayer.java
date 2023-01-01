// Destiny made by ChengFeng
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.ui.client.mainmenu.GuiAntiForge;
import net.ccbluex.liquidbounce.ui.client.mainmenu.GuiServerSpoof;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends MixinGuiScreen {

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        buttonList.add(new GuiButton(997, 5, 8, 98, 20, "AntiForge"));
        buttonList.add(new GuiButton(998, 108, 8, 98, 20, "ServerSpoof"));
        buttonList.add(new GuiButton(999, width - 104, 8, 98, 20, "AltManager"));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        switch (button.id) {
            case 997:
                mc.displayGuiScreen(new GuiAntiForge((GuiScreen) (Object) this));
                break;
            case 998:
                mc.displayGuiScreen(new GuiServerSpoof((GuiScreen) (Object) this));
                break;
            case 999:
                mc.displayGuiScreen(new GuiAltManager((GuiScreen) (Object) this));
        }
    }

    @Inject(method="connectToServer", at=@At(value="HEAD"))
    public void connectToServer(CallbackInfo callbackInfo) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.getNetHandler() != null) {
            minecraft.getNetHandler().getNetworkManager().closeChannel(new ChatComponentText(""));
        }
    }
}