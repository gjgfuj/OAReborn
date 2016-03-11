package tk.sandradev.oareborn.lasers;
/*
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tk.sandradev.oareborn.repackage.vazkii.psi.Message;
/**
 * Created by Sandra on 13/01/2016.
 *
 * /
public class LaserMessage extends Message<LaserMessage> {
    public BlockPos pos;
    public BlockPos finalPos;
    public int facing;
    public EnumFacing enumFacing;

    public LaserMessage() {
    }

    public LaserMessage(BlockPos p, BlockPos fp, EnumFacing facing) {
        pos = p;
        finalPos = fp;
        this.enumFacing = facing;
        this.facing = enumFacing.getIndex();
    }

    public IMessage handleMessage(MessageContext context) {
        enumFacing = EnumFacing.VALUES[facing];
        BlockPos cp = pos;
        while (!cp.equals(finalPos)) {
            Minecraft.getMinecraft().theWorld.spawnParticle(EnumParticleTypes.REDSTONE, cp.getX() + 0.5, cp.getY() + 0.5, cp.getZ() + 0.5, 0, 0, 0);
            cp = cp.offset(enumFacing);
        }
        return null;
    }

}
 */
