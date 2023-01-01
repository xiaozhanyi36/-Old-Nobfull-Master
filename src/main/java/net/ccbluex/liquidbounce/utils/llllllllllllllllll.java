package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.Minecraft;

public class llllllllllllllllll {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isOnGround(double height) {
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
