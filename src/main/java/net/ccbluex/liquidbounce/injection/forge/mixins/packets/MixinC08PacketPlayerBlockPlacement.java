package net.ccbluex.liquidbounce.injection.forge.mixins.packets;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.ccbluex.liquidbounce.features.module.modules.exploit.PacketFixer;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;


@Mixin(C08PacketPlayerBlockPlacement.class)

public abstract class MixinC08PacketPlayerBlockPlacement {
    @Shadow private BlockPos position;

    @Shadow private int placedBlockDirection;

    @Shadow public ItemStack stack;

    @Shadow public float facingX;

    @Shadow public float facingY;

    @Shadow public float facingZ;

    /**
     * @author
     */
    @Overwrite()
    public void readPacketData(PacketBuffer p_readPacketData_1_)throws IOException{
            this.position = p_readPacketData_1_.readBlockPos();
            this.placedBlockDirection = p_readPacketData_1_.readUnsignedByte();
            this.stack = p_readPacketData_1_.readItemStackFromBuffer();
            this.facingX = (float)p_readPacketData_1_.readUnsignedByte() / 1.0F;
            this.facingY = (float)p_readPacketData_1_.readUnsignedByte() / 1.0F;
            this.facingZ = (float)p_readPacketData_1_.readUnsignedByte() / 1.0F;

    }
    /**
     * @author
     */
    @Overwrite()

    public void writePacketData(PacketBuffer p_writePacketData_1_) throws IOException{

            p_writePacketData_1_.writeBlockPos(this.position);
            p_writePacketData_1_.writeByte(this.placedBlockDirection);
            p_writePacketData_1_.writeItemStackToBuffer(this.stack);
            p_writePacketData_1_.writeByte((int) (this.facingX * 1.0F));
            p_writePacketData_1_.writeByte((int) (this.facingY * 1.0F));
            p_writePacketData_1_.writeByte((int) (this.facingZ * 1.0F));

    }

}
