/*
 * Decompiled with CFR 0_132.
 */
package net.ccbluex.liquidbounce.ui.font;

import java.awt.Font;
import java.io.InputStream;


import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class FontLoaders {


    public static CFontRenderer F14 = new CFontRenderer(FontLoaders.getFont(14), true, true);
    public static CFontRenderer F16 = new CFontRenderer(FontLoaders.getFont(16), true, true);
    public static CFontRenderer F18 = new CFontRenderer(FontLoaders.getFont(18), true, true);
    public static CFontRenderer F20 = new CFontRenderer(FontLoaders.getFont(20), true, true);
    public static CFontRenderer F22 = new CFontRenderer(FontLoaders.getFont(22), true, true);
    public static CFontRenderer F23 = new CFontRenderer(FontLoaders.getFont(23), true, true);
    public static CFontRenderer F24 = new CFontRenderer(FontLoaders.getFont(24), true, true);
    public static CFontRenderer F30 = new CFontRenderer(FontLoaders.getFont(30), true, true);
    public static CFontRenderer F40 = new CFontRenderer(FontLoaders.getFont(40), true, true);

    public static CFontRenderer C12 = new CFontRenderer(FontLoaders.getComfortaa(12), true, true);
    public static CFontRenderer C13 = new CFontRenderer(FontLoaders.getComfortaa(13), true, true);
    public static CFontRenderer C14 = new CFontRenderer(FontLoaders.getComfortaa(14), true, true);
    public static CFontRenderer C16 = new CFontRenderer(FontLoaders.getComfortaa(16), true, true);
    public static CFontRenderer C18 = new CFontRenderer(FontLoaders.getComfortaa(18), true, true);
    public static CFontRenderer C20 = new CFontRenderer(FontLoaders.getComfortaa(20), true, true);
    public static CFontRenderer C22 = new CFontRenderer(FontLoaders.getComfortaa(22), true, true);
    public static CFontRenderer C30 = new CFontRenderer(FontLoaders.getComfortaa(30), true, true);
    public static CFontRenderer C35 = new CFontRenderer(FontLoaders.getComfortaa(35), true, true);
    public static CFontRenderer C40 = new CFontRenderer(FontLoaders.getComfortaa(40), true, true);

    public static CFontRenderer FLUXICON16 = new CFontRenderer(FontLoaders.FLUX(16), true, true);

    public static CFontRenderer guiicons18  = new CFontRenderer(FontLoaders.GuiICONS(18), true, true);
    public static CFontRenderer guiicons24  = new CFontRenderer(FontLoaders.GuiICONS(24), true, true);
    public static CFontRenderer guiicons22  = new CFontRenderer(FontLoaders.GuiICONS(22), true, true);
    public static CFontRenderer guiicons28   = new CFontRenderer(FontLoaders.GuiICONS(28), true, true);
    public static CFontRenderer guiicons20   = new CFontRenderer(FontLoaders.GuiICONS(20), true, true);
    public static CFontRenderer guiicons40   = new CFontRenderer(FontLoaders.GuiICONS(40), true, true);

    public static CFontRenderer icon18   = new CFontRenderer(FontLoaders.getIcon(18), true, true);
    public static CFontRenderer icon24   = new CFontRenderer(FontLoaders.getIcon(24), true, true);

    public static CFontRenderer icon   = new CFontRenderer(FontLoaders.GUIICONS2(18), true, true);
    public static CFontRenderer icon20    = new CFontRenderer(FontLoaders.GUIICONS2(20), true, true);
    public static CFontRenderer icon40    = new CFontRenderer(FontLoaders.GUIICONS2(20), true, true);

    public static CFontRenderer kiona22 = new CFontRenderer(FontLoaders.getKiona(22), true, true);
    public static CFontRenderer kiona26 = new CFontRenderer(FontLoaders.getKiona(26), true, true);
    public static CFontRenderer kiona48 = new CFontRenderer(FontLoaders.getKiona(48), true, true);

    public static CFontRenderer GoogleSans24 = new CFontRenderer(FontLoaders.getGoogleSans(24), true, true);
    public static CFontRenderer GoogleSans36 = new CFontRenderer(FontLoaders.getGoogleSans(36), true, true);
    public static CFontRenderer GoogleSans55 = new CFontRenderer(FontLoaders.getGoogleSans(55), true, true);
    public static CFontRenderer GoogleSans18 = new CFontRenderer(FontLoaders.getGoogleSans(18), true, true);
    public static CFontRenderer GoogleSans12 = new CFontRenderer(FontLoaders.getGoogleSans(12), true, true);
    public static CFontRenderer GoogleSans14 = new CFontRenderer(FontLoaders.getGoogleSans(14), true, true);

    public static CFontRenderer xyz16 = new CFontRenderer(FontLoaders.getxyz(16), true, true);
    public static CFontRenderer xyz18 = new CFontRenderer(FontLoaders.getxyz(18), true, true);
    public static CFontRenderer xyz20 = new CFontRenderer(FontLoaders.getxyz(20), true, true);
    public static CFontRenderer xyz28 = new CFontRenderer(FontLoaders.getxyz(28), true, true);
    public static CFontRenderer xyz26 = new CFontRenderer(FontLoaders.getxyz(26), true, true);

    public static CFontRenderer calibrilite65 = new CFontRenderer(FontLoaders.getcalibrilite(65), true, true);

    public static CFontRenderer Stylesicons50 = new CFontRenderer(FontLoaders.getStylesicons(50), true, true);

    public static CFontRenderer NovICON44 = new CFontRenderer(FontLoaders.getNovICON(44), true, true);

    public static CFontRenderer SessionInfo16 = new CFontRenderer(FontLoaders.getSessionInfo(16), true, true);

    public static CFontRenderer NovICON10 = new CFontRenderer(FontLoaders.getNovICON(10), true, true);

    private static Font getNovICON(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("fdpclient/font/NovICON.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getSessionInfo(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("fdpclient/font/SessionInfo.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    private static Font getGoogleSans(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("fdpclient/font/GoogleSans.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getxyz(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("fdpclient/font/Arial.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static Font getFont(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("fdpclient/font/regular.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    private static Font getKiona(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("fdpclient/font/Kiona.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static Font getComfortaa(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("fdpclient/font/comfortaa.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static Font FLUX(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("fdpclient/font/fluxicon.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static Font getIcon(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("fdpclient/font/icon.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static Font GuiICONS(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("fdpclient/font/GuiICONS.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static Font GUIICONS2(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("fdpclient/font/GuiICONS2.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getcalibrilite(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("fdpclient/font/calibrilites.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getStylesicons(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("fdpclient/font/stylesicons.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
}