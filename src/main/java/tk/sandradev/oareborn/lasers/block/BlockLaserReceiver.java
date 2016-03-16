package tk.sandradev.oareborn.lasers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import tk.sandradev.oareborn.api.lasers.ILaser;
import tk.sandradev.oareborn.api.lasers.ILaserReceiver;

/**
 * Created by Sandra on 12/01/2016.
 */
public class BlockLaserReceiver extends BlockLaserSender implements ILaserReceiver {
    public BlockLaserReceiver() {

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
