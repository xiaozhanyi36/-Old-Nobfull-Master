/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.*;

@ModuleInfo(name = "NoFall", description = "Prevents you from taking fall damage.", category = ModuleCategory.PLAYER)
public class NoFall1 extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Packet","Hypixel","SpoofGround", "NoGround","AAC","AAC5"}, "SpoofGround");
    private int state;

    private boolean aac5doFlag=false;
    private boolean aac5Check=false;
    private int aac5Timer=0;

    @Override
    public void onEnable() {
        aac5Check=false;
        aac5doFlag=false;
        aac5Timer=0;
    }

    @EventTarget(ignoreCondition = true)
    public void onUpdate(UpdateEvent event) {
        if (!getState() || LiquidBounce.moduleManager.getModule(FreeCam.class).getState())
            return;

        if (BlockUtils.collideBlock(mc.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLiquid) ||
                BlockUtils.collideBlock(new AxisAlignedBB(mc.thePlayer.getEntityBoundingBox().maxX, mc.thePlayer.getEntityBoundingBox().maxY, mc.thePlayer.getEntityBoundingBox().maxZ, mc.thePlayer.getEntityBoundingBox().minX, mc.thePlayer.getEntityBoundingBox().minY - 0.01D, mc.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid))
            return;

        switch (modeValue.get().toLowerCase()) {
            case "packet":
                if (mc.thePlayer.fallDistance > 2F ) {
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer(true));
                    break;
                }
                break;
            case "hypixel":{
                if(mc.thePlayer.onGround){
                    mc.thePlayer.fallDistance = 0.5F;
                }
                if(mc.thePlayer.fallDistance > 2) {
                    mc.thePlayer.onGround = false;
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                }
                break;
            }

            case "aac5": {
                double offsetYs = 0.0;
                aac5Check=false;
                while (mc.thePlayer.motionY-1.5 < offsetYs) {
                    final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + offsetYs, mc.thePlayer.posZ);
                    final Block block = BlockUtils.getBlock(blockPos);
                    final AxisAlignedBB axisAlignedBB = block.getCollisionBoundingBox(mc.theWorld, blockPos, BlockUtils.getState(blockPos));
                    if(axisAlignedBB != null) {
                        offsetYs = -999.9;
                        aac5Check=true;
                    }
                    offsetYs -= 0.5;
                }
                if(mc.thePlayer.onGround) {
                    mc.thePlayer.fallDistance=-2;
                    aac5Check=false;
                }
                if(aac5Timer>0) {
                    aac5Timer -= 1;
                }
                if(aac5Check && mc.thePlayer.fallDistance>3.125 && !mc.thePlayer.onGround) {
                    aac5doFlag=true;
                    aac5Timer = 16;
                    //chat("test");
                }else {
                    if(aac5Timer<2) aac5doFlag=false;
                }
                if(aac5doFlag) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY + 0.5, mc.thePlayer.posZ, true));
                }
                break;
            }

            case "aac":
                if (mc.thePlayer.fallDistance > 2F) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                    state = 2;
                } else if (state == 2 && mc.thePlayer.fallDistance < 2) {
                    mc.thePlayer.motionY = 0.1D;
                    state = 3;
                    return;
                }

                switch (state) {
                    case 3:
                        mc.thePlayer.motionY = 0.1D;
                        state = 4;
                        break;
                    case 4:
                        mc.thePlayer.motionY = 0.1D;
                        state = 5;
                        break;
                    case 5:
                        mc.thePlayer.motionY = 0.1D;
                        state = 1;
                        break;
                }
                break;
        }
    }

    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet<?> packet = event.getPacket();
        final String mode = modeValue.get();

        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer playerPacket = (C03PacketPlayer) packet;

            if (mode.equalsIgnoreCase("SpoofGround"))
                playerPacket.onGround = true;

            if (mode.equalsIgnoreCase("NoGround"))
                playerPacket.onGround = false;

            if (mode.equalsIgnoreCase("Hypixel")
                    && mc.thePlayer != null && mc.thePlayer.fallDistance > 1.5)
                playerPacket.onGround = mc.thePlayer.ticksExisted % 2 == 0;
        }
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}
