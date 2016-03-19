package tk.sandradev.oareborn.api.lasers.types;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import tk.sandradev.oareborn.api.lasers.ILaser;
import tk.sandradev.oareborn.api.lasers.SplitLaser;

public class LaserEnergy implements ILaser {
    public int amount;
    public int loss;
    public LaserEnergy(int amount)
    {
        this(amount,300);
    }
    public LaserEnergy(int amount, int loss) {this.amount = amount; this.loss=loss;}
    @Override
    public String getType() {
        return "energy";
    }

    @Override
    public boolean canMultiDestination() {
        return true;
    }

    @Override
    public SplitLaser split() {
        return new SplitLaser(new LaserEnergy(amount/2,loss),new LaserEnergy(amount/2,loss));
    }

    @Override
    public Object receiveIn(World world, BlockPos pos, IBlockState state, EnumFacing face) {
        return Math.max(((IEnergyReceiver) world.getTileEntity(pos)).receiveEnergy(face,Math.max(amount-loss,0),false)+loss,amount);
    }

    @Override
    public boolean canReceiveIn(World world, BlockPos pos, IBlockState state, EnumFacing face) {
        TileEntity te = world.getTileEntity(pos);
        return te != null && te instanceof IEnergyReceiver && ((IEnergyReceiver) te).receiveEnergy(face,amount,true) > 0;
    }

    @Override
    public void transitCallback(BlockPos start, BlockPos end) {
         BlockPos value = start.subtract(end);
         int dist = value.getX() != 0 ? value.getX() : (value.getY() != 0 ? value.getY() : value.getZ());
         loss += Math.abs(dist)*100;
    }
}
