package net.ccbluex.liquidbounce.utils.HWID;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.swing.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Checker {
    public static boolean IllIlIlIllllIIlIl() {
        LiquidBounce.INSTANCE.setUsingBackground(false);
        boolean ver = false;
        try {
            String LLL = WebUtils.get("https://raw.fastgit.org/xiaozhanyi36/hwid_noblefull/main/hwid.txt");
            String HWID = HWIDUtils.getHWID();
            if (!LLL.contains("Ver:043022")) {
                JOptionPane.showMessageDialog(null, "你正在使用一个旧版本，请在内部下载最新版本", "验证失败", JOptionPane.ERROR_MESSAGE);
                FMLCommonHandler.instance().exitJava(0, true);
            }
            if (!LLL.contains(HWID)) {
                HWIDUtils.setClipboardString();
                JOptionPane.showInputDialog(null, "您的hwid不在验证库", HWID);
                FMLCommonHandler.instance().exitJava(0, true);
            } else ver = true;
        } catch (NoSuchAlgorithmException | IOException e) {
            JOptionPane.showMessageDialog(null, "链接验证库失败，请重启游戏", "验证失败", JOptionPane.ERROR_MESSAGE);
            FMLCommonHandler.instance().exitJava(0, true);
        }
        return ver;
    }
    public static boolean IlllIIlllllIlIl(String username, String password) {
        boolean ver = false;
        try {
            String LLL = WebUtils.get("https://raw.fastgit.org/xiaozhanyi36/hwid_noblefull/main/hwid.txt");
            String HWID = HWIDUtils.getHWID();

            String rank = getSubString(LLL, HWID + "-", username + ":" + password + ";");
            switch (rank) {
                case "[Dev]":
                    LiquidBounce.rankedName = "§c" + rank + username;
                    LiquidBounce.cleanName = rank + username;
                    ver = true;
                    break;
                case "[Beta]":
                    LiquidBounce.rankedName = "§a" + rank + username;
                    LiquidBounce.cleanName = rank + username;
                    ver = true;
                    break;
                case "[User]":
                    LiquidBounce.rankedName = rank + username;
                    LiquidBounce.cleanName = rank + username;
                    ver = true;
                    break;
                case "[MVP++]":
                    LiquidBounce.rankedName = "§4"+ rank + username;
                    LiquidBounce.cleanName = rank+username;
                    ver = true;
                    break;
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ver;
    }

    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }
}
