
package net.ccbluex.liquidbounce.ui.font;

import com.google.gson.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.FileUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SideOnly(Side.CLIENT)
public class Fonts {

    @FontDetails(fontName = "Minecraft Font")
    public static final FontRenderer minecraftFont = Minecraft.getMinecraft().fontRendererObj;
    private static final List<GameFontRenderer> CUSTOM_FONT_RENDERERS = new ArrayList<>();
    // in fact these "roboto medium" is product sans lol
    @FontDetails(fontName = "Roboto Medium", fontSize = 26)
    public static GameFontRenderer font26;
    @FontDetails(fontName = "Roboto Medium", fontSize = 35)
    public static GameFontRenderer font35;
    @FontDetails(fontName = "Roboto Medium", fontSize = 40)
    public static GameFontRenderer font40;

    @FontDetails(fontName = "Roboto Medium", fontSize = 30)
    public static GameFontRenderer font30;

    @FontDetails(fontName = "Roboto Medium", fontSize = 36)
    public static GameFontRenderer font36;
    @FontDetails(fontName = "Roboto Medium", fontSize = 30)
    public static GameFontRenderer fontSmall;
    @FontDetails(fontName = "SFUI Regular", fontSize = 35)
    public static GameFontRenderer fontSFUI35;

    @FontDetails(fontName = "SFUI Regular", fontSize = 40)
    public static GameFontRenderer fontSFUI40;
    @FontDetails(fontName = "SFUI Regular", fontSize = 35)
    public static GameFontRenderer SFUI35;
    @FontDetails(fontName = "Baloo 2 Regular", fontSize = 35)
    public static GameFontRenderer fontBaloo35;
    @FontDetails(fontName = "Baloo 2 Regular", fontSize = 40)
    public static GameFontRenderer fontBaloo40;
    @FontDetails(fontName = "Roboto Bold", fontSize = 180)
    public static GameFontRenderer fontBold180;
    @FontDetails(fontName = "Roboto Bold",fontSize = 40)
    public static GameFontRenderer fontBold40;
    @FontDetails(fontName = "icomoon", fontSize = 35)
    public static GameFontRenderer fontIcon;
    @FontDetails(fontName = "文泉驿微米黑", fontSize = 35)
    public static GameFontRenderer fontChinese;
    @FontDetails(fontName = "Myfont Regular", fontSize = 35)
    public static GameFontRenderer fontIcon2;
    @FontDetails(fontName = "Comfortaa",fontSize = 35)
    public  static  GameFontRenderer fontcomfortaa;
    @FontDetails(fontName = "zaozigongfang",fontSize = 35)
    public  static  GameFontRenderer fontzaozigongfang;
    @FontDetails(fontName = "SFTHIN",fontSize = 35)
    public static GameFontRenderer fontSFTHIN;
    @FontDetails(fontName = "diramight",fontSize = 35)
    public static GameFontRenderer fontdiramight;
    @FontDetails(fontName = "Tenacitycheck",fontSize = 60)
    public static GameFontRenderer Tenacitycheck60;
    @FontDetails(fontName = "tenacitybold",fontSize = 43)
    public static GameFontRenderer TenacityBold43;
    @FontDetails(fontName = "tenacitybold",fontSize = 35)
    public static GameFontRenderer TenacityBold35;

    public static void loadFonts() {
        long l = System.currentTimeMillis();

        ClientUtils.INSTANCE.logInfo("Loading Fonts.");

        ClientUtils.INSTANCE.logInfo("Unpacking Fonts.");
        unpackFonts();

        ClientUtils.INSTANCE.logInfo("Init Fonts.");

        initFonts();

        try {
            CUSTOM_FONT_RENDERERS.clear();

            final File fontsFile = new File(LiquidBounce.fileManager.getFontsDir(), "fonts.json");

            if (fontsFile.exists()) {
                final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(fontsFile)));

                if (jsonElement instanceof JsonNull)
                    return;

                final JsonArray jsonArray = (JsonArray) jsonElement;

                for (final JsonElement element : jsonArray) {
                    if (element instanceof JsonNull)
                        return;

                    final JsonObject fontObject = (JsonObject) element;

                    CUSTOM_FONT_RENDERERS.add(new GameFontRenderer(getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt())));
                }
            } else {
                fontsFile.createNewFile();

                final PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonArray()));
                printWriter.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        ClientUtils.INSTANCE.logInfo("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }

    private static void initFonts() {
        font35 = new GameFontRenderer(getFont("medium.ttf", 35));
        font40 = new GameFontRenderer(getFont("medium.ttf", 40));
        font26 = new GameFontRenderer(getFont("medium.ttf",26));
        font36 = new GameFontRenderer(getFont("medium.ttf",36));
        font30 = new GameFontRenderer(getFont("medium.ttf",30));
        fontBold40 = new GameFontRenderer(getFont("bold.ttf",40));
        fontSmall = new GameFontRenderer(getFont("medium.ttf", 30));
        fontSFUI35 = new GameFontRenderer(getFont("sfui.ttf", 35));
        fontSFUI40 = new GameFontRenderer(getFont("sfui.ttf", 40));
        SFUI35 = new GameFontRenderer(getFont("sfui.ttf", 35));
        fontBaloo35 = new GameFontRenderer(getFont("baloo.ttf", 35));
        fontBaloo40 = new GameFontRenderer(getFont("baloo.ttf", 40));
        fontBold180 = new GameFontRenderer(getFont("bold.ttf", 180));
        fontIcon = new GameFontRenderer(getFont("icon.ttf", 35));
        fontChinese = new GameFontRenderer(getFont("chinese.ttf", 35));
        fontIcon2 = new GameFontRenderer(getFont("stylesicons.ttf", 35));
        fontcomfortaa = new GameFontRenderer(getFont("comfortaa.ttf",35));
        fontzaozigongfang = new GameFontRenderer(getFont("zaozigongfang.ttf",35));
        fontSFTHIN = new GameFontRenderer(getFont("SFTHIN.ttf",35));
        fontdiramight = new GameFontRenderer(getFont("diramight.ttf",35));
    }

    private static void unpackFonts() {
        File sfui = new File(LiquidBounce.fileManager.getFontsDir(), "sfui.ttf");
        File medium = new File(LiquidBounce.fileManager.getFontsDir(), "medium.ttf");
        File bold = new File(LiquidBounce.fileManager.getFontsDir(), "bold.ttf");
        File baloo = new File(LiquidBounce.fileManager.getFontsDir(), "baloo.ttf");
        File icon = new File(LiquidBounce.fileManager.getFontsDir(), "icon.ttf");
        File chinese = new File(LiquidBounce.fileManager.getFontsDir(), "chinese.ttf");
        File icon2 = new File(LiquidBounce.fileManager.getFontsDir(), "stylesicons.ttf");
        File comfortaa = new File(LiquidBounce.fileManager.getFontsDir(),"comfortaa.ttf");
        File zaozigongfang = new File(LiquidBounce.fileManager.getFontsDir(),"zaozigongfang.ttf");
        File SFTHIN = new File(LiquidBounce.fileManager.getFontsDir(),"SFTHIN.ttf");
        File diramight = new File(LiquidBounce.fileManager.getFontsDir(),"diramight.ttf");

        if (!sfui.exists() || !medium.exists() || !bold.exists() || !baloo.exists() || !icon.exists() || !chinese.exists() || !icon2.exists() || !comfortaa.exists() || !zaozigongfang.exists() || !SFTHIN.exists() || !diramight.exists()) {
            FileUtils.unpackFile(sfui, "assets/minecraft/destiny/fonts/sfui.ttf");
            FileUtils.unpackFile(medium, "assets/minecraft/destiny/fonts/medium.ttf");
            FileUtils.unpackFile(bold, "assets/minecraft/destiny/fonts/bold.ttf");
            FileUtils.unpackFile(baloo, "assets/minecraft/destiny/fonts/baloo.ttf");
            FileUtils.unpackFile(icon, "assets/minecraft/destiny/fonts/icon.ttf");
            FileUtils.unpackFile(chinese, "assets/minecraft/destiny/fonts/chinese.ttf");
            FileUtils.unpackFile(icon2, "assets/minecraft/destiny/fonts/stylesicons.ttf");
            FileUtils.unpackFile(comfortaa,"assets/minecraft/destiny/fonts/comfortaa.ttf");
            FileUtils.unpackFile(zaozigongfang,"assets/minecraft/destiny/fonts/zaozigongfang.ttf");
            FileUtils.unpackFile(SFTHIN, "assets/minecraft/destiny/fonts/SFTHIN.ttf");
            FileUtils.unpackFile(diramight,"assets/minecraft/destiny/fonts/diramight.ttf");
        }
    }

    public static FontRenderer getFontRenderer(final String name, final int size) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(null);

                if (o instanceof FontRenderer) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    if (fontDetails.fontName().equals(name) && fontDetails.fontSize() == size)
                        return (FontRenderer) o;
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (final GameFontRenderer liquidFontRenderer : CUSTOM_FONT_RENDERERS) {
            final Font font = liquidFontRenderer.getDefaultFont().getFont();

            if (font.getName().equals(name) && font.getSize() == size)
                return liquidFontRenderer;
        }

        return minecraftFont;
    }

    public static Object[] getFontDetails(final FontRenderer fontRenderer) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(null);

                if (o.equals(fontRenderer)) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    return new Object[]{fontDetails.fontName(), fontDetails.fontSize()};
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (fontRenderer instanceof GameFontRenderer) {
            final Font font = ((GameFontRenderer) fontRenderer).getDefaultFont().getFont();

            return new Object[]{font.getName(), font.getSize()};
        }

        return null;
    }

    public static List<FontRenderer> getFonts() {
        final List<FontRenderer> fonts = new ArrayList<>();

        for (final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);

                final Object fontObj = fontField.get(null);

                if (fontObj instanceof FontRenderer) fonts.add((FontRenderer) fontObj);
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS);

        return fonts;
    }

    private static Font getFont(final String fontName, final int size) {
        try {
            final InputStream inputStream = new FileInputStream(new File(LiquidBounce.fileManager.getFontsDir(), fontName));
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            return awtClientFont;
        } catch (final Exception e) {
            e.printStackTrace();

            return new Font("default", Font.PLAIN, size);
        }
    }
}