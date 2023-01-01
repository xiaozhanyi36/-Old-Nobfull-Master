package net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.chocolate;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGUIModule;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;

public class ChocolateClickGUI extends GuiScreen {
    public static ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public static ModuleCategory currentModuleType = ModuleCategory.COMBAT;
    public static Module currentModule = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(0);
    public static float startX = sr.getScaledWidth() / 2 - 450 / 2, startY = sr.getScaledHeight() / 2 - 350 / 2;
    public static int moduleStart = 0;
    public static int valueStart = 0;
    public static int alpha;
    public float moveX = 0, moveY = 0;
    boolean previousmouse = true;
    boolean mouse;
    boolean bind = false;
    float hue;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        sr = new ScaledResolution(mc);
        if (alpha < 255) {
            alpha += 5;
        }
        if (this.hue > 255.0f) {
            this.hue = 0.0f;
        }
        float h = this.hue;
        float h2 = this.hue + 85.0f;
        float h3 = this.hue + 170.0f;
        if (h2 > 255.0f) {
            h2 -= 255.0f;
        }
        if (h3 > 255.0f) {
            h3 -= 255.0f;
        }
        Color color33 = Color.getHSBColor(h / 255.0f, 0.9f, 1.0f);
        Color color332 = Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
        Color color333 = Color.getHSBColor(h3 / 255.0f, 0.9f, 1.0f);
        int color1 = color33.getRGB();
        int color2 = color332.getRGB();
        int color3 = color333.getRGB();
        int color4 = new Color(ClickGUIModule.colorRedValue.get(), ClickGUIModule.colorGreenValue.get(), ClickGUIModule.colorBlueValue.get(), alpha).getRGB();
        this.hue = (float) ((double) this.hue + 0.1);
        RenderUtils.rectangleBordered(startX, startY, startX + (float) 450, startY + (float) 450, 0.5, Colors.getColor(90, alpha), Colors.getColor(0, alpha));
        RenderUtils.rectangleBordered(startX + 1.0f, startY + 1.0f, startX + (float) 450 - 1.0f, startY + (float) 450 - 1.0f, 1.0, Colors.getColor(90, alpha), Colors.getColor(61, alpha));
        RenderUtils.rectangleBordered((double) startX + 2.5, (double) startY + 2.5, (double) (startX + (float) 450) - 2.5, (double) (startY + (float) 450) - 2.5, 0.5, Colors.getColor(61, alpha), Colors.getColor(0, alpha));
        RenderUtils.rectangleBordered(startX + 3.0f, startY + 3.0f, startX + (float) 450 - 3.0f, startY + (float) 450 - 3.0f, 0.5, Colors.getColor(27, alpha), Colors.getColor(61, alpha));
        if (alpha >= 55) {
            RenderUtils.drawGradientSideways(startX + 3.0f, startY + 3.0f, startX + (float) (450 / 2), (double) startY + 3.6, color1, color2);
            RenderUtils.drawGradientSideways(startX + (float) (450 / 2), startY + 3.0f, startX + (float) 450 - 3.0f, (double) startY + 3.6, color2, color3);
        }

        RenderUtils.drawRect(startX + 98, startY + 48, startX + 432, startY + 418, new Color(30, 30, 30, alpha).getRGB());
        RenderUtils.drawRect(startX + 100, startY + 50, startX + 430, startY + 415, new Color(35, 35, 35, alpha).getRGB());
        RenderUtils.drawRect(startX + 200, startY + 50, startX + 430, startY + 415, new Color(37, 37, 37, alpha).getRGB());
        RenderUtils.drawRect(startX + 202, startY + 50, startX + 430, startY + 415, new Color(40, 40, 40, alpha).getRGB());

        Fonts.font40.drawCenteredString(LiquidBounce.CLIENT_NAME, startX + 50, startY + 12, new Color(255, 255, 255, alpha).getRGB());
        Fonts.font35.drawCenteredString(LiquidBounce.CLIENT_VERSION, startX + 50, startY + 32, color4);
        int m = Mouse.getDWheel();
        if (this.isCategoryHovered(startX + 100, startY + 40, startX + 200, startY + 315, mouseX, mouseY)) {
            if (m < 0 && moduleStart < LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).size() - 1) {
                moduleStart++;
            }
            if (m > 0 && moduleStart > 0) {
                moduleStart--;
            }
        }
        if (this.isCategoryHovered(startX + 200, startY + 50, startX + 430, startY + 315, mouseX, mouseY)) {
            if (m < 0 && valueStart < currentModule.getValues().size() - 1) {
                valueStart++;
            }
            if (m > 0 && valueStart > 0) {
                valueStart--;
            }
        }
        float mY = startY + 12;
        for (int i = 0; i < LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).size(); i++) {
            Module module = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(i);
            if (mY > startY + 350)
                break;
            if (i < moduleStart) {
                continue;
            }

            if (!module.getState()) {
                RenderUtils.drawRect(startX + 100, mY + 45, startX + 200, mY + 70, isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? new Color(60, 60, 60, alpha).getRGB() : new Color(35, 35, 35, alpha).getRGB());
                RenderUtils.drawFilledCircle(startX + (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? 112 : 110), mY + 58, 3, new Color(70, 70, 70, alpha).getRGB(), 5);
                Fonts.font35.drawString(module.getName(), startX + (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? 122 : 120), mY + 55,
                        new Color(175, 175, 175, alpha).getRGB());
            } else {
                RenderUtils.drawRect(startX + 100, mY + 45, startX + 200, mY + 70, isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? new Color(60, 60, 60, alpha).getRGB() : new Color(35, 35, 35, alpha).getRGB());
                RenderUtils.drawFilledCircle(startX + (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? 112 : 110), mY + 58, 3, new Color(100, 255, 100, alpha).getRGB(), 5);
                Fonts.font35.drawString(module.getName(), startX + (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? 122 : 120), mY + 55,
                        new Color(255, 255, 255, alpha).getRGB());
            }

            if (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY)) {
                if (!this.previousmouse && Mouse.isButtonDown(0)) {
                    module.setState(!module.getState());
                    previousmouse = true;
                }
                if (!this.previousmouse && Mouse.isButtonDown(1)) {
                    previousmouse = true;
                }
            }

            if (!Mouse.isButtonDown(0)) {
                this.previousmouse = false;
            }
            if (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY)
                    && Mouse.isButtonDown(1)) {
                for (int j = 0; j < currentModule.getValues().size(); j++) {
                    Value value = currentModule.getValues().get(j);
                    if (value instanceof BoolValue) {
                        ((BoolValue) value).setAnim(55);
                    }
                }
                currentModule = module;
                valueStart = 0;
            }
            mY += 25;
        }
        mY = startY + 12;
        GameFontRenderer font = Fonts.font35;
        for (int i = 0; i < currentModule.getValues().size(); i++) {
            if (mY > startY + 350)
                break;
            if (i < valueStart) {
                continue;
            }
            Value value = currentModule.getValues().get(i);
            if (value instanceof TextValue) {
                TextValue textValue = (TextValue) value;
                font.drawStringWithShadow(textValue.getName() + ": " + textValue.get(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                mY += 20;
            }
            if (value instanceof BlockValue) {
                BlockValue blockValue = (BlockValue) value;
                font.drawStringWithShadow(blockValue.getName() + ": " + blockValue.get(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                mY += 20;
            }
            if (value instanceof FontValue) {
                FontValue fontValue = (FontValue) value;
                font.drawStringWithShadow(fontValue.getName() + ": " + fontValue.get(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                mY += 20;
            }
            if (value instanceof IntegerValue) {
                IntegerValue floatValue = (IntegerValue) value;
                float x = startX + 320;
                double render = 68.0F
                        * (floatValue.get() - floatValue.getMinimum())
                        / ((floatValue).getMaximum()
                        - floatValue.getMinimum());
                RenderUtils.drawRect(x + 2, mY + 52, (float) ((double) x + 75), mY + 53,
                        isButtonHovered(x, mY + 45, x + 100, mY + 57, mouseX, mouseY) && Mouse.isButtonDown(0) ? new Color(80, 80, 80, alpha).getRGB() : (new Color(30, 30, 30, alpha)).getRGB());
                RenderUtils.drawRect(x + 2, mY + 52, (float) ((double) x + render + 6.5D), mY + 53, new Color(35, 35, 255, alpha).getRGB());
                RenderUtils.drawFilledCircle((float) ((double) x + render + 2D) + 3, mY + 52.25, 1.5, new Color(35, 35, 255, alpha).getRGB(), 5);
                font.drawStringWithShadow(floatValue.getName(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                font.drawStringWithShadow(floatValue.get().toString(), startX + 320 - font.getStringWidth(value.get().toString()), mY + 50,
                        new Color(255, 255, 255, alpha).getRGB());
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                if (this.isButtonHovered(x, mY + 45, x + 100, mY + 57, mouseX, mouseY)
                        && Mouse.isButtonDown(0)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        render = (floatValue).getMinimum();
                        double max = (floatValue).getMaximum();
                        double inc = 1;
                        double valAbs = (double) mouseX - ((double) x + 1.0D);
                        double perc = valAbs / 68.0D;
                        perc = Math.min(Math.max(0.0D, perc), 1.0D);
                        double valRel = (max - render) * perc;
                        float val = (float) (render + valRel);
                        val = (float) (Math.round(val * (1.0D / inc)) / (1.0D / inc));
                        floatValue.set((int) val);
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                }
                mY += 20;
            }
            if (value instanceof FloatValue) {
                FloatValue floatValue = (FloatValue) value;
                float x = startX + 320;
                double render = 68.0F
                        * (floatValue.get() - floatValue.getMinimum())
                        / ((floatValue).getMaximum()
                        - floatValue.getMinimum());
                RenderUtils.drawRect(x + 2, mY + 52, (float) ((double) x + 75), mY + 53,
                        isButtonHovered(x, mY + 45, x + 100, mY + 57, mouseX, mouseY) && Mouse.isButtonDown(0) ? new Color(80, 80, 80, alpha).getRGB() : (new Color(30, 30, 30, alpha)).getRGB());
                RenderUtils.drawRect(x + 2, mY + 52, (float) ((double) x + render + 6.5D), mY + 53, new Color(35, 35, 255, alpha).getRGB());
                RenderUtils.drawFilledCircle((float) ((double) x + render + 2D) + 3, mY + 52.25, 1.5, new Color(35, 35, 255, alpha).getRGB(), 5);
                font.drawStringWithShadow(floatValue.getName(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                font.drawStringWithShadow(floatValue.get().toString(), startX + 320 - font.getStringWidth(value.get().toString()), mY + 50,
                        new Color(255, 255, 255, alpha).getRGB());
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                if (this.isButtonHovered(x, mY + 45, x + 100, mY + 57, mouseX, mouseY)
                        && Mouse.isButtonDown(0)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        render = (floatValue).getMinimum();
                        double max = (floatValue).getMaximum();
                        double inc = 0.5F;
                        double valAbs = (double) mouseX - ((double) x + 1.0D);
                        double perc = valAbs / 68.0D;
                        perc = Math.min(Math.max(0.0D, perc), 1.0D);
                        double valRel = (max - render) * perc;
                        float val = (float) (render + valRel);
                        val = (float) (Math.round(val * (1.0D / inc)) / (1.0D / inc));
                        floatValue.set(val);
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                }
                mY += 20;
            }
            if (value instanceof BoolValue) {
                BoolValue boolValue = (BoolValue) value;
                float x = startX + 320;
                int xx = 50;
                int x2x = 65;
                font.drawStringWithShadow(boolValue.getName(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                if (boolValue.get()) {
                    RenderUtils.drawRect(x + xx, mY + 50, x + x2x, mY + 59, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB());
                    RenderUtils.drawFilledCircle(x + xx, mY + 54.5, 4.5, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + x2x, mY + 54.5, 4.5, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + x2x, mY + 54.5, 5, new Color(35, 35, 255, alpha).getRGB(), 10);
                } else {
                    RenderUtils.drawRect(x + xx, mY + 50, x + x2x, mY + 59, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB());
                    RenderUtils.drawFilledCircle(x + xx, mY + 54.5, 4.5, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + x2x, mY + 54.5, 4.5, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + xx, mY + 54.5, 5,
                            new Color(56, 56, 56, alpha).getRGB(), 10);
                }
                if (this.isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        this.previousmouse = true;
                        this.mouse = true;
                    }

                    if (this.mouse) {
                        boolValue.set(!boolValue.get());
                        this.mouse = false;
                    }
                }
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                mY += 20;
            }
            if (value instanceof ListValue) {
                ListValue listValue = (ListValue) value;
                float x = startX + 320;
                font.drawStringWithShadow(listValue.getName(), startX + 220, mY + 52,
                        new Color(175, 175, 175, alpha).getRGB());
                RenderUtils.drawRect(x + 5, mY + 45, x + 75, mY + 65, isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB());
                RenderUtils.drawRect(x + 2, mY + 48, x + 78, mY + 62, isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB());
                RenderUtils.drawFilledCircle(x + 5, mY + 48, 3, isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
                RenderUtils.drawFilledCircle(x + 5, mY + 62, 3, isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
                RenderUtils.drawFilledCircle(x + 75, mY + 48, 3, isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
                RenderUtils.drawFilledCircle(x + 75, mY + 62, 3, isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
                font.drawStringWithShadow(listValue.get(),
                        x + 40 - font.getStringWidth(listValue.get()) / 2, mY + 53, new Color(255, 255, 255, alpha).getRGB());
                if (this.isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY)) {
                    if (Mouse.isButtonDown(0) && !this.previousmouse) {
                        if (listValue.getValues().length <= listValue.getModeListNumber(listValue.get()) + 1) {
                            listValue.set(listValue.getValues()[0]);
                        } else {
                            listValue.set(listValue.getValues()[listValue.getModeListNumber(listValue.get()) + 1]);
                        }
                        this.previousmouse = true;
                    }

                }
                mY += 25;
            }
        }
        float x = startX + 320;
        float yyy = startY + 240;
        font.drawStringWithShadow("Bind", startX + 220, yyy + 50, new Color(170, 170, 170, alpha).getRGB());
        RenderUtils.drawRect(x + 5, yyy + 45, x + 75, yyy + 65, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB());
        RenderUtils.drawRect(x + 2, yyy + 48, x + 78, yyy + 62, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB());
        RenderUtils.drawFilledCircle(x + 5, yyy + 48, 3, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
        RenderUtils.drawFilledCircle(x + 5, yyy + 62, 3, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
        RenderUtils.drawFilledCircle(x + 75, yyy + 48, 3, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
        RenderUtils.drawFilledCircle(x + 75, yyy + 62, 3, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
        font.drawStringWithShadow(Keyboard.getKeyName(currentModule.getKeyBind()),
                (x + 40 - font.getStringWidth(Keyboard.getKeyName(currentModule.getKeyBind())) / 2),
                yyy + 53, new Color(255, 255, 255, alpha).getRGB());

        if ((isHovered(startX, startY, startX + 450, startY + 50, mouseX, mouseY) || isHovered(startX, startY + 315, startX + 450, startY + 350, mouseX, mouseY) || isHovered(startX + 430, startY, startX + 450, startY + 350, mouseX, mouseY)) && Mouse.isButtonDown(0)) {
            if (moveX == 0 && moveY == 0) {
                moveX = mouseX - startX;
                moveY = mouseY - startY;
            } else {
                startX = mouseX - moveX;
                startY = mouseY - moveY;
            }
            this.previousmouse = true;
        } else if (moveX != 0 || moveY != 0) {
            moveX = 0;
            moveY = 0;
        }

        int j = 60;
        int l = 45;
        float k = startY + 10;
        float xx = startX + 5;
        for (int i = 0; i < ModuleCategory.values().length; i++) {
            ModuleCategory[] iterator = ModuleCategory.values();
            if (iterator[i] == currentModuleType) {
                RenderUtils.drawRect(xx + 8, k + 12 + j + i * l, xx + 30, k + 13 + j + i * l, color4);
            }
            float y2 = k + 20 + j + i * l;
            float y = k - 10 + j + i * l;
            Fonts.font40.drawString(iterator[i].toString(), xx + (this.isCategoryHovered(xx + 8, y, xx + 80, y2, mouseX, mouseY) ? 27 : 25), k + 56 + l * i,
                    new Color(255, 255, 255, alpha).getRGB());
            try {
                if (this.isCategoryHovered(xx + 8, y, xx + 80, y2, mouseX, mouseY)
                        && Mouse.isButtonDown(0)) {
                    currentModuleType = iterator[i];
                    currentModule = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(0);
                    moduleStart = 0;
                    valueStart = 0;
                    for (int x1 = 0; x1 < currentModule.getValues().size(); x1++) {
                        Value value = currentModule.getValues().get(x1);
                        if (value instanceof BoolValue) {
                            ((BoolValue) value).setAnim(55);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initGui() {
        for (int i = 0; i < currentModule.getValues().size(); i++) {
            Value value = currentModule.getValues().get(i);
            if (value instanceof BoolValue) {
                ((BoolValue) value).setAnim(55);
            }
        }

        super.initGui();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.bind) {
            currentModule.setKeyBind(keyCode);
            if (keyCode == 1) {
                currentModule.setKeyBind(0);
            }
            this.bind = false;
        } else if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        float mY = startY + 30;
        for (int i = 0; i < currentModule.getValues().size(); i++) {
            if (mY > startY + 350)
                break;
            if (i < valueStart) {
                continue;
            }
            Value value = currentModule.getValues().get(i);
            if (value instanceof FloatValue) {
                mY += 20;
            }
            if (value instanceof BoolValue) {

                mY += 20;
            }
            if (value instanceof ListValue) {

                mY += 25;
            }
        }
        float x1 = startX + 320;
        float yyy = startY + 240;
        if (isHovered(x1 + 2, yyy + 45, x1 + 78, yyy + 65, mouseX, mouseY)) {
            this.bind = true;
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public void onGuiClosed() {
        alpha = 0;
    }
}
