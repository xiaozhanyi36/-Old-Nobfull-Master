/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  org.lwjgl.opengl.ARBShaderObjects
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public enum RenderUtil {
    INSTANCE;
    public static Minecraft mc;
    public static float delta;
    private static Frustum frustrum;

    static {
        mc = Minecraft.getMinecraft();
    }

    public static void drawRectWH(float x, float y, float w, float h, int color) {
        RenderUtil.drawRect(x, y, w + x, h + y, color);
    }

    public static void setupRender(final boolean start) {
        if (start) {
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(!start);
    }

    public static void startDrawing() {
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
    }

    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static Vec3 interpolateRender(EntityPlayer player) {
        float part = Minecraft.getMinecraft().timer.renderPartialTicks;
        double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) part;
        double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) part;
        double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) part;
        return new Vec3(interpX, interpY, interpZ);
    }

    public static void drawHead(ResourceLocation skin, int x, int y, int width, int height) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(skin);
        RenderUtils.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height,
                64F, 64F);
    }

    public static Color fastrainbow() {
        int rainbowTick = 0;
        return new Color(Color.HSBtoRGB((float) ((double) mc.thePlayer.ticksExisted / 50 + Math.sin((double) rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
    }

    public static Color fastRainbow2() {
        int rainbowTick = 0;
        return new Color(Color.HSBtoRGB((float) ((double) System.currentTimeMillis() / 15000.0 + Math.sin((double) rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
    }

    public static void drawImage(ResourceLocation image, double x, double y, double width, double height) {
        new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        RenderUtil.drawModalRectWithCustomSizedTexture((float) x, (float) y, 0.0f, 0.0f, (float) width,
                (float) height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawIcon(float x, float y, int sizex, int sizey, ResourceLocation resourceLocation) {
        GL11.glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glTranslatef(x, y, 0.0f);
        RenderUtil.drawScaledRect(0, 0, 0.0f, 0.0f, sizex, sizey, sizex, sizey, sizex, sizey);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableRescaleNormal();
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float) c.getRed();
        float g = 0.003921569f * (float) c.getGreen();
        float b = 0.003921569f * (float) c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static void drawGradientRect2(double left, double top, double right, double bottom, int startColor, int endColor) {
//        float f = (float)(startColor >> 24 & 255) / 255.0F;
//        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
//        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
//        float f3 = (float)(startColor & 255) / 255.0F;
//        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
//        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
//        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
//        float f7 = (float)(endColor & 255) / 255.0F;

        float sa = (float) (startColor >> 24 & 0xFF) / 255.0f;
        float sr = (float) (startColor >> 16 & 0xFF) / 255.0f;
        float sg = (float) (startColor >> 8 & 0xFF) / 255.0f;
        float sb = (float) (startColor & 0xFF) / 255.0f;
        float ea = (float) (endColor >> 24 & 0xFF) / 255.0f;
        float er = (float) (endColor >> 16 & 0xFF) / 255.0f;
        float eg = (float) (endColor >> 8 & 0xFF) / 255.0f;
        float eb = (float) (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(left, bottom, 0.0).color(sr, sg, sb, sa).endVertex();
        worldrenderer.pos(right, bottom, 0.0).color(er, eg, eb, ea).endVertex();
        worldrenderer.pos(right, top, 0.0).color(er, eg, eb, ea).endVertex();
        worldrenderer.pos(left, top, 0.0).color(sr, sg, sb, sa).endVertex();
//        GlStateManager.color(f, f1, f2, f3);
//        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
//        worldrenderer.pos(left, bottom, 0.0).endVertex();
//        worldrenderer.pos(right, bottom, 0.0).endVertex();
//        worldrenderer.pos(right, top, 0.0).endVertex();
//        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawDimRect(final double x, final double y, final double x2, final double y2, final int col1) {
        RenderUtil.drawRect(x, y, x2, y2, col1);
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, 0.2f);
        GL11.glLineWidth(2);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static boolean isInViewFrustrum(final Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static boolean isInViewFrustrum(final AxisAlignedBB bb) {
        final Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static void drawColorRect(double left, double top, double right, double bottom, Color color1, Color color2, Color color3, Color color4) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(color2); //left-down
        GL11.glVertex2d(left, bottom);
        glColor(color3); //right-down
        GL11.glVertex2d(right, bottom);
        glColor(color4); //right-up
        GL11.glVertex2d(right, top);
        glColor(color1); //left-up
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        Gui.drawRect(0, 0, 0, 0, 0);
    }

    public static boolean isHovering(int n, int n2, float n3, float n4, float n5, float n6) {
        boolean b = (float) n > n3 && (float) n < n5 && (float) n2 > n4 && (float) n2 < n6;
        return b;
    }

    public static void drawTexture(int x, int y, int width, int height, int angle, ResourceLocation texture) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GL11.glColor4f(255f, 255f, 255f, 255f);
        if (angle <= 0) {
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        } else {
            GlStateManager.pushMatrix();
            int x1 = x + 5;
            int y1 = 240 / 2 - 130;
            GlStateManager.translate(x1, y1 + y + 15f, 0f);
            GlStateManager.rotate(angle, 0f, 0f, 1f);
            GlStateManager.translate(-x1, -(y1 + y + 15f), 0f);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 10, 10, 10, 10);
            GlStateManager.popMatrix();
        }
    }

    public static void drawYBrect2(float x, float y, float width, float height, float r, int color) {
        //left
        drawSector(x + r + 0.5F, y + r + 0.5F, r, 90, 180, 1, color, true, false);
        drawRect(0, 0, 0, 0, 0);
        //right
        drawSector(x + width - r - 0.5F, y + r + 0.5F, r, 90, 90, 1, color, true, false);
        drawRect(0, 0, 0, 0, 0);
        //left - down
        drawSector(x + r + 0.5F, y + height - r - 0.5F, r, 90, 270, 1, color, true, false);
        drawRect(0, 0, 0, 0, 0);
        //left - right
        drawSector(x + width - r - 0.5F, y + height - r - 0.5F, r, 90, 360, 1, color, true, false);
        drawRect(0, 0, 0, 0, 0);

        //Middle
        drawRect(x + r, y, x + r + width - 2 * r, y + height, color);
        //Left
        drawRect(x, y + r, x + r, y + height - r, color);
        //Right
        drawRect(x + width - r, y + r, x + width, y + height - r, color);
    }

    public static void drawSector(float x, float y, double r, int angle, int startAngle, float lineWidth, int c, boolean isFull, boolean isClose) {
        if (angle <= 0) {
            return;
        }
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        r *= 2.0;
        x *= 2;
        y *= 2;
        final float f2 = (c >> 24 & 0xFF) / 255.0f;
        final float f3 = (c >> 16 & 0xFF) / 255.0f;
        final float f4 = (c >> 8 & 0xFF) / 255.0f;
        final float f5 = (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GlStateManager.enableAlpha();
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(3);
        for (int i = startAngle; i <= angle + startAngle; i++) {
            double x2 = Math.sin(i * 3.141592653589793 / 180.0) * r;
            double y2 = Math.cos(i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
            if (isFull)
                GL11.glVertex2d(x, y);
        }
        if (isClose && !isFull && angle < 360) {
            double startX = Math.sin(startAngle * 3.141592653589793 / 180.0) * r;
            double startY = Math.cos(startAngle * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x, y);
            GL11.glVertex2d(x + startX, y + startY);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }

    public static int width() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
    }

    public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width,
                                                           float height, float textureWidth, float textureHeight) {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D)
                .tex(u * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D)
                .tex((u + width) * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0D)
                .tex((u + width) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    public static int height() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    }

    public static void enableGL3D(float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }

    public static void drawArc(float n, float n2, double n3, int n4, int n5, double n6, int n7) {
        n3 *= 2.0;
        n *= 2.0f;
        n2 *= 2.0f;
        float n8 = (float) (n4 >> 24 & 0xFF) / 255.0f;
        float n9 = (float) (n4 >> 16 & 0xFF) / 255.0f;
        float n10 = (float) (n4 >> 8 & 0xFF) / 255.0f;
        float n11 = (float) (n4 & 0xFF) / 255.0f;
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glLineWidth((float) n7);
        GL11.glEnable(2848);
        GL11.glColor4f(n9, n10, n11, n8);
        GL11.glBegin(3);
        int n12 = n5;
        while ((double) n12 <= n6) {
            GL11.glVertex2d((double) n + Math.sin((double) n12 * Math.PI / 180.0) * n3, (double) n2 + Math.cos((double) n12 * Math.PI / 180.0) * n3);
            ++n12;
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static int rainbow(int delay) {
        double rainbow = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 10.0);
        return Color.getHSBColor((float) ((rainbow %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static int rainbow(int delay, float slowspeed) {
        double rainbow = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / (double) slowspeed);
        return Color.getHSBColor((float) ((rainbow %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
        R2DUtils.enableGL2D();
        RenderUtil.glColor(borderColor);
        R2DUtils.drawRect(x + width, y, x1 - width, y + width);
        R2DUtils.drawRect(x, y, x + width, y1);
        R2DUtils.drawRect(x1 - width, y, x1, y1);
        R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
        R2DUtils.disableGL2D();
    }

    public static void drawCircle(double x, double y, double radius, int c) {
        float alpha = (float) (c >> 24 & 0xFF) / 255.0f;
        float red = (float) (c >> 16 & 0xFF) / 255.0f;
        float green = (float) (c >> 8 & 0xFF) / 255.0f;
        float blue = (float) (c & 0xFF) / 255.0f;
        boolean blend = GL11.glIsEnabled(3042);
        boolean line = GL11.glIsEnabled(2848);
        boolean texture = GL11.glIsEnabled(3553);
        if (!blend) {
            GL11.glEnable(3042);
        }
        if (!line) {
            GL11.glEnable(2848);
        }
        if (texture) {
            GL11.glDisable(3553);
        }
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin((double) i * 3.141526 / 180.0) * radius, y + Math.cos((double) i * 3.141526 / 180.0) * radius);
        }
        GL11.glEnd();
        if (texture) {
            GL11.glEnable(3553);
        }
        if (!line) {
            GL11.glDisable(2848);
        }
        if (!blend) {
            GL11.glDisable(3042);
        }
    }

    public static void drawCircle2(double x, double y, double radius, int c) {
        float f2 = (float) (c >> 24 & 0xFF) / 255.0f;
        float f22 = (float) (c >> 16 & 0xFF) / 255.0f;
        float f3 = (float) (c >> 8 & 0xFF) / 255.0f;
        float f4 = (float) (c & 0xFF) / 255.0f;
        GlStateManager.alphaFunc(516, 0.001f);
        GlStateManager.color(f22, f3, f4, f2);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tes = Tessellator.getInstance();
        for (double i = 0.0; i < 360.0; i += 1.0) {
            double f5 = Math.sin(i * Math.PI / 180.0) * radius;
            double f6 = Math.cos(i * Math.PI / 180.0) * radius;
            GL11.glVertex2d((double) f3 + x, (double) f4 + y);
        }
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.alphaFunc(516, 0.1f);
    }

    public static void drawFilledCircle(double x, double y, double r, int c, int id) {
        float f = (float) (c >> 24 & 0xFF) / 255.0f;
        float f1 = (float) (c >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (c >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(9);
        if (id == 1) {
            GL11.glVertex2d(x, y);
            for (int i = 0; i <= 90; ++i) {
                double x2 = Math.sin((double) i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double) i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        } else if (id == 2) {
            GL11.glVertex2d(x, y);
            for (int i = 90; i <= 180; ++i) {
                double x2 = Math.sin((double) i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double) i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        } else if (id == 3) {
            GL11.glVertex2d(x, y);
            for (int i = 270; i <= 360; ++i) {
                double x2 = Math.sin((double) i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double) i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        } else if (id == 4) {
            GL11.glVertex2d(x, y);
            for (int i = 180; i <= 270; ++i) {
                double x2 = Math.sin((double) i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double) i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        } else {
            for (int i = 0; i <= 360; ++i) {
                double x2 = Math.sin((double) i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double) i * 3.141526 / 180.0) * r;
                GL11.glVertex2f((float) (x - x2), (float) (y - y2));
            }
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void drawFullCircle(int cx, int cy, double r, int segments, float lineWidth, int part, int c) {
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        float f2 = (float) (c >> 24 & 0xFF) / 255.0f;
        float f22 = (float) (c >> 16 & 0xFF) / 255.0f;
        float f3 = (float) (c >> 8 & 0xFF) / 255.0f;
        float f4 = (float) (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glBegin(3);
        for (int i = segments - part; i <= segments; ++i) {
            double x = Math.sin((double) i * Math.PI / 180.0) * r;
            double y = Math.cos((double) i * Math.PI / 180.0) * r;
            GL11.glVertex2d((double) cx + x, (double) cy + y);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }


    public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 0xFF) / 255.0f;
        float red = (float) (hex >> 16 & 0xFF) / 255.0f;
        float green = (float) (hex >> 8 & 0xFF) / 255.0f;
        float blue = (float) (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static Color rainbowEffect(int delay) {
        float hue = (float) (System.nanoTime() + (long) delay) / 2.0E10f % 1.0f;
        Color color = new Color((int) Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16));
        return new Color((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
    }

    public static void drawFullscreenImage(ResourceLocation image) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3008);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawPlayerHead(String playerName, int x, int y, int width, int height2) {
        Minecraft.getMinecraft();
        for (Entity player : mc.theWorld.getLoadedEntityList()) {
            EntityPlayer ply;
            if (!(player instanceof EntityPlayer) || !playerName.equalsIgnoreCase((ply = (EntityPlayer) player).getName()))
                continue;
            GameProfile profile = new GameProfile(ply.getUniqueID(), ply.getName());
            NetworkPlayerInfo networkplayerinfo1 = new NetworkPlayerInfo(profile);
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glDepthMask(false);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height2, width, height2);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
        }
    }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float) (0.01 * speed);
        animation = animation < finalState ? (animation + (double) add < finalState ? (animation += add) : finalState) : (animation - (double) add > finalState ? (animation -= add) : finalState);
        return animation;
    }

    public static String getShaderCode(InputStreamReader file) {
        String shaderSource = "";
        try {
            String line;
            BufferedReader reader = new BufferedReader(file);
            while ((line = reader.readLine()) != null) {
                shaderSource = shaderSource + line + "\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return shaderSource;
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height2) {
        RenderUtil.drawImage(image, x, y, width, height2, 1.0f);
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height2, float alpha) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height2, width, height2);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawOutlinedRect(int x, int y, int width, int height2, int lineSize, Color lineColor, Color backgroundColor) {
        RenderUtil.drawRect(x, y, width, height2, backgroundColor.getRGB());
        RenderUtil.drawRect(x, y, width, y + lineSize, lineColor.getRGB());
        RenderUtil.drawRect(x, height2 - lineSize, width, height2, lineColor.getRGB());
        RenderUtil.drawRect(x, y + lineSize, x + lineSize, height2 - lineSize, lineColor.getRGB());
        RenderUtil.drawRect(width - lineSize, y + lineSize, width, height2 - lineSize, lineColor.getRGB());
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height2, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getRed() / 255.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height2, width, height2);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void doGlScissor(int x, int y, int width, int height2) {
        Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor(x * scaleFactor, mc.displayHeight - (y + height2) * scaleFactor, width * scaleFactor, height2 * scaleFactor);
    }

    public static void drawblock(double a, double a2, double a3, int a4, int a5, float a6) {
        float a7 = (float) (a4 >> 24 & 0xFF) / 255.0f;
        float a8 = (float) (a4 >> 16 & 0xFF) / 255.0f;
        float a9 = (float) (a4 >> 8 & 0xFF) / 255.0f;
        float a10 = (float) (a4 & 0xFF) / 255.0f;
        float a11 = (float) (a5 >> 24 & 0xFF) / 255.0f;
        float a12 = (float) (a5 >> 16 & 0xFF) / 255.0f;
        float a13 = (float) (a5 >> 8 & 0xFF) / 255.0f;
        float a14 = (float) (a5 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(a8, a9, a10, a7);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glLineWidth(a6);
        GL11.glColor4f(a12, a13, a14, a11);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawRect(float left, float top, float right, float bottom, int color) {
        float f3;
        if (left < right) {
            f3 = left;
            left = right;
            right = f3;
        }
        if (top < bottom) {
            f3 = top;
            top = bottom;
            bottom = f3;
        }
        f3 = (float) (color >> 24 & 0xFF) / 255.0f;
        float f = (float) (color >> 16 & 0xFF) / 255.0f;
        float f1 = (float) (color >> 8 & 0xFF) / 255.0f;
        float f2 = (float) (color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer WorldRenderer2 = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        WorldRenderer2.begin(7, DefaultVertexFormats.POSITION);
        WorldRenderer2.pos(left, bottom, 0.0).endVertex();
        WorldRenderer2.pos(right, bottom, 0.0).endVertex();
        WorldRenderer2.pos(right, top, 0.0).endVertex();
        WorldRenderer2.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void color(int color) {
        float f = (float) (color >> 24 & 0xFF) / 255.0f;
        float f1 = (float) (color >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (color >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (color & 0xFF) / 255.0f;
        GL11.glColor4f(f1, f2, f3, f);
    }

    public static int createShader(String shaderCode, int shaderType) throws Exception {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
            if (shader == 0) {
                return 0;
            }
        } catch (Exception exc) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw exc;
        }
        ARBShaderObjects.glShaderSourceARB(shader, shaderCode);
        ARBShaderObjects.glCompileShaderARB(shader);
        if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
            throw new RuntimeException("Error creating shader:");
        }
        return shader;
    }

    public static void drawBorderRect(double x, double y, double x1, double y1, int color, double lwidth) {
        RenderUtil.drawHLine(x, y, x1, y, (float) lwidth, color);
        RenderUtil.drawHLine(x1, y, x1, y1, (float) lwidth, color);
        RenderUtil.drawHLine(x, y1, x1, y1, (float) lwidth, color);
        RenderUtil.drawHLine(x, y1, x, y, (float) lwidth, color);
    }

    public static void drawHLine(double x, double y, double x1, double y1, float width, int color) {
        float var11 = (float) (color >> 24 & 0xFF) / 255.0f;
        float var6 = (float) (color >> 16 & 0xFF) / 255.0f;
        float var7 = (float) (color >> 8 & 0xFF) / 255.0f;
        float var8 = (float) (color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        GL11.glPushMatrix();
        GL11.glLineWidth(width);
        GL11.glBegin(3);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glLineWidth(1.0f);
        GL11.glPopMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void renderOne(float width) {
        RenderUtil.checkSetupFBO();
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(width);
        GL11.glEnable(2848);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void renderTwo() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }

    public static void renderThree() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void renderFour() {
        RenderUtil.setColor(new Color(255, 255, 255));
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

    public static void renderFive() {
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glEnable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }

    public static void setColor(Color c) {
        GL11.glColor4d((float) c.getRed() / 255.0f, (float) c.getGreen() / 255.0f, (float) c.getBlue() / 255.0f, (float) c.getAlpha() / 255.0f);
    }

    public static void checkSetupFBO() {
        Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {
            RenderUtil.setupFBO(fbo);
            fbo.depthBuffer = -1;
        }
    }

    public static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawBox(AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glEnd();
    }

    public static void drawCompleteBox(AxisAlignedBB axisalignedbb, float width, int insideColor, int borderColor) {
        GL11.glLineWidth(width);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        RenderUtil.glColor(insideColor);
        RenderUtil.drawBox(axisalignedbb);
        RenderUtil.glColor(borderColor);
        RenderUtil.drawOutlinedBox(axisalignedbb);
        RenderUtil.drawCrosses(axisalignedbb);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
    }

    public static void drawCrosses(AxisAlignedBB axisalignedbb, float width, int color) {
        GL11.glLineWidth(width);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        RenderUtil.glColor(color);
        RenderUtil.drawCrosses(axisalignedbb);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
    }

    public static void drawOutlineBox(AxisAlignedBB axisalignedbb, float width, int color) {
        GL11.glLineWidth(width);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        RenderUtil.glColor(color);
        RenderUtil.drawOutlinedBox(axisalignedbb);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
    }

    public static void drawOutlinedBox(AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin(3);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
    }

    public static void drawCrosses(AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin(1);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glEnd();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height2, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height2, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawSolidEntityESP(double x, double y, double z, double width, double height2, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height2, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height2, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height2, z + width));
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height2, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height2, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height2, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    private static void glColor(Color color) {
        GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
    }

    public static void drawFilledBox(AxisAlignedBB mask) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ).endVertex();
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        tessellator.draw();
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        RenderUtil.drawRect(x + 0.5f, y, x1 - 0.5f, y + 0.5f, insideC);
        RenderUtil.drawRect(x + 0.5f, y1 - 0.5f, x1 - 0.5f, y1, insideC);
        RenderUtil.drawRect(x, y + 0.5f, x1, y1 - 0.5f, insideC);
    }

    public static void circle(float x, float y, float radius, int fill) {
        RenderUtil.arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void circle(float x, float y, float radius, Color fill) {
        RenderUtil.arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void arc(float x, float y, float start, float end, float radius, int color) {
        RenderUtil.arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arc(float x, float y, float start, float end, float radius, Color color) {
        RenderUtil.arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arcEllipse(float x, float y, float start, float end, float w, float h, int color) {
        float ldy;
        float ldx;
        float i;
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        float var11 = (float) (color >> 24 & 0xFF) / 255.0f;
        float var12 = (float) (color >> 16 & 0xFF) / 255.0f;
        float var13 = (float) (color >> 8 & 0xFF) / 255.0f;
        float var14 = (float) (color & 0xFF) / 255.0f;
        Tessellator var15 = Tessellator.getInstance();
        WorldRenderer var16 = var15.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var12, var13, var14, var11);
        if (var11 > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (i = end; i >= start; i -= 4.0f) {
                ldx = (float) Math.cos((double) i * Math.PI / 180.0) * w * 1.001f;
                ldy = (float) Math.sin((double) i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (i = end; i >= start; i -= 4.0f) {
            ldx = (float) Math.cos((double) i * Math.PI / 180.0) * w;
            ldy = (float) Math.sin((double) i * Math.PI / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void arcEllipse(float x, float y, float start, float end, float w, float h, Color color) {
        float ldy;
        float ldx;
        float i;
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
        if ((float) color.getAlpha() > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (i = end; i >= start; i -= 4.0f) {
                ldx = (float) Math.cos((double) i * Math.PI / 180.0) * w * 1.001f;
                ldy = (float) Math.sin((double) i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (i = end; i >= start; i -= 4.0f) {
            ldx = (float) Math.cos((double) i * Math.PI / 180.0) * w;
            ldy = (float) Math.sin((double) i * Math.PI / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float) (col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float) (col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (col1 & 0xFF) / 255.0f;
        float f4 = (float) (col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float) (col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float) (col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float) (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtil.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }
        float var11 = (float) (color >> 24 & 0xFF) / 255.0f;
        float var6 = (float) (color >> 16 & 0xFF) / 255.0f;
        float var7 = (float) (color >> 8 & 0xFF) / 255.0f;
        float var8 = (float) (color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0).endVertex();
        worldRenderer.pos(right, bottom, 0.0).endVertex();
        worldRenderer.pos(right, top, 0.0).endVertex();
        worldRenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawBorderedRect(double x2, double d2, double x22, double e2, float l1, int col1, int col2) {
        RenderUtil.drawRect(x2, d2, x22, e2, col2);
        float f2 = (float) (col1 >> 24 & 0xFF) / 255.0f;
        float f22 = (float) (col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float) (col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float) (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, d2);
        GL11.glVertex2d(x2, e2);
        GL11.glVertex2d(x22, e2);
        GL11.glVertex2d(x22, d2);
        GL11.glVertex2d(x2, d2);
        GL11.glVertex2d(x22, d2);
        GL11.glVertex2d(x2, e2);
        GL11.glVertex2d(x22, e2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawRect(double d, double e, double g, double h, int color) {
        if (d < g) {
            int i = (int) d;
            d = g;
            g = i;
        }
        if (e < h) {
            int j = (int) e;
            e = h;
            h = j;
        }
        float f3 = (float) (color >> 24 & 0xFF) / 255.0f;
        float f = (float) (color >> 16 & 0xFF) / 255.0f;
        float f1 = (float) (color >> 8 & 0xFF) / 255.0f;
        float f2 = (float) (color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(d, h, 0.0).endVertex();
        worldrenderer.pos(g, h, 0.0).endVertex();
        worldrenderer.pos(g, e, 0.0).endVertex();
        worldrenderer.pos(d, e, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float) ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
        return color3;
    }

    public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(p_147046_0_, p_147046_1_, 40.0f);
        GlStateManager.scale(-p_147046_2_, p_147046_2_, p_147046_2_);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        float var6 = p_147046_5_.renderYawOffset;
        float var7 = p_147046_5_.rotationYaw;
        float var8 = p_147046_5_.rotationPitch;
        float var9 = p_147046_5_.prevRotationYawHead;
        float var10 = p_147046_5_.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-((float) Math.atan(p_147046_4_ / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        p_147046_5_.renderYawOffset = (float) Math.atan(p_147046_3_ / 40.0f) * -14.0f;
        p_147046_5_.rotationYaw = (float) Math.atan(p_147046_3_ / 40.0f) * -14.0f;
        p_147046_5_.rotationPitch = -((float) Math.atan(p_147046_4_ / 40.0f)) * 15.0f;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
        var11.setPlayerViewY(180.0f);
        var11.setRenderShadow(false);
        var11.renderEntityWithPosYaw(p_147046_5_, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        var11.setRenderShadow(true);
        p_147046_5_.renderYawOffset = var6;
        p_147046_5_.rotationYaw = var7;
        p_147046_5_.rotationPitch = var8;
        p_147046_5_.prevRotationYawHead = var9;
        p_147046_5_.rotationYawHead = var10;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void drawRoundRect(double d, double e, double g, double h, int color) {
        RenderUtil.drawRect(d + 1.0, e, g - 1.0, h, color);
        RenderUtil.drawRect(d, e + 1.0, d + 1.0, h - 1.0, color);
        RenderUtil.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        RenderUtil.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        RenderUtil.drawRect(g - 1.0, e + 1.0, g - 0.5, e + 0.5, color);
        RenderUtil.drawRect(g - 1.0, e + 1.0, g, h - 1.0, color);
        RenderUtil.drawRect(d + 1.0, h - 1.0, d + 0.5, h - 0.5, color);
        RenderUtil.drawRect(g - 1.0, h - 1.0, g - 0.5, h - 0.5, color);
    }


    public static void pre() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }

    public static void post() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0, 1.0, 1.0);
    }

    public void drawCircle(int x, int y, float radius, int color) {
        float alpha = (float) (color >> 24 & 0xFF) / 255.0f;
        float red = (float) (color >> 16 & 0xFF) / 255.0f;
        float green = (float) (color >> 8 & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;
        boolean blend = GL11.glIsEnabled(3042);
        boolean line = GL11.glIsEnabled(2848);
        boolean texture = GL11.glIsEnabled(3553);
        if (!blend) {
            GL11.glEnable(3042);
        }
        if (!line) {
            GL11.glEnable(2848);
        }
        if (texture) {
            GL11.glDisable(3553);
        }
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double) x + Math.sin((double) i * 3.141526 / 180.0) * (double) radius, (double) y + Math.cos((double) i * 3.141526 / 180.0) * (double) radius);
        }
        GL11.glEnd();
        if (texture) {
            GL11.glEnable(3553);
        }
        if (!line) {
            GL11.glDisable(2848);
        }
        if (!blend) {
            GL11.glDisable(3042);
        }
    }

    public static class R2DUtils {
        public static void enableGL2D() {
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glDepthMask(true);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glHint(3155, 4354);
        }

        public static void disableGL2D() {
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glHint(3154, 4352);
            GL11.glHint(3155, 4352);
        }

        public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
            R2DUtils.enableGL2D();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            R2DUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
            R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
            R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
            R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
            R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
            R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
            R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
            R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
            R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
            GL11.glScalef(2.0f, 2.0f, 2.0f);
            R2DUtils.disableGL2D();
            Gui.drawRect(0, 0, 0, 0, 0);
        }

        public static void drawRect(double x2, double y2, double x1, double y1, int color) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(color);
            R2DUtils.drawRect(x2, y2, x1, y1);
            R2DUtils.disableGL2D();
        }

        private static void drawRect(double x2, double y2, double x1, double y1) {
            GL11.glBegin(7);
            GL11.glVertex2d(x2, y1);
            GL11.glVertex2d(x1, y1);
            GL11.glVertex2d(x1, y2);
            GL11.glVertex2d(x2, y2);
            GL11.glEnd();
        }

        public static void glColor(int hex) {
            float alpha = (float) (hex >> 24 & 0xFF) / 255.0f;
            float red = (float) (hex >> 16 & 0xFF) / 255.0f;
            float green = (float) (hex >> 8 & 0xFF) / 255.0f;
            float blue = (float) (hex & 0xFF) / 255.0f;
            GL11.glColor4f(red, green, blue, alpha);
        }

        public static void drawRect(float x, float y, float x1, float y1, int color) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(color);
            R2DUtils.drawRect(x, y, x1, y1);
            R2DUtils.disableGL2D();
        }

        public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(borderColor);
            R2DUtils.drawRect(x + width, y, x1 - width, y + width);
            R2DUtils.drawRect(x, y, x + width, y1);
            R2DUtils.drawRect(x1 - width, y, x1, y1);
            R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
            R2DUtils.disableGL2D();
        }

        public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
            R2DUtils.enableGL2D();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            R2DUtils.drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
            R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
            R2DUtils.drawHLine(x, x1 - 1.0f, y, borderC);
            R2DUtils.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
            R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
            GL11.glScalef(2.0f, 2.0f, 2.0f);
            R2DUtils.disableGL2D();
        }

        public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
            R2DUtils.enableGL2D();
            GL11.glShadeModel(7425);
            GL11.glBegin(7);
            R2DUtils.glColor(topColor);
            GL11.glVertex2f(x, y1);
            GL11.glVertex2f(x1, y1);
            R2DUtils.glColor(bottomColor);
            GL11.glVertex2f(x1, y);
            GL11.glVertex2f(x, y);
            GL11.glEnd();
            GL11.glShadeModel(7424);
            R2DUtils.disableGL2D();
        }

        public static void drawHLine(float x, float y, float x1, int y1) {
            if (y < x) {
                float var5 = x;
                x = y;
                y = var5;
            }
            R2DUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
        }

        public static void drawVLine(float x, float y, float x1, int y1) {
            if (x1 < y) {
                float var5 = y;
                y = x1;
                x1 = var5;
            }
            R2DUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
        }

        public static void drawHLine(float x, float y, float x1, int y1, int y2) {
            if (y < x) {
                float var5 = x;
                x = y;
                y = var5;
            }
            R2DUtils.drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
        }

        public static void drawRect(float x, float y, float x1, float y1) {
            GL11.glBegin(7);
            GL11.glVertex2f(x, y1);
            GL11.glVertex2f(x1, y1);
            GL11.glVertex2f(x1, y);
            GL11.glVertex2f(x, y);
            GL11.glEnd();
        }
    }
}

