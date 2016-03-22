package tk.sandradev.oareborn.api.integration.oc.lasers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import tk.sandradev.oareborn.api.lasers.ILaser;
import tk.sandradev.oareborn.api.lasers.SplitLaser;

public class LaserData implements ILaser {

    public String address;
    public Object[] data;

    public LaserData(String address,Object[] data)
    {
        this.address = address;
        this.data = data;
    }
    public LaserData(Object[] data)
    {
        this(null,data);
    }
    @Override
    public String getType() {
        return "OCData";
    }

    @Override
    public boolean canMultiDestination() {
        return true;
    }

    @Override
    public SplitLaser split() {
        return new SplitLaser(this,this);
    }

    @Override
    public Object receiveIn(World world, BlockPos pos, IBlockState state, EnumFacing face) {
        return this;
    }

    //Only can be received by a data laser.
    @Override
    public boolean canReceiveIn(World world, BlockPos pos, IBlockState state, EnumFacing face) {
        return false;
    }

    @Override
    public void transitCallback(BlockPos start, BlockPos end) {

    }
}
