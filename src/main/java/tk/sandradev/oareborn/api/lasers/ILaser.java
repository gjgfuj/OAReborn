package tk.sandradev.oareborn.api.lasers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Sandra on 12/01/2016.
 */
public interface ILaser {
    String getType();
    boolean canMultiDestination();
    SplitLaser split();
    Object receiveIn(World world, BlockPos pos, IBlockState state, EnumFacing face);

    boolean canReceiveIn(World world, BlockPos pos, IBlockState state, EnumFacing face);

    void transitCallback(BlockPos start,BlockPos end);
}
