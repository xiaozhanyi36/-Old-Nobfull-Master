package net.ccbluex.liquidbounce.ui.client.mainmenu;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.HWID.Checker;
import net.ccbluex.liquidbounce.utils.HWID.HWIDUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class GuiLogin extends GuiScreen {
    public static EmptyInputBox username;
    public static String Name = "";
    static EmptyInputBox password;
    boolean type = false;

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public void initGui() {
        super.initGui();
        username = new EmptyInputBox(4, mc.fontRendererObj, 20, 150, 100, 20);
        password = new EmptyInputBox(4, mc.fontRendererObj, 20, 180, 100, 20);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);

        drawBackground(0);

        username.yPosition = 100;
        password.yPosition = username.yPosition + 30;
        RenderUtils.drawRect(0, 0, 140, sr.getScaledHeight(), new Color(68, 68, 68, 150).getRGB());

        Fonts.fontBaloo40.drawString("Welcome!", 40, 45, Color.WHITE.getRGB());
        Fonts.fontBaloo35.drawString("Login to " + LiquidBounce.CLIENT_NAME, 30, 65, Color.WHITE.getRGB());

        if (type) {
            Fonts.fontBaloo35.drawString("Wrong USERNAME or PASSWORD", width / 2 - Fonts.fontBaloo35.getStringWidth("Wrong USERNAME or PASSWORD") / 2, height - 10, Color.RED.getRGB());
        }


        RenderUtils.drawRoundRect(username.xPosition, username.yPosition, username.xPosition + username.getWidth(), username.yPosition + 20, username.isFocused() ? new Color(71, 71, 71).getRGB() : new Color(149, 149, 149).getRGB());
        RenderUtils.drawRoundRect(username.xPosition + 0.5f, username.yPosition + 0.5f, username.xPosition + username.getWidth() - 0.5f, username.yPosition + 20 - 0.5f, new Color(33, 33, 33, 180).getRGB());

        if (!username.isFocused() && username.getText().isEmpty()) {
            Fonts.fontBaloo35.drawString("USERNAME", username.xPosition + 4, username.yPosition + 6, new Color(180, 180, 180).getRGB());
        }

        RenderUtils.drawRoundRect(password.xPosition, password.yPosition, password.xPosition + password.getWidth(), password.yPosition + 20, password.isFocused() ? new Color(71, 71, 71).getRGB() : new Color(149, 149, 149).getRGB());
        RenderUtils.drawRoundRect(password.xPosition + 0.5f, password.yPosition + 0.5f, password.xPosition + password.getWidth() - 0.5f, password.yPosition + 20 - 0.5f, new Color(68, 68, 68, 180).getRGB());
        if (!password.isFocused() && password.getText().isEmpty()) {
            Fonts.fontBaloo35.drawString("PASSWORD", password.xPosition + 4, password.yPosition + 6, new Color(180, 180, 180).getRGB());
        } else {
            StringBuilder wdf = new StringBuilder();
            for (char ignored : password.getText().toCharArray()) {
                wdf.append("*");
            }
            Fonts.fontBaloo40.drawString(wdf.toString(), password.xPosition + 4, password.yPosition + 6, new Color(180, 180, 180).getRGB());
        }

        username.drawTextBox();
        if (isHovered(password.xPosition, password.yPosition + 30, password.xPosition + password.getWidth(), password.yPosition + 50, mouseX, mouseY)) {
            if (Mouse.isButtonDown(0)) {

                verify();
            }
            RenderUtils.drawRoundRect(password.xPosition, password.yPosition + 30, password.xPosition + password.getWidth(), password.yPosition + 50, new Color(107, 141, 205).getRGB());
            Fonts.fontBaloo35.drawCenteredString("LOGIN", password.xPosition + password.getWidth() / 2, password.yPosition + 38, new Color(255, 255, 255).getRGB());
        } else {
            RenderUtils.drawRoundRect(password.xPosition, password.yPosition + 30, password.xPosition + password.getWidth(), password.yPosition + 50, new Color(77, 111, 175).getRGB());
            Fonts.fontBaloo35.drawCenteredString("LOGIN", password.xPosition + password.getWidth() / 2, password.yPosition + 38, new Color(255, 255, 255).getRGB());
        }


        if (isHovered(password.xPosition + password.getWidth() - Fonts.fontBaloo35.getStringWidth("Copy hwid"), password.yPosition + 60, password.xPosition + password.getWidth(), password.yPosition + 70, mouseX, mouseY)) {
            if (Mouse.isButtonDown(0)) {
                try {
                    HWIDUtils.setClipboardString();
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
            Fonts.fontBaloo35.drawString("Copy hwid", password.xPosition + password.getWidth() - Fonts.fontBaloo35.getStringWidth("Copy hwid"), password.yPosition + 60, new Color(77, 111, 175).getRGB());//77,111,175
        } else {
            Fonts.fontBaloo35.drawString("Copy hwid", password.xPosition + password.getWidth() - Fonts.fontBaloo35.getStringWidth("Copy hwid"), password.yPosition + 60, new Color(150, 150, 150).getRGB());//77,111,175
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        switch (keyCode) {
            case Keyboard.KEY_TAB:
                if (username.isFocused()) {
                    // Tab键切换焦点
                    password.setFocused(true);
                    username.setFocused(false);
                    return;
                }
                break;
            case Keyboard.KEY_RETURN:
                verify();
                break;
            default:
                if (username.isFocused()) {
                    username.textboxKeyTyped(typedChar, keyCode);
                }
                if (password.isFocused()) {
                    password.textboxKeyTyped(typedChar, keyCode);
                }
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void verify() {
        if (Checker.IlllIIlllllIlIl(username.getText(), password.getText())) {
            LiquidBounce.INSTANCE.setVerified(true);
            LiquidBounce.INSTANCE.setUsingBackground(true);
            LiquidBounce.mainMenu = new GuiMainMenu();
            Minecraft.getMinecraft().displayGuiScreen(new GuiWelcomeBack());
        } else type = true;
    }
}
