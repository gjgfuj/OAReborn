package tk.sandradev.oareborn.api.lasers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Implement on blocks that can receive lasers.
 */
public interface ILaserReceiver {
    Object receiveLaser(World world, BlockPos pos, IBlockState state, EnumFacing face, ILaser laser);

    boolean canReceiveLaser(World world, BlockPos pos, IBlockState state, EnumFacing face, ILaser laser);
}
