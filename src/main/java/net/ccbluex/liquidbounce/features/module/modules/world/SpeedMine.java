
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "SpeedMine", description = "SpeedMine", category = ModuleCategory.WORLD)
public class SpeedMine extends Module {
    public final ListValue modeValue = new ListValue("modeValue", new String[]{"Packet", "NewPacket", "NewPacket2", "Remix"}, "Packet");
    private boolean bzs;
    private float bzx;
    public BlockPos blockPos;
    public EnumFacing facing;

    @EventTarget
    private void onUpdate(final UpdateEvent e) {
        if (modeValue.get().equals("Packet")) {
            mc.playerController.blockHitDelay = 0;
            if (mc.playerController.curBlockDamageMP >= 0.5f) {
                mc.playerController.curBlockDamageMP = 1.0f;
            }
        }
        if (modeValue.get().equals("NewPacket2")) {
            if (mc.playerController.curBlockDamageMP == 0.2f) {
                final PlayerControllerMP playerController = mc.playerController;
                playerController.curBlockDamageMP += 0.1f;
            }
            if (mc.playerController.curBlockDamageMP == 0.4f) {
                final PlayerControllerMP playerController2 = mc.playerController;
                playerController2.curBlockDamageMP += 0.1f;
            }
            if (mc.playerController.curBlockDamageMP == 0.6f) {
                final PlayerControllerMP playerController3 = mc.playerController;
                playerController3.curBlockDamageMP += 0.1f;
            }
            if (mc.playerController.curBlockDamageMP == 0.8f) {
                final PlayerControllerMP playerController4 = mc.playerController;
                playerController4.curBlockDamageMP += 0.1f;
            }
        }
        if (modeValue.get().equals("NewPacket")) {
            if (mc.playerController.curBlockDamageMP == 0.1f) {
                final PlayerControllerMP playerController5 = mc.playerController;
                playerController5.curBlockDamageMP += 0.1f;
            }
            if (mc.playerController.curBlockDamageMP == 0.4f) {
                final PlayerControllerMP playerController6 = mc.playerController;
                playerController6.curBlockDamageMP += 0.1f;
            }
            if (mc.playerController.curBlockDamageMP == 0.7f) {
                final PlayerControllerMP playerController7 = mc.playerController;
                playerController7.curBlockDamageMP += 0.1f;
            }
        }
        if (modeValue.get().equals("Remix")) {
            if (mc.playerController.extendedReach()) {
                mc.playerController.blockHitDelay = 0;
            }
            else if (this.bzs) {
                final Block block = mc.theWorld.getBlockState(this.blockPos).getBlock();
                this.bzx += (float)(block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, this.blockPos) * 1.4);
                if (this.bzx >= 1.0f) {
                    mc.theWorld.setBlockState(this.blockPos, Blocks.air.getDefaultState(), 11);
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.facing));
                    this.bzx = 0.0f;
                    this.bzs = false;
                }
            }
        }
    }

    @EventTarget
    public void onDamageBlock(final PacketEvent event) {
        if (modeValue.get().equals("Remix") && event.getPacket() instanceof C07PacketPlayerDigging && !mc.playerController.extendedReach() && mc.playerController != null) {
            final C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging)event.getPacket();
            if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                this.bzs = true;
                this.blockPos = c07PacketPlayerDigging.getPosition();
                this.facing = c07PacketPlayerDigging.getFacing();
                this.bzx = 0.0f;
            }
            else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                this.bzs = false;
                this.blockPos = null;
                this.facing = null;
            }
        }
    }
    
    
    @Override
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
    }

    public Block getBlock(final double x, final double y, final double z) {
        final BlockPos bp = new BlockPos(x, y, z);
        return mc.theWorld.getBlockState(bp).getBlock();
    }
    @Override
    public String getTag() {
        return modeValue.get();
    }
}
