package tk.sandradev.oareborn.lasers.block;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import tk.sandradev.oareborn.api.lasers.LaserUtil;
import tk.sandradev.oareborn.api.lasers.SplitResult;
import tk.sandradev.oareborn.api.lasers.types.LaserEnergy;
import tk.sandradev.oareborn.internal.BlockPointerOrSided;
import tk.sandradev.oareborn.lasers.OARebornLasers;

public class BlockLaserEnergy extends BlockPointerOrSided implements ITileEntityProvider {
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TE();
    }

    @Override
    public boolean enabledByDefault() {
        return true;
    }

    @Override
    public String type() {
        return "LaserEnergy";
    }

    public static class TE extends TileEntity implements IEnergyReceiver, ITickable {
        int counter = 0;
        int rate = 20;
        EnumFacing lastSide = EnumFacing.NORTH;
        //TODO: ADD CAPABILITY SUPPORT
        EnergyStorage storage = new EnergyStorage(64000, 32000);

        @Override
        public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
            return storage.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public Packet getDescriptionPacket() {
            SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(pos, getBlockMetadata(), new NBTTagCompound());
            packet.getNbtCompound().setInteger("energy", storage.getEnergyStored());
            return packet;
        }

        @Override
        public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
            storage.setEnergyStored(packet.getNbtCompound().getInteger("energy"));
        }

        @Override
        public int getEnergyStored(EnumFacing from) {
            return storage.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored(EnumFacing from) {
            return storage.getMaxEnergyStored();
        }

        @Override
        public boolean canConnectEnergy(EnumFacing from) {
            return true;
        }

        public void readFromNBT(NBTTagCompound tag) {
            super.readFromNBT(tag);
            storage.readFromNBT(tag);
        }

        public void writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            storage.writeToNBT(tag);
        }

        public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
            return oldState.getBlock() != newState.getBlock();
        }
        public int getAmountSent(Object result, int counter)
        {
            if (result instanceof SplitResult)
            {
                counter = getAmountSent(((SplitResult) result).result1,counter);
                counter = getAmountSent(((SplitResult) result).result2,counter);
            }else
            {
                counter += ((Integer) result);
            }
            return counter;
        }
        @Override
        public void update() {
            if (worldObj == null || worldObj.isRemote) return;
            counter++;
            if (counter >= rate && storage.getEnergyStored() >= 1000)
            {
                EnumFacing side = OARebornLasers.laserEnergy.getSide(worldObj,pos,worldObj.getBlockState(pos),lastSide);
                LaserEnergy laser = new LaserEnergy(storage.getEnergyStored());
                if (LaserUtil.canSendLaser(worldObj,pos,side,laser))
                {
                    Object result = LaserUtil.sendLaser(worldObj,pos,side,laser);
                    int finalamountsent = getAmountSent(result,0);
                    storage.extractEnergy(finalamountsent,false);
                }
                counter = 0;
            }
        }

    }
}