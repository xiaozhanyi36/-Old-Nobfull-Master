package net.ccbluex.liquidbounce.ui.client.mainmenu;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class GuiWelcomeBack extends GuiScreen {
    private static long startTime = 0L;
    int textAlpha;
    double Anitext;
    private float currentX;
    private float currentY;

    public GuiWelcomeBack() {
        super();
        this.textAlpha = 255;
        this.Anitext = 0.0;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (startTime == 0L) {
            startTime = System.currentTimeMillis();
        }
        final int h = new ScaledResolution(this.mc).getScaledHeight();
        final int w = new ScaledResolution(this.mc).getScaledWidth();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final float xDiff = (mouseX - h / 2 - this.currentX) / sr.getScaleFactor();
        final float yDiff = (mouseY - w / 2 - this.currentY) / sr.getScaleFactor();
        this.currentX += xDiff * 0.3f;
        this.currentY += yDiff * 0.3f;
        final GameFontRenderer font = Fonts.font35;
        final GameFontRenderer font2 = Fonts.font40;
        this.drawBackground(0);
        GlStateManager.translate(this.currentX / 100.0f, this.currentY / 100.0f, 0.0f);
        GlStateManager.translate(this.currentX / 15.0f, this.currentY / 15.0f, 0.0f);
        GlStateManager.translate(-this.currentX / 100.0f, -this.currentY / 100.0f, 0.0f);
        font2.drawStringWithShadow("Welcome to NobleFull", sr.getScaledWidth() / 2f - font2.getStringWidth("Welcome back to NobleFull") / 2f, (float) (sr.getScaledHeight() / 2f - 3f - this.Anitext), new Color(255, 255, 255).getRGB());
        font.drawStringWithShadow("Welcome back, " + LiquidBounce.rankedName, sr.getScaledWidth() / 2f - font.getStringWidth("Welcome back, " + LiquidBounce.rankedName) / 2f, sr.getScaledHeight() / 2.0f + font2.FONT_HEIGHT - (float) this.Anitext, new Color(255, 255, 255).getRGB());

        if (startTime + 1500L <= System.currentTimeMillis()) {
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }
}
