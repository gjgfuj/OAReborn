package tk.sandradev.oareborn.lasers.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import tk.sandradev.oareborn.api.lasers.ILaser;
import tk.sandradev.oareborn.api.lasers.ILaserReceiver;
import tk.sandradev.oareborn.internal.BlockPointerOrSided;

/**
 * Created by Sandra on 12/01/2016.
 */
public class BlockLaserReceiver extends BlockPointerOrSided implements ILaserReceiver {
    @Override
    public boolean enabledByDefault() {
        return false;
    }

    @Override
    public String type() {
        return "LaserReceiver";
    }

    @Override
    public Object receiveLaser(World world, BlockPos pos, IBlockState state, EnumFacing face, ILaser laser) {
        return laser.receiveIn(world, pos.offset(getSide(world,pos,state,face)), state, face);
    }

    @Override
    public boolean canReceiveLaser(World world, BlockPos pos, IBlockState state, EnumFacing face, ILaser laser) {
        return laser.canReceiveIn(world, pos.offset(getSide(world,pos,state,face)), state, face);
    }
}
