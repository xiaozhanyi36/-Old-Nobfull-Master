package net.ccbluex.liquidbounce.ui.client.mainmenu;


import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.particle.ParticleManager;
import net.ccbluex.liquidbounce.utils.render.particle.ParticleSnow;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Random;

public class GuiGoodBye extends GuiScreen {
    private static long startTime = 0L;
    private final ParticleManager particleManager = new ParticleManager(new ParticleSnow(), 20);
    int alpha = 0;
    String[] bye2 = {
            "See you next time",
            "Good Bye",
            "Do you forgot sth",
            "下次再见"
    };
    Random r = new Random();
    String bye2r = bye2[r.nextInt(bye2.length)];
    double Anitext = 0;
    private float currentX, currentY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        int h = new ScaledResolution(mc).getScaledHeight();
        int w = new ScaledResolution(mc).getScaledWidth();
        float xDiff = ((mouseX - h / 2f) - currentX) / sr.getScaleFactor();
        float yDiff = ((mouseY - w / 2f) - currentY) / sr.getScaleFactor();
        currentX += xDiff * 0.3F;
        currentY += yDiff * 0.3F;
        if (startTime == 0L) startTime = System.currentTimeMillis();


        GlStateManager.translate(currentX / 100, currentY / 100, 0);

        RenderUtils.drawImage(new ResourceLocation("destiny/gui/background/bg.png"), -1, -1, sr.getScaledWidth() + 1, sr.getScaledHeight() + 1);

        GlStateManager.translate(currentX / 15, currentY / 15, 0);

        particleManager.draw(0);
        GameFontRenderer fontwel = Fonts.font35;


        GlStateManager.translate(-currentX / 100, -currentY / 100, 0);

        fontwel.drawCenteredString(bye2r, (float) sr.getScaledWidth() / 2, (float) sr.getScaledHeight() / 2 - 3f - (float) Anitext, new Color(255, 255, 255).getRGB(), true);
        GlStateManager.translate(0, 0, 0);
        Gui.drawRect(-100, -100, sr.getScaledWidth() + 100, sr.getScaledHeight() + 100, new Color(0, 0, 0, alpha).getRGB());
        if (startTime + 500 <= System.currentTimeMillis()) {
            if (alpha != 255) {
                alpha += 3;
            }
        }
        if (alpha >= 255 && startTime + 1500 <= System.currentTimeMillis()) {
            mc.shutdown();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
