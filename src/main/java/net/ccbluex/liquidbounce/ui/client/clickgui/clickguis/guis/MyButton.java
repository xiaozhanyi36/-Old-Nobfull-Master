package net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.guis;

import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.TimerUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class MyButton extends GuiButton {
    private final GameFontRenderer font;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean visible;
    protected boolean hovered;
    private final TimerUtils time;
    private int opacity = 150;

    public MyButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, 10, 12, buttonText);
        this.time = new TimerUtils();
        this.width = widthIn;
        this.height = heightIn;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.displayString = buttonText;
        this.font = Fonts.fontChinese;
    }

    public MyButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText, final int color, final GameFontRenderer font) {
        super(buttonId, x, y, 10, 12, buttonText);
        time = new TimerUtils();
        this.width = widthIn;
        this.height = heightIn;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.displayString = buttonText;
        this.font = font;
    }

    @Override
    protected int getHoverState(final boolean mouseOver) {
        int i = 1;
        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }
        return i;
    }

    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            if (hovered) {
                if (opacity < 250)
                    opacity += 10;
            } else if (opacity > 150)
                opacity -= 10;
            RenderUtils.drawGradientSideways(this.xPosition, this.yPosition, this.xPosition + width, this.yPosition + this.height, new Color(100, 100, 255, opacity).getRGB(), new Color(50, 50, 255, opacity).getRGB());
            this.font.drawCenteredString(this.displayString, (float) (this.xPosition + this.width / 2), this.yPosition + (this.height - this.font.FONT_HEIGHT) / 2.0f + 4.0f, -1);
        }
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
    }

    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    @Override
    public boolean isMouseOver() {
        return this.hovered;
    }

    @Override
    public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
    }

    @Override
    public void playPressSound(final SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
    }

    @Override
    public int getButtonWidth() {
        return this.width;
    }

    @Override
    public void setWidth(final int width) {
        this.width = width;
    }
}
