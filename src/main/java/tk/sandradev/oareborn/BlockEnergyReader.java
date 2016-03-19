package tk.sandradev.oareborn;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import tk.sandradev.oareborn.internal.BlockPointerOrSided;

public class BlockEnergyReader extends BlockPointerOrSided implements ITileEntityProvider {
    @Override
    public boolean enabledByDefault() {
        return true;
    }

    @Override
    public String type() {
        return "EnergyReader";
    }
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side) {
        if (!super.onBlockActivated(world, pos, state, player, side) && !world.isRemote) {
            player.addChatComponentMessage(new TextComponentString(((Integer) ((TE) world.getTileEntity(pos)).storage.getEnergyStored()).toString()));
        }
        return true;
    }
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TE();
    }

    public static class TE extends TileEntity implements ITickable,IEnergyReceiver,IEnergyProvider {
        protected EnergyStorage storage = new EnergyStorage(128000,64000);
        protected EnumFacing lastFacing = EnumFacing.DOWN;
        @Override
        public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
            return ((BlockEnergyReader) blockType).getSide(worldObj, pos, worldObj.getBlockState(pos), lastFacing) == from ? storage.extractEnergy(maxExtract, simulate) : 0;
        }

        @Override
        public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
            lastFacing = from;
            return storage.receiveEnergy(maxReceive, simulate);
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
        public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
        {
            return oldState.getBlock() != newState.getBlock();
        }

        @Override
        public void update() {
            if (worldObj == null || worldObj.isRemote) return;
            EnumFacing side = OAReborn.energyReader.getSide(worldObj,pos,worldObj.getBlockState(pos),lastFacing);
            TileEntity te = worldObj.getTileEntity(pos.offset(side));
            if (te != null && te instanceof IEnergyReceiver)
            {
                ((IEnergyReceiver) te).receiveEnergy(side.getOpposite(),storage.extractEnergy(storage.getMaxExtract(),false),false);
            }
        }
    }
}
