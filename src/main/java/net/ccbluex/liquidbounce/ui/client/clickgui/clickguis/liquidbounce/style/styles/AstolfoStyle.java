package net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.liquidbounce.style.styles;

import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.liquidbounce.Panel;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.liquidbounce.style.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.*;
import java.awt.*;
import net.ccbluex.liquidbounce.features.module.modules.render.*;
import net.ccbluex.liquidbounce.ui.client.clickgui.*;
import net.ccbluex.liquidbounce.utils.render.*;
import net.minecraft.client.renderer.*;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.liquidbounce.elements.*;
import org.lwjgl.input.*;
import net.minecraft.client.audio.*;
import net.minecraft.util.*;
import net.ccbluex.liquidbounce.utils.block.*;
import java.io.*;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.gui.*;
import net.ccbluex.liquidbounce.ui.font.*;
import java.util.*;
import java.math.*;
import java.util.List;

@SideOnly(Side.CLIENT)
public class AstolfoStyle extends Style {
    private boolean mouseDown;
    private boolean rightMouseDown;

    private Color getCategoryColor(String categoryName) {
        categoryName = categoryName.toLowerCase();
        if (categoryName.equals("combat")) {
             new Color(231, 75, 58, 175).getRGB();
        }
        if (categoryName.equals("player")) {
             new Color(142, 69, 174, 175).getRGB();
        }
        if (categoryName.equals("movement")) {
             new Color(46, 205, 111, 175).getRGB();
        }
        if (categoryName.equals("render")) {
             new Color(76, 143, 200, 175).getRGB();
        }
        if (categoryName.equals("world")) {
             new Color(233, 215, 100, 175).getRGB();
        }
        if (categoryName.equals("misc")) {
             new Color(244, 157, 19, 175).getRGB();
        }
        if (categoryName.equals("exploit")){
             new Color(241, 89, 172, 175).getRGB();
        }
        if (categoryName.equals("color")){
             new Color(255,255,166,175).getRGB();
        }
        if (categoryName.equals("client")){
             new Color(255,158,120,175).getRGB();
        }
        return ClickGUIModule.generateColor();
    }

    public void drawPanel(final int mouseX, final int mouseY, final Panel panel) {
        RenderUtils.drawRect(panel.getX() - 3.0f, panel.getY() - 1.0f, panel.getX() + (float) panel.getWidth() + 3.0f, (float) (panel.getY() + 22 + panel.getFade()), this.getCategoryColor(panel.getName()).getRGB());
        RenderUtils.drawRect((float) (panel.getX() - 2), (float) panel.getY(), (float) (panel.getX() + panel.getWidth() + 2), (float) (panel.getY() + 21 + panel.getFade()), new Color(17, 17, 17).getRGB());
        RenderUtils.drawRect(panel.getX() + 1.0f, panel.getY() + 19.0f, panel.getX() + (float) panel.getWidth() - 1.0f, (float) (panel.getY() + 18 + panel.getFade()), new Color(26, 26, 26).getRGB());
        GlStateManager.resetColor();
        Fonts.minecraftFont.drawString("§l" + panel.getName().toLowerCase(), panel.getX() + 2, panel.getY() + 6, Integer.MAX_VALUE);
    }

    public void drawDescription(final int mouseX, final int mouseY, final String text) {
        final int textWidth = Fonts.minecraftFont.getStringWidth(text);
        RenderUtils.drawRect((float) (mouseX + 9), (float) mouseY, (float) (mouseX + textWidth + 14), (float) (mouseY + Fonts.minecraftFont.FONT_HEIGHT + 3), new Color(26, 26, 26).getRGB());
        GlStateManager.resetColor();
        Fonts.minecraftFont.drawString(text.toLowerCase(), mouseX + 12, mouseY + Fonts.minecraftFont.FONT_HEIGHT / 2 - 2, Integer.MAX_VALUE);
    }

    public void drawButtonElement(final int mouseX, final int mouseY, final ButtonElement buttonElement) {
        Gui.drawRect(buttonElement.getX() - 1, buttonElement.getY() + 1, buttonElement.getX() + buttonElement.getWidth() + 1, buttonElement.getY() + buttonElement.getHeight() + 2, this.hoverColor((buttonElement.getColor() != Integer.MAX_VALUE) ? ClickGUIModule.generateColor() : new Color(26, 26, 26), buttonElement.hoverTime).getRGB());
        GlStateManager.resetColor();
        Fonts.minecraftFont.drawString(buttonElement.getDisplayName().toLowerCase(), buttonElement.getX() + 3, buttonElement.getY() + 6, Color.WHITE.getRGB());
    }

    public void drawModuleElement(final int mouseX, final int mouseY, final ModuleElement moduleElement) {
        Minecraft.getMinecraft().getLimitFramerate();
        Gui.drawRect(moduleElement.getX() + 1, moduleElement.getY() + 1, moduleElement.getX() + moduleElement.getWidth() - 1, moduleElement.getY() + moduleElement.getHeight() + 2, this.hoverColor(new Color(26, 26, 26), moduleElement.hoverTime).getRGB());
        Gui.drawRect(moduleElement.getX() + 1, moduleElement.getY() + 1, moduleElement.getX() + moduleElement.getWidth() - 1, moduleElement.getY() + moduleElement.getHeight() + 2, this.hoverColor(new Color(this.getCategoryColor(moduleElement.getModule().getCategory().getDisplayName()).getRed(), this.getCategoryColor(moduleElement.getModule().getCategory().getDisplayName()).getGreen(), this.getCategoryColor(moduleElement.getModule().getCategory().getDisplayName()).getBlue(), moduleElement.slowlyFade), moduleElement.hoverTime).getRGB());
        final int guiColor = ClickGUIModule.generateColor().getRGB();
        GlStateManager.resetColor();
        Fonts.minecraftFont.drawString(moduleElement.getDisplayName().toLowerCase(), moduleElement.getX() + 3, moduleElement.getY() + 7, Integer.MAX_VALUE);
        final List<Value<?>> moduleValues = (List<Value<?>>) moduleElement.getModule().getValues();
        if (!moduleValues.isEmpty()) {
            Fonts.minecraftFont.drawString("+", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + moduleElement.getHeight() / 2, new Color(255, 255, 255, 200).getRGB());
            if (moduleElement.isShowSettings()) {
                int yPos = moduleElement.getY() + 4;
                for (final Value value : moduleValues) {
                    if (value instanceof BoolValue) {
                        final String text = value.getName();
                        final float textWidth = (float) Fonts.minecraftFont.getStringWidth(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        RenderUtils.drawRect((float) (moduleElement.getX() + moduleElement.getWidth() + 4), (float) (yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float) (yPos + 14), new Color(26, 26, 26).getRGB());
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                            final BoolValue boolValue = (BoolValue) value;
                            boolValue.set((Boolean) !(boolean) boolValue.get());
                            AstolfoStyle.mc.getSoundHandler().playSound((ISound) PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
                        }
                        GlStateManager.resetColor();
                        Fonts.minecraftFont.drawString(text.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, ((boolean) ((BoolValue) value).get()) ? guiColor : Integer.MAX_VALUE);
                        yPos += 12;
                    } else if (value instanceof ListValue) {
                        final ListValue listValue = (ListValue) value;
                        final String text2 = value.getName();
                        final float textWidth2 = (float) Fonts.minecraftFont.getStringWidth(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 16.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 16.0f);
                        }
                        RenderUtils.drawRect((float) (moduleElement.getX() + moduleElement.getWidth() + 4), (float) (yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float) (yPos + 14), new Color(26, 26, 26).getRGB());
                        GlStateManager.resetColor();
                        Fonts.minecraftFont.drawString("§c" + text2.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 16777215);
                        Fonts.minecraftFont.drawString(listValue.openList ? "-" : "+", (int) (moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - (listValue.openList ? 5 : 6)), yPos + 4, 16777215);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                            listValue.openList = !listValue.openList;
                            AstolfoStyle.mc.getSoundHandler().playSound((ISound) PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
                        }
                        yPos += 12;
                        for (final String valueOfList : listValue.getValues()) {
                            final float textWidth3 = (float) Fonts.minecraftFont.getStringWidth(">" + valueOfList);
                            if (moduleElement.getSettingsWidth() < textWidth3 + 12.0f) {
                                moduleElement.setSettingsWidth(textWidth3 + 12.0f);
                            }
                            if (listValue.openList) {
                                RenderUtils.drawRect((float) (moduleElement.getX() + moduleElement.getWidth() + 4), (float) (yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float) (yPos + 14), new Color(26, 26, 26).getRGB());
                                if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                                    listValue.set((String) valueOfList);
                                    AstolfoStyle.mc.getSoundHandler().playSound((ISound) PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
                                }
                                GlStateManager.resetColor();
                                Fonts.minecraftFont.drawString(">", moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Integer.MAX_VALUE);
                                Fonts.minecraftFont.drawString(valueOfList.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 14, yPos + 4, (listValue.get() != null && ((String) listValue.get()).equalsIgnoreCase(valueOfList)) ? guiColor : Integer.MAX_VALUE);
                                yPos += 12;
                            }
                        }
                    } else if (value instanceof FloatValue) {
                        final FloatValue floatValue = (FloatValue) value;
                        final String text2 = value.getName() + "§f: §c" + this.round((float) floatValue.get());
                        final float textWidth2 = (float) Fonts.minecraftFont.getStringWidth(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 8.0f);
                        }
                        RenderUtils.drawRect((float) (moduleElement.getX() + moduleElement.getWidth() + 4), (float) (yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float) (yPos + 24), new Color(26, 26, 26).getRGB());
                        RenderUtils.drawRect((float) (moduleElement.getX() + moduleElement.getWidth() + 8), (float) (yPos + 18), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4.0f, (float) (yPos + 19), Integer.MAX_VALUE);
                        final float sliderValue = moduleElement.getX() + moduleElement.getWidth() + (moduleElement.getSettingsWidth() - 12.0f) * ((float) floatValue.get() - floatValue.getMinimum()) / (floatValue.getMaximum() - floatValue.getMinimum());
                        RenderUtils.drawRect(8.0f + sliderValue, (float) (yPos + 15), sliderValue + 11.0f, (float) (yPos + 21), guiColor);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4.0f && mouseY >= yPos + 15 && mouseY <= yPos + 21 && Mouse.isButtonDown(0)) {
                            final double i = MathHelper.clamp_double((double) ((mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f)), 0.0, 1.0);
                            floatValue.set((Float) this.round((float) (floatValue.getMinimum() + (floatValue.getMaximum() - floatValue.getMinimum()) * i)).floatValue());
                        }
                        GlStateManager.resetColor();
                        Fonts.minecraftFont.drawString(text2.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 16777215);
                        yPos += 22;
                    } else if (value instanceof IntegerValue) {
                        final IntegerValue integerValue = (IntegerValue) value;
                       // Integer[] integers = new Integer[10000080] ;for (int i = 0; i < 1000000;++i) {
                          //  integers[i] = (int)(Math.random() * 100000000);
                       // }
                        final String text2 = value.getName() + "§f: §c" + ((value instanceof BlockValue) ? (BlockUtils.getBlockName((int) integerValue.get()) + " (" + integerValue.get() + ")") : ((Serializable) integerValue.get()));
                        final float textWidth2 = (float) Fonts.minecraftFont.getStringWidth(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 8.0f);
                        }
                        RenderUtils.drawRect((float) (moduleElement.getX() + moduleElement.getWidth() + 4), (float) (yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float) (yPos + 24), new Color(26, 26, 26).getRGB());
                        RenderUtils.drawRect((float) (moduleElement.getX() + moduleElement.getWidth() + 8), (float) (yPos + 18), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4.0f, (float) (yPos + 19), Integer.MAX_VALUE);
                        final float sliderValue = moduleElement.getX() + moduleElement.getWidth() + (moduleElement.getSettingsWidth() - 12.0f) * ((int) integerValue.get() - integerValue.getMinimum()) / (integerValue.getMaximum() - integerValue.getMinimum());
                        RenderUtils.drawRect(8.0f + sliderValue, (float) (yPos + 15), sliderValue + 11.0f, (float) (yPos + 21), guiColor);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 15 && mouseY <= yPos + 21 && Mouse.isButtonDown(0)) {
                            final double i = MathHelper.clamp_double((double) ((mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f)), 0.0, 1.0);
                            integerValue.set((int) (integerValue.getMinimum() + (integerValue.getMaximum() - integerValue.getMinimum()) * i));
                        }
                        GlStateManager.resetColor();
                        Fonts.minecraftFont.drawString(text2.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 16777215);
                        yPos += 22;
                    } else if (value instanceof FontValue) {
                        final FontValue fontValue = (FontValue) value;
                        final FontRenderer fontRenderer = (FontRenderer) fontValue.get();
                        RenderUtils.drawRect((float) (moduleElement.getX() + moduleElement.getWidth() + 4), (float) (yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float) (yPos + 14), new Color(26, 26, 26).getRGB());
                        String displayString = "Font: Unknown";
                        if (fontRenderer instanceof GameFontRenderer) {
                            final GameFontRenderer liquidFontRenderer = (GameFontRenderer) fontRenderer;
                            displayString = "Font: " + liquidFontRenderer.getDefaultFont().getFont().getName() + " - " + liquidFontRenderer.getDefaultFont().getFont().getSize();
                        } else if (fontRenderer == Fonts.minecraftFont) {
                            displayString = "Font: SFUI35";
                        } else {
                            final Object[] objects = Fonts.getFontDetails(fontRenderer);
                            if (objects != null) {
                                displayString = objects[0] + (((int) objects[1] != -1) ? (" - " + objects[1]) : "");
                            }
                        }
                        Fonts.minecraftFont.drawString(displayString.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Color.WHITE.getRGB());
                        final int stringWidth = Fonts.minecraftFont.getStringWidth(displayString);
                        if (moduleElement.getSettingsWidth() < stringWidth + 8) {
                            moduleElement.setSettingsWidth((float) (stringWidth + 8));
                        }
                        if (((Mouse.isButtonDown(0) && !this.mouseDown) || (Mouse.isButtonDown(1) && !this.rightMouseDown)) && mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 4 && mouseY <= yPos + 12) {
                            final List<FontRenderer> fonts = (List<FontRenderer>) Fonts.getFonts();
                            if (Mouse.isButtonDown(0)) {
                                for (int j = 0; j < fonts.size(); ++j) {
                                    final FontRenderer font = fonts.get(j);
                                    if (font == fontRenderer) {
                                        if (++j >= fonts.size()) {
                                            j = 0;
                                        }
                                        fontValue.set((FontRenderer) fonts.get(j));
                                        break;
                                    }
                                }
                            } else {
                                for (int j = fonts.size() - 1; j >= 0; --j) {
                                    final FontRenderer font = fonts.get(j);
                                    if (font == fontRenderer) {
                                        if (--j >= fonts.size()) {
                                            j = 0;
                                        }
                                        if (j < 0) {
                                            j = fonts.size() - 1;
                                        }
                                        fontValue.set((FontRenderer) fonts.get(j));
                                        break;
                                    }
                                }
                            }
                        }
                        yPos += 11;
                    } else {
                        final String text = value.getName() + "§f: §c" + value.get();
                        final float textWidth = (float) Fonts.minecraftFont.getStringWidth(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        RenderUtils.drawRect((float) (moduleElement.getX() + moduleElement.getWidth() + 4), (float) (yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float) (yPos + 14), new Color(26, 26, 26).getRGB());
                        GlStateManager.resetColor();
                        Fonts.minecraftFont.drawString(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 16777215);
                        yPos += 12;
                    }
                }
                moduleElement.updatePressed();
                this.mouseDown = Mouse.isButtonDown(0);
                this.rightMouseDown = Mouse.isButtonDown(1);
                if (moduleElement.getSettingsWidth() > 0.0f && yPos > moduleElement.getY() + 4) {
                    RenderUtils.drawBorderedRect((float) (moduleElement.getX() + moduleElement.getWidth() + 4), (float) (moduleElement.getY() + 6), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float) (yPos + 2), 1.0f, new Color(26, 26, 26).getRGB(), 0);
                }
            }
        }
    }
    private BigDecimal round(final float f) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(2, 4);
        return bd;
    }

    private Color hoverColor(final Color color, final int hover) {
        final int r = color.getRed() - hover * 2;
        final int g = color.getGreen() - hover * 2;
        final int b = color.getBlue() - hover * 2;
        return new Color(Math.max(r, 0), Math.max(g, 0), Math.max(b, 0), color.getAlpha());
    }
}
