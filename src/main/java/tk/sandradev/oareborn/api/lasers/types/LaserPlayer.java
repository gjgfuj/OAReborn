package tk.sandradev.oareborn.api.lasers.types;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import tk.sandradev.oareborn.api.lasers.ILaser;
import tk.sandradev.oareborn.api.lasers.SplitLaser;

/**
 * Created by Sandra on 12/01/2016.
 */
public class LaserPlayer implements ILaser {
    EntityPlayer player;

    public LaserPlayer(EntityPlayer player) {
        this.player = player;
    }

    public String getType() {
        return "PLAYER";
    }

    @Override
    public boolean canMultiDestination() {
        return false;
    }

    @Override
    public SplitLaser split() {
        return new SplitLaser(this,this);
    }

    @Override
    public Object receiveIn(World world, BlockPos pos, IBlockState state, EnumFacing face) {
        if (player.worldObj != world)
            player.travelToDimension(world.provider.getDimensionId());
        player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        return null;
    }

    @Override
    public boolean canReceiveIn(World world, BlockPos pos, IBlockState state, EnumFacing face) {
        return world.isAirBlock(pos);
    }

    @Override
    public void transitCallback(BlockPos start, BlockPos end) {

    }
}
